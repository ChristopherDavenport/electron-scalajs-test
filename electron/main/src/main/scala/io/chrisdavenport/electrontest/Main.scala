package io.chrisdavenport.electrontest

import cats.effect._
// import io.chrisdavenport.electrontest.internal.jsdeps.node._
import io.chrisdavenport.electrontest.internal.jsdeps.electron.mod.{BrowserWindow, NodeEventEmitter}

import io.chrisdavenport.electrontest.internal.jsdeps.electron.electronRequire
import io.chrisdavenport.electrontest.internal.jsdeps.electron.mod.app
import io.chrisdavenport.electrontest.internal.jsdeps.electron
import io.chrisdavenport.electrontest.internal.jsdeps.node

import electron.mod.BrowserWindow
import cats.effect.unsafe.IORuntime.global


object Main extends IOApp  {

  def createWindow() = IO.defer{
    val window = new BrowserWindow(
      electron.Electron.BrowserWindowConstructorOptions()
        .setMaxHeight(600)
        .setMaxWidth(800)
        .setWebPreferences(
          electron.Electron.WebPreferences()
            .setPreload(node.pathMod.join("__dirname", "preload.js"))
        )
    )
    IO.fromPromise(IO(window.loadFile("index.html"))).as(window)
  }

  def run(args: List[String]): IO[ExitCode] = {
    for {
      _ <- IO(app.on("window-all-closed", {_: Any => IO(app.exit()).unsafeRunAndForget()(global)}))
      _ <- IO.fromPromise(IO(app.whenReady()))
      window <- createWindow()
    } yield ExitCode.Success
  }

}
