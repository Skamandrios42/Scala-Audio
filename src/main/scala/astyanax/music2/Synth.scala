package astyanax.music2

object Synth extends Composable[Synth] {
    def impl(elems: Seq[(Synth, Double)]): Synth = 
        elems.head
        val len = elems.map(_(0).samples.length).max
        val rate = elems.head(0).rate
        val array = new Array[Double](len)
        for (synth, amp) <- elems do
            require(rate == synth.rate)
            val samples = synth.samples
            for i <- samples.indices do
                array(i) += samples(i) * amp
        ArraySynth(array, rate)
    
    def apply(sound: Sound, length: Double)(using Player) = SoundSynth(sound, length, Player.rate)
}

trait Synth {
    def rate: Float
    def length: Double
    def samples: Array[Double]
}

case class SoundSynth(sound: Sound, length: Double, rate: Float) extends Synth {
    lazy val samples =
        Array.tabulate((length * rate).toInt)(t => sound(t / rate))
}

case class ArraySynth(samples: Array[Double], rate: Float) extends Synth {
    def length = samples.length / rate
}