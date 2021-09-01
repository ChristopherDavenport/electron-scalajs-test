import scala.concurrent.Future
import org.scalajs.jsenv.{Input, JSRun, RunConfig}
import org.scalajs.jsenv.{Input, JSComRun, RunConfig}
import org.scalajs.jsenv.JSEnv
import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}

val Scala213 = "2.13.6"

ThisBuild / crossScalaVersions := Seq(Scala213)
ThisBuild / scalaVersion := Scala213

ThisBuild / testFrameworks += new TestFramework("munit.Framework")

val catsV = "2.6.1"
val catsEffectV = "3.1.1"
val fs2V = "3.1.1"
val http4sV = "1.0.0-M24"
val circeV = "0.14.1"
val doobieV = "1.0.0-M5"
val munitCatsEffectV = "1.0.5"


// Projects
lazy val `electron-test` = project.in(file("."))
  .disablePlugins(MimaPlugin)
  .enablePlugins(NoPublishPlugin)
  .aggregate(node, main, render, preload)

lazy val node = project
  .in(file("electron/node"))
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalablyTypedConverterGenSourcePlugin)
  .settings(
    name := "electron-test-node",
    mimaPreviousArtifacts := Set.empty,
    scalacOptions += "-nowarn",
    Compile / doc / sources := Nil,
    scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)),
    Compile / npmDependencies += "@types/node" -> "14.17.5",
    Compile / npmDependencies += "electron" -> "13.2.3",

    useYarn := true,
    // yarnExtraArgs += "--frozen-lockfile",
    stOutputPackage := "io.chrisdavenport.electrontest.internal.jsdeps",
    stStdlib := List("es2020")
  )


import org.scalajs.jsenv._
lazy val main = project.in(file("electron/main"))
  .enablePlugins(NpmPackagePlugin)
  .dependsOn(node)
  .settings(commonSettings)
  .settings(
    name := "electron-test",
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)),
    jsEnv := new org.scalajs.jsenv.nodejs.ElectronJSEnv(),
    npmPackageOutputDirectory := file("output")
  )

lazy val preload = project.in(file("electron/preload"))
  .enablePlugins(NpmPackagePlugin)
  .dependsOn(node)
  .settings(commonSettings)
  .settings(
    name := "electron-test-preload",
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)),
    npmPackageOutputDirectory := file("output"),
    npmPackageOutputFilename := "preload.js",
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "1.1.0"
  )


lazy val render = project.in(file("electron/renderer"))
  .enablePlugins(NpmPackagePlugin)
  .settings(commonSettings)
  .settings(
    name := "electron-test-render",
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)),
    jsEnv := new org.scalajs.jsenv.nodejs.ElectronJSEnv(),
    npmPackageOutputDirectory := file("output"),
    npmPackageOutputFilename := "renderer.js",
    libraryDependencies += "org.scala-js" %%% "scalajs-dom" % "1.1.0"
  )


addCommandAlias("electronOutput", ";render/npmPackageOutputJS ; preload/npmPackageOutputJS ; main/npmPackage")

// General Settings
lazy val commonSettings = Seq(

  libraryDependencies ++= Seq(
    "org.typelevel"               %%% "cats-core"                  % catsV,
    "org.typelevel"               %%% "cats-effect"                % catsEffectV,

    "co.fs2"                      %%% "fs2-core"                   % fs2V,
    "co.fs2"                      %%% "fs2-io"                     % fs2V,

    // "org.http4s"                  %%% "http4s-dsl"                 % http4sV,
    // "org.http4s"                  %%% "http4s-ember-server"        % http4sV,
    // "org.http4s"                  %%% "http4s-ember-client"        % http4sV,
    // "org.http4s"                  %%% "http4s-circe"               % http4sV,

    // "io.circe"                    %%% "circe-core"                 % circeV,
    // "io.circe"                    %%% "circe-generic"              % circeV,
    // "io.circe"                    %%% "circe-parser"               % circeV,

    // "org.tpolecat"                %% "doobie-core"                % doobieV,
    // "org.tpolecat"                %% "doobie-h2"                  % doobieV,
    // "org.tpolecat"                %% "doobie-hikari"              % doobieV,
    // "org.tpolecat"                %% "doobie-postgres"            % doobieV,

    "org.typelevel"               %%% "munit-cats-effect-3"        % munitCatsEffectV         % Test,

  )
)