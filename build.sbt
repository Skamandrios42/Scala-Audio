// TODO low pass / high pass
// TODO envelope / Synth API
// TODO better Track generation
// TODO more effects (chorus, reverb, distortion, overdrive)
// TODO tunings (more tunings systems)
val root = project in file(".") settings (
    name := "Scala Audio",
    version := "0.0.0",
    scalaVersion := "3.2.1",
    libraryDependencies += "org.scalatest" %% "scalatest" % "3.2.14" % "test",
    scalacOptions ++= Seq("-feature", "-deprecation"),
)

/*

## Sound -> Synth -> Track

- Sound: Doule => Double, combination of waves, noise etc., envelopes
    - composable
    - effects (harmonics, unison, delay)
    - envelopes
    - no obvious finite length
- Synth: Seq[Double]
    - composable
    - effects (delay)
    - envelopes
    - filters
    - obvious finite length
- Track -- timed combination of synths

*/