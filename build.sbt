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
  .aggregate(core)

lazy val node = project
  .in(file("node"))
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalablyTypedConverterGenSourcePlugin)
  .settings(
    name := "electron-test-node",
    mimaPreviousArtifacts := Set.empty,
    scalacOptions += "-nowarn",
    Compile / doc / sources := Nil,
    scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)),
    Compile / npmDependencies += "@types/node" -> "16.0.0",
    Compile / npmDependencies += "electron" -> "13.2.2",

    useYarn := true,
    // yarnExtraArgs += "--frozen-lockfile",
    stOutputPackage := "io.chrisdavenport.electrontest.internal.jsdeps",
    stStdlib := List("es2020")
  )

lazy val core = project.in(file("core"))
  .enablePlugins(ScalaJSPlugin)
  .enablePlugins(ScalaJSBundlerPlugin)
  .dependsOn(node)
  .settings(commonSettings)
  .settings(
    name := "electron-test",
    scalaJSUseMainModuleInitializer := true,
    scalaJSLinkerConfig ~= (_.withModuleKind(ModuleKind.CommonJSModule)),
  )

// lazy val site = project.in(file("site"))
//   .disablePlugins(MimaPlugin)
//   .enablePlugins(DavenverseMicrositePlugin)
//   .settings(commonSettings)
//   .dependsOn(core)
//   .settings{
//     import microsites._
//     Seq(
//       micrositeDescription := "Electron Test App",
//     )
//   }

// General Settings
lazy val commonSettings = Seq(

  libraryDependencies ++= Seq(
    "org.typelevel"               %%% "cats-core"                  % catsV,
    "org.typelevel"               %%% "cats-effect"                % catsEffectV,

    "co.fs2"                      %%% "fs2-core"                   % fs2V,
    "co.fs2"                      %%% "fs2-io"                     % fs2V,

    "org.http4s"                  %%% "http4s-dsl"                 % http4sV,
    "org.http4s"                  %%% "http4s-ember-server"        % http4sV,
    "org.http4s"                  %%% "http4s-ember-client"        % http4sV,
    "org.http4s"                  %%% "http4s-circe"               % http4sV,

    "io.circe"                    %%% "circe-core"                 % circeV,
    "io.circe"                    %%% "circe-generic"              % circeV,
    "io.circe"                    %%% "circe-parser"               % circeV,

    // "org.tpolecat"                %% "doobie-core"                % doobieV,
    // "org.tpolecat"                %% "doobie-h2"                  % doobieV,
    // "org.tpolecat"                %% "doobie-hikari"              % doobieV,
    // "org.tpolecat"                %% "doobie-postgres"            % doobieV,

    "org.typelevel"               %%% "munit-cats-effect-3"        % munitCatsEffectV         % Test,

  )
)