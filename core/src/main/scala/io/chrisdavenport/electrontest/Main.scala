package io.chrisdavenport.electrontest

import cats.effect._
// import io.chrisdavenport.electrontest.internal.jsdeps.node._
import io.chrisdavenport.electrontest.internal.jsdeps.electron.mod.{BrowserWindow, NodeEventEmitter}
import fs2.internal.jsdeps.node.eventsMod

object Main extends IOApp {

  def run(args: List[String]): IO[ExitCode] = {
    for {
      app <- IO(NodeEventEmitter)
      window <- IO(BrowserWindow)
      _ <- app.on()
    } yield ExitCode.Success
  }

}