package astyanax.music2

import math.{pow, sqrt, exp}
import astyanax.music2.Effects.detune
import astyanax.music2.Track.{on, repeat}
import astyanax.utils.Benchmark

@main def awake = Music {
    val notes = Notes(440)
    val t = Timer(0.2)

    def key(note: String, i: Int) =
        // sound elements
        val noise  = Filter(Synth(Noise(0.2) <*> Envelope(t => -8 * t + 1), 0.5), Seq.fill(100)(0.01))
        val start  = Effects.unison(10, notes.get(note, i + 1), Sine).detune(5).combine
        val end    = Effects.harmonize(3, notes.get(note, i), Triangle).detune(1).combine
        val chorus = Effects.unison(6, notes.get(note, i), Square).detune(1).combine
        // combined sound
        val sound = Sound.sum(
            (start  <*> Envelope(t => pow(2, -8 * t)))                            -> 0.4,
            (end    <*> Envelope(t => 0.6 - pow(2, -8 * t) min (pow(3, -2 * t)))) -> 0.35,
            (chorus <*> Envelope(t => 0.6 - pow(2, -8 * t) min (pow(3, -2 * t)))) -> 0.05,
        )
        // combined synth
        Effects.delay(Synth.sum(Synth(sound, 4) -> 1.0, noise -> 0.2), 0.4, 8, pow(0.5, _))

    def bass(note: String, i: Int, length: Double, amp: Double) =
        val main = 
            (Effects.harmonize(10, notes.get(note, i), Sine).detune(3) ++
             Effects.unison(3, notes.get(note, i), Triangle).detune(3)
            ).combine
        val env  = Envelope(t => 
            if t < sqrt(amp) then amp - (t - sqrt(amp)) * (t - sqrt(amp)) 
            else amp min (-amp * (t - length)))
        val syn  = Synth(main <*> env, length)
        syn


    bass("a", -3, t.step * 12, 0.3) on t.now
    key("a", 0) on t.next
    key("a", 0) on t.next
    key("as", 0) on t.next
    key("a", 0) on t.next
    key("c", 0) on t.next
    t.wait(7.0)
    bass("d", -4, t.step * 16, 0.3) on t.now
    key("g", -1) on t.next
    key("g", -1) on t.next
    key("d", -1) on t.next
    key("d", 0) on t.next
    key("a", -1) on t.next
    key("a", 0) on t.next
    key("as", 0) on t.next
    key("a", 0) on t.next
    key("c", 0) on t.next
}

