// TODO low pass / high pass
// TODO envelope / Synth API
// TODO better Track generation
// TODO more effects
// TODO tuning
val root = project in file(".") settings (
    name := "Scala Audio",
    version := "0.0.0",
    scalaVersion := "3.2.1",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.14" % "test",
    scalacOptions ++= Seq("-feature", "-deprecation"),
)

/*

Sound -> Waves + Envelope(amp) + Envelop(freq) + Effects + Filters

*/