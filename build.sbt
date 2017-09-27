lazy val commonSettings = Seq(
  version := "0.1",
  scalaVersion := "2.12.3",
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.1" % Test
  )
)

lazy val root = (project in file("./"))
  .settings(commonSettings: _*)
  .settings(
    name := "Scala Todo"
  )
  .aggregate(core, rest)

lazy val core = (project in file("./scala-todo-core"))
  .settings(commonSettings: _*)
  .settings(
    name := "Scala Todo Core",
    libraryDependencies ++= Seq(
      "com.typesafe.slick" %% "slick" % "3.2.1",
      "org.slf4j" % "slf4j-nop" % "1.7.10",
      "com.h2database" % "h2" % "1.4.194"
    )
  )

lazy val rest = (project in file("./scala-todo-rest-api"))
  .settings(commonSettings: _*)
  .settings(
    name := "Scala Todo REST API",
    libraryDependencies ++= Seq(
      guice,
      "org.scalatestplus.play" %% "scalatestplus-play" % "3.0.0" % Test
    )
  )
  .dependsOn(core)
  .enablePlugins(PlayScala)
