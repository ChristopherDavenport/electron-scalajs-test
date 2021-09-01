package io.chrisdavenport.electrontest.render

import cats.syntax.all._
import cats.effect._
import cats.effect.unsafe.IORuntime.global

import io.chrisdavenport.electrontest.internal.jsdeps.node
import org.scalajs.dom
import org.scalajs.dom.{CanvasRenderingContext2D, document, html}
import cats.effect.unsafe.IORuntime
import cats.effect.std.Dispatcher

/*
// All of the Node.js APIs are available in the preload process.
// It has the same sandbox as a Chrome extension.
*/
object Main extends ResourceApp  {

  def getVersion(s: String): IO[String] = IO.defer{
    node.processMod.versions.get(s).flatMap(_.toOption) match {
      case None => IO.raiseError(new Throwable(s"Version for $s not found"))
      case Some(a) => 
        IO.pure(a)
    }
  }

  def replaceText(elementId: String, value: String): IO[Unit] = IO{
    val element = dom.document.getElementById(elementId)
    println("")
    element.innerText = value
  }

  def run(args: List[String]): Resource[IO, ExitCode] = {
    val processes = List("chrome", "node", "electron")
    val app = processes.traverse(getVersion).map(processes.map(_ + "-version").zip(_))
      .flatMap(_.traverse_{ case (v, s) => replaceText(v,s)})
    for {
      dispatcher <- Dispatcher[IO]
      set <- Resource.eval(
        IO(
          dom.window.onload = {_: dom.Event =>
            println("onLoad Called")
            app.unsafeRunAndForget()(IORuntime.global) // This runs after this app exits. Global Dispatcher?
          }
        )
      )
      _ <- Resource.eval(IO(println("Test Preload")))
    } yield ExitCode.Success
  }

}
/*
window.addEventListener('DOMContentLoaded', () => {
  const replaceText = (selector, text) => {
    const element = document.getElementById(selector)
    if (element) element.innerText = text
  }

  for (const type of ['chrome', 'node', 'electron']) {
    replaceText(`${type}-version`, process.versions[type])
  }
})
*/