@main def invincible_old = Music {
    val notes = Notes(440)
    val t = Timer(0.2)

    def key(note: String, i: Int, speed: Int, length: Double, f: Double => Double = _ => 1.0): Synth =
        val noise  = Filter(Synth(Noise(0.2) <*> Envelope(t => -8 * t + 1), 0.5), Seq.fill(10)(0.1))
        def coefficients(t: Double) = Seq.fill((t * 10).toInt)(10 / (t * 10))
        val env = Envelope(t => f(t) min (-t + length))
        val base0 = Synth(Effects.unison(4, notes.get(note, i-1), Saw).detune(2).combine <*> env, length)
        val base1 = Synth(Effects.harmonize(1, notes.get(note, i-1), Sine).combine <*> env, length)
        val base2 = Synth(Effects.harmonize(1, notes.get(note, i+1), Triangle).combine <*> env, length)
        val tone = Filter.timed(Synth.sum(base0 -> 0.3, base1 -> 0.4, base2 -> 0.3), t =>
                val drop = speed * t*t
                Seq.fill(drop.toInt)(1.0 / drop)
            )
        Synth.sum(tone -> 0.8, noise -> 0.2)

    def slide(note1: String, i1: Int, note2: String, i2: Int, speed: Int, length: Double): Synth =
        val f1 = notes.get(note1, i1)
        val f2 = notes.get(note2, i2)
        val ratio = f2 / f1

        val freqEnv = Envelope(t => ratio - exp(-1 * t) * (ratio - 1), lower = 1.0, upper = ratio)

        val noise  = Filter(Synth(Noise(0.2) <*> Envelope(t => -8 * t + 1), 0.5), Seq.fill(10)(0.1))
        def coefficients(t: Double) = Seq.fill((t * 10).toInt)(10 / (t * 10))
        val env = Envelope(t => 1.0 min (-t + length))
        val base0 = Synth((Effects.unison(4, f1 / 2, Saw).detune(2).combine <*> env).scaleFrequency(freqEnv), length)
        val base1 = Synth((Effects.harmonize(1, f1 / 2, Sine).combine <*> env).scaleFrequency(freqEnv), length)
        val base2 = Synth((Effects.harmonize(1, f1 * 2, Triangle).combine <*> env).scaleFrequency(freqEnv), length)
        val tone = Filter.timed(Synth.sum(base0 -> 0.3, base1 -> 0.4, base2 -> 0.3), t =>
                val drop = speed * t*t
                Seq.fill(drop.toInt)(1.0 / drop)
            )
        Synth.sum(tone -> 0.8, noise -> 0.2)

    //slide("a", -1, "c", -1, 100, 2) on t.next(8)

    // val ratio = 2.0
    // val length = 1.0
    // val k = 1.5
    // val freqEnv = Envelope(t => ratio - exp(-1 * t) * (ratio - 1), lower = 1.0, upper = ratio)//Envelope(t => ((ratio-1) / length) * t + 1, lower = 1.0, upper = ratio)
        //Envelope(t => ratio / (1 + exp(-k * ratio * t) * (ratio / 1 - 1)), lower = 1.0, upper = ratio)
        //Envelope(t => ratio - exp(-2 * t) * (ratio - 1))

    // val sound = new Sound {
    //     def apply(t: Double): Double = freqEnv.f(t) * Sine(440, 0.3)(t)
    // }
    
    //Synth(Sine(440, 0.3).scaleFrequency(freqEnv), 5) on t.next
    //t.wait(50.0)

    repeat(4) {
        key("d", -2, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("f", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
    }

    repeat(3) {
        key("fis", -2, 100, 1) on t.next
        key("cis", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("e", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("cis", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
    }
    key("fis", -2, 100, 1) on t.next
    key("cis", -1, 100, 1) on t.next
    key("d", -1, 100, 1) on t.next
    key("e", -1, 100, 1) on t.next
    key("d", -1, 100, 1) on t.next
    key("cis", -1, 100, 2) on t.now
    key("gis", -1, 100, 1, x => (0.15 / t.step) * x) on t.next(2)

    repeat(4) {
        key("cis", -1, 100, 1) on t.next
        key("gis", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("h", 0, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("gis", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
    }

    key("fis", -2, 100, 1) on t.next
    key("cis", -1, 100, 1) on t.next
    key("d", -1, 100, 1) on t.next
    key("e", -1, 100, 1) on t.next
    key("d", -1, 100, 1) on t.next
    key("cis", -1, 100, 1) on t.next
    key("d", -1, 100, 1) on t.next
    
    key("fis", -2, 100, 1) on t.next
    key("cis", -1, 100, 1) on t.next
    key("d", -1, 100, 1) on t.next
    key("e", -1, 100, 1) on t.next
    key("d", -1, 100, 1) on t.next
    key("cis", -1, 100, 2) on t.now
    key("gis", -1, 100, 1, x => (0.15 / t.step) * x) on t.next(2)

    repeat(2) {
        key("cis", -1, 100, 1) on t.next
        key("gis", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("h", 0, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("gis", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
    }

    repeat(2) {
        key("dis", -2, 100, 1) on t.next
        key("c", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("dis", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("c", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
    }
    repeat(3) {
        key("g", -2, 100, 1) on t.next
        key("f", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("g", -1, 100, 1) on t.next
        key("dis", -2, 100, 1) on t.next
        key("c", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
    }

    key("g", -2, 100, 1) on t.next
    key("f", -1, 100, 1) on t.next
    key("d", -1, 100, 1) on t.next
    key("g", -1, 100, 1) on t.next

    key("f", -2, 100, 1) on t.next(2)
    key("c", -1, 100, 1) on t.next(2)
    key("d", -1, 100, 1) on t.next(2)
    key("d", -1, 100, 1, x => (0.15 / t.step) * x) on t.next(2)

    repeat(4) {
        key("d", -2, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("f", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
    }

}












    // key("gis", -1, 100, 1) on t.next
    //slide("cis", -1, "gis", -1, 100, 2) on t.next(2)
