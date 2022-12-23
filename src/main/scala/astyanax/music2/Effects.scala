package astyanax.music2

object Effects {

    def harmonize(voices: Int, f: Double, wave: (Double, Double) => Wave = Sine.apply) =
        Seq.tabulate(voices)(i => wave((i + 1) * f, 1.0) -> 1.0 / voices)
    def unison(voices: Int, f: Double, wave: (Double, Double) => Wave = Sine.apply) =
        Seq.fill(voices)(wave(f, 1.0) -> 1.0 / voices)

    extension (self: (Seq[(Wave, Double)])) {
        def detune(range: Double): Seq[(Wave, Double)] =
            self.map { (x, a) => 
                val diff = (math.random() * 2 * range) - range
                (x.copy(frequency = x.frequency + diff), a)
            }
    }

    def delay(sound: Sound, time: Double, amount: Int, decay: Int => Double) = 
        Seq.tabulate(amount + 1)(i => (Sound.from(t => sound(t - i * time)) -> decay(i))).combine

    def delay(synth: Synth, time: Double, amount: Int, decay: Int => Double) = 
        val samples = synth.samples
        val res = new Array[Double]((samples.length + amount * time * synth.rate).toInt)
        for i <- 0 to amount do
            val offset = (i * time * synth.rate).toInt
            for j <- samples.indices do
                res(offset + j) += decay(i) * samples(j)
        ArraySynth(res, synth.rate)
}
