package io.chrisdavenport.electrontest.render

import cats.effect._
import cats.effect.unsafe.IORuntime.global

/*
// This file is required by the index.html file and will
// be executed in the renderer process for that window.
// No Node.js APIs are available in this process because
// `nodeIntegration` is turned off. Use `preload.js` to
// selectively enable features needed in the rendering
// process.
*/
object Main extends IOApp  {

  // def createWindow() = IO.defer{
  //   val window = new BrowserWindow(
  //     electron.Electron.BrowserWindowConstructorOptions()
  //       .setMaxHeight(600)
  //       .setMaxWidth(800)
  //   )
  //   IO.fromPromise(IO(window.loadFile("index.html"))).as(window)
  // }

  def run(args: List[String]): IO[ExitCode] = {
    for {
      // _ <- IO(app.on("window-all-closed", {_: Any => IO(app.exit()).unsafeRunAndForget()(global)}))
      // _ <- IO.fromPromise(IO(app.whenReady()))
      // window <- createWindow()
      _ <- IO(println("Renderer Test"))
    } yield ExitCode.Success
  }

}
