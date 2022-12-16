val root = project in file(".") settings (
    name := "Scala Audio",
    version := "0.0.0",
    scalaVersion := "3.2.1",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.14" % "test",
    scalacOptions ++= Seq("-feature", "-deprecation"),
)