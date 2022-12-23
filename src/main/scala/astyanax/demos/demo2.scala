package astyanax.demos

import math.{pow, sqrt, exp}
import astyanax.music2.{Music, Notes, Timer, Filter, Synth, Sound, Noise, Effects, Envelope, Saw, Sine, Triangle}
import astyanax.music2.Track.{on, repeat}
import astyanax.music2.Effects.detune
import astyanax.utils.Benchmark

@main def demo2 = Music {
    val notes = Notes(440)
    val t = Timer(0.2)

    def key(note: String, i: Int, speed: Int, length: Double, f: Double => Double = _ => 1.0): Synth =
        val noise  = Filter(Synth(Noise(0.2) <*> Envelope(t => -8 * t + 1), 0.5), Seq.fill(10)(0.1))
        val env = Envelope(t => f(t) min (-t + length))
        val base0 = Synth(Effects.unison(4, notes.get(note, i-1), Saw).detune(2).combine <*> env, length)
        val base1 = Synth(Effects.harmonize(1, notes.get(note, i-1), Sine).combine <*> env, length)
        val base2 = Synth(Effects.harmonize(1, notes.get(note, i+1), Triangle).combine <*> env, length)
        val tone = Filter.timed(Synth.sum(base0 -> 0.3, base1 -> 0.4, base2 -> 0.3), t =>
                val drop = speed * t*t
                Seq.fill(drop.toInt)(1.0 / drop)
            )
        Synth.sum(tone -> 0.6, noise -> 0.1)

    def bass(note: String, i: Int, speed: Int, length: Double, f: Double => Double = _ => 1.0, withDelay: Boolean = true): Synth =
        val noise0 = Filter(Synth(Noise(0.2) <*> Envelope(t => -8 * t + 1), 0.5), Seq.fill(5)(0.2))
        val noise1 = Filter(Synth(Noise(0.2) <*> Envelope(t => -3 * t + 1), 0.5), Seq.fill(50)(0.02))
        val noise = noise0 ++ noise1

        val env = Envelope(t => f(t) min (-10 * t + 3 * length))
        val env2 = Envelope(t => f(t) min (-10 * t + 4 * length))

        val base0 =
            Filter(Synth(Effects.unison(3, notes.get(note, i), Saw).detune(0.5).combine <*> env, length), Seq.fill(12)(1.0 / 12))
        val base1 = 
            Filter.timed(Synth(Effects.harmonize(5, notes.get(note, i-1), Sine).combine <*> env, length), t =>
                val drop = speed * t*t
                Seq.fill(drop.toInt)(1.0 / drop)
            )
        val base2 = Synth(Effects.harmonize(4, notes.get(note, i), Triangle).combine <*> env2, length)
        val tone = Effects.delay(Synth.sum(base0 -> 0.7, base1 -> 0.3, base2 -> 0.3), .01, 2, t => math.pow(0.5, t + 1))
        if withDelay then Effects.delay(Synth.sum(tone -> 0.9, noise -> 0.1), 0.4, 1, math.pow(0.8, _))
        else Effects.delay(Synth.sum(tone -> 0.9, noise -> 0.1), 0.4, 0, math.pow(0.8, _))

    // INTRO A

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

    key("f", -2, 100, 2) on t.next(2)
    key("c", -1, 100, 2) on t.next(2)
    key("d", -1, 100, 2) on t.next(2)
    key("d", -1, 100, 2, x => (0.15 / t.step) * x) on t.next(2)

    repeat(2) {
        repeat(3) {
            key("d", -2, 100, 1) on t.next
            key("d", -1, 100, 1) on t.next
            key("d", -1, 100, 1) on t.next
            key("f", -1, 100, 1) on t.next
            key("d", -1, 100, 1) on t.next
            key("d", -1, 100, 1) on t.next
            key("d", -1, 100, 1) on t.next
        }
        key("f", -2, 100, 2) on t.next(2)
        key("c", -1, 100, 2) on t.next(2)
        key("d", -1, 100, 2) on t.next(2)
        key("d", -1, 100, 2, x => (0.15 / t.step) * x) on t.next(2)
    }

    repeat(2) {
        key("b", -1, 100, 1) on t.next
        key("f", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("a",  0, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("f", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
    }
    repeat(2) {
        key("a", -1, 100, 1) on t.next
        key("e", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("g", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("e", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
    }
    repeat(2) {
        key("c", -1, 100, 1) on t.next
        key("g", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("d",  0, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("g", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
    }

    // INTRO B

    repeat(3) {
        key("a", -1, 100, 1) on t.next
        key("e", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        
        key("f", -2, 100, 1) on t.next
        key("c", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        
        key("g", -2, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next

        key("d", -2, 100, 1) on t.next
        key("a", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next

        key("f", -2, 100, 1) on t.next
        key("d", -2, 100, 1) on t.next
    }

    key("a", -1, 100, 1) on t.next
    key("e", -1, 100, 1) on t.next
    key("d", -1, 100, 1) on t.next
    
    key("f", -2, 100, 1) on t.next
    key("c", -1, 100, 1) on t.next
    key("d", -1, 100, 1) on t.next
    
    key("g", -2, 100, 1) on t.next
    key("d", -1, 100, 1) on t.next
    key("d", -1, 100, 1) on t.next

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
        key("f", -2, 100, 1) on t.next
        key("c", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("e", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
        key("c", -1, 100, 1) on t.next
        key("d", -1, 100, 1) on t.next
    }
    key("f", -2, 100, 1) on t.next
    key("c", -1, 100, 1) on t.next
    key("d", -1, 100, 1) on t.next
    key("e", -1, 100, 1) on t.next
    key("d", -1, 100, 1) on t.next
    key("c", -1, 100, 2) on t.now
    key("g", -1, 100, 1, x => (0.15 / t.step) * x) on t.next(2)

    // VERSE
    t.wait(7.0)

    bass("d", -3, 100, 1) on t.next(3)
    bass("d", -3, 100, 1) on t.next(4)
    bass("a", -2, 100, 1) on t.now
    bass("d", -3, 100, 1) on t.next(3)
    bass("d", -3, 100, 1) on t.next(4)

    bass("d", -3, 100, 1) on t.next(3)
    bass("d", -3, 100, 1) on t.next(4)
    bass("a", -2, 100, 1) on t.now
    bass("d", -3, 100, 1) on t.next(3)
    bass("d", -3, 100, 1) on t.next(2)
    bass("d", -3, 100, 1, withDelay = false) on t.next
    bass("f", -3, 100, 1, withDelay = false) on t.next


    bass("f", -3, 100, 1) on t.next(3)
    bass("f", -3, 100, 1) on t.next(4)
    bass("a", -2, 100, 1) on t.now
    bass("f", -3, 100, 1) on t.next(3)
    bass("f", -3, 100, 1) on t.next(4)

    bass("f", -3, 100, 1) on t.next(3)
    bass("f", -3, 100, 1) on t.next(4)
    bass("a", -2, 100, 1) on t.now
    bass("f", -3, 100, 1) on t.next(3)
    bass("f", -3, 100, 1) on t.next(2)
    bass("f", -3, 100, 1, withDelay = false) on t.next
    bass("e", -3, 100, 1, withDelay = false) on t.next


    bass("d", -3, 100, 1) on t.next(3)
    bass("d", -3, 100, 1) on t.next(4)
    bass("a", -2, 100, 1) on t.now
    bass("d", -3, 100, 1) on t.next(3)
    bass("d", -3, 100, 1) on t.next(4)

    bass("d", -3, 100, 1) on t.next(3)
    bass("d", -3, 100, 1) on t.next(4)
    bass("a", -2, 100, 1) on t.now
    bass("d", -3, 100, 1) on t.next(3)
    bass("d", -3, 100, 1) on t.next(2)
    bass("d", -3, 100, 1, withDelay = false) on t.next
    bass("f", -3, 100, 1, withDelay = false) on t.next


    bass("f", -3, 100, 1) on t.next(3)
    bass("f", -3, 100, 1) on t.next(2)
    bass("f", -3, 100, 1, withDelay = false) on t.next
    bass("d", -3, 100, 1, withDelay = false) on t.next
    bass("a", -2, 100, 1) on t.now
    bass("f", -3, 100, 1) on t.next(3)
    bass("f", -3, 100, 1) on t.next(4)

    bass("f", -3, 100, 1) on t.next(3)
    bass("f", -3, 100, 1) on t.next(4)
    bass("a", -2, 100, 1) on t.now
    bass("f", -3, 100, 1) on t.next(3)
    bass("e", -3, 100, 1) on t.next(4)

    bass("d", -3, 100, 1, withDelay = false) on t.next(7)

}

