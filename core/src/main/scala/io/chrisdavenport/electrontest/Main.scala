package io.chrisdavenport.electrontest

import cats.effect._
// import io.chrisdavenport.electrontest.internal.jsdeps.node._
import io.chrisdavenport.electrontest.internal.jsdeps.electron.mod.{BrowserWindow, NodeEventEmitter}

import io.chrisdavenport.electrontest.internal.jsdeps.electron.electronRequire
import io.chrisdavenport.electrontest.internal.jsdeps.electron.global.Electron.app
// import fs2.internal.jsdeps.node.eventsMod
import cats.effect.unsafe.IORuntime.global


object App extends NodeEventEmitter

object Main  {

  def main(args: Array[String]): Unit = {
    electronRequire
    app.on("ready", {_: Any => println("Hey There")})
    
  }

  // def run(args: List[String]): IO[ExitCode] = {
  //   for {
  //     finished <- Deferred[IO, Unit]
  //     // window <- IO(new BrowserWindow())
  //     _ <- IO()
  //     _ <- IO(App.on("window-all-closed", {_: Any => finished.complete(()).unsafeRunAndForget()(global)}))
  //     _ <- finished.get
  //   } yield ExitCode.Success
  // }

}
