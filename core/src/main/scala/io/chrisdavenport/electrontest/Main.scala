package io.chrisdavenport.electrontest

import cats.effect._
// import io.chrisdavenport.electrontest.internal.jsdeps.node._
import io.chrisdavenport.electrontest.internal.jsdeps.electron.mod.{BrowserWindow, NodeEventEmitter}

import io.chrisdavenport.electrontest.internal.jsdeps.electron.electronRequire
import io.chrisdavenport.electrontest.internal.jsdeps.electron.mod.app
import io.chrisdavenport.electrontest.internal.jsdeps.electron

import electron.mod.BrowserWindow
import cats.effect.unsafe.IORuntime.global


object Main extends IOApp  {

  def createWindow() = IO.defer{
    val window = new BrowserWindow(
      electron.Electron.BrowserWindowConstructorOptions()
        .setMaxHeight(600)
        .setMaxWidth(800)
    )
    IO.fromPromise(IO(window.loadFile("index.html")))
  }

  def run(args: List[String]): IO[ExitCode] = {
    for {
      _ <- IO(app.on("window-all-closed", {_: Any => IO(app.exit()).unsafeRunAndForget()(global)}))
      _ <- IO.fromPromise(IO(app.whenReady()))
      _ <- createWindow()
    
    } yield ExitCode.Success
  }

}
