package astyanax.music2

import math.{pow, sqrt, exp}
import astyanax.music2.Effects.detune
import astyanax.music2.Track.{on, repeat}
import astyanax.utils.Benchmark

@main def demo1 = Music {
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
