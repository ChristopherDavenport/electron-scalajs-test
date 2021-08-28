package io.chrisdavenport.electrontest

import cats.effect._
// import io.chrisdavenport.electrontest.internal.jsdeps.node._
import io.chrisdavenport.electrontest.internal.jsdeps.electron.mod.{BrowserWindow, NodeEventEmitter}

import io.chrisdavenport.electrontest.internal.jsdeps.electron.electronRequire
import io.chrisdavenport.electrontest.internal.jsdeps.electron.mod.app
import io.chrisdavenport.electrontest.internal.jsdeps.electron

import electron.mod.BrowserWindow

// import fs2.internal.jsdeps.node.eventsMod
// import cats.effect.unsafe.IORuntime.global


object Main  {

  def createWindow() = {
    val window = new BrowserWindow(
      electron.Electron.BrowserWindowConstructorOptions()
        .setMaxHeight(600)
        .setMaxWidth(800)
    )
    // window.loadFile("index.html")
  }

  def main(args: Array[String]): Unit = {
    app.on("ready", {_: Any => createWindow(); ()})
    app.on("window-all-closed", {_: Any => app.quit()})
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
