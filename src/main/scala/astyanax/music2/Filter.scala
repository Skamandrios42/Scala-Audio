package astyanax.music2

object Filter {
    def apply(synth: Synth, coefficients: Seq[Double]) =
        val samples = synth.samples
        val res = new Array[Double](samples.length)
        for i <- coefficients.length until samples.length do
            res(i) = samples.slice(i - coefficients.length, i).zip(coefficients).map(_ * _).sum
        System.arraycopy(samples, 0, res, 0, coefficients.length)
        ArraySynth(res, synth.rate)
}
