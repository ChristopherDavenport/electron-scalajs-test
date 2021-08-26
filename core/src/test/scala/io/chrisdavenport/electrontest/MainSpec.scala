package io.chrisdavenport.electrontest

import munit.CatsEffectSuite
import cats.effect._

class MainSpec extends CatsEffectSuite {

  test("Main should exit succesfully") {
    Main.run(List.empty[String]).map(ec =>
      assertEquals(ec, ExitCode.Success)
    )
  }

}
