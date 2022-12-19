package astyanax.music

import scala.annotation.targetName

case class Envelope(
    attack: Double => Double, 
    sustain: Double => Double, 
    release: Double => Double,
) {
    def apply(sound: Sound, timing: (Double, Double, Double)): Synth = Synth(
        new Sound {
            def apply(t: Double): Double = 
                if t < timing(0) then attack(t) * sound(t)
                else if t < timing(1) then sustain(t) * sound(t)
                else release(t) * sound(t)
        }, timing(2)
    )
}

object Synth {
    def from(sound: Sound, env: Double => Double, stop: Double) = Synth(sound *> env, stop)
    def from(sound: Sound, env: Envelope, timing: (Double, Double, Double)) = env(sound, timing)
    def from(sound: Sound, stop: Double) = Synth(sound, stop)
}

case class Synth(sound: Sound, length: Double, filters: Seq[Filter] = Seq.empty) extends Sound {
    def apply(t: Double): Double = if t < length then sound(t) else 0
    def samples(using player: Player) =
        val samples = Array.tabulate((length * player.sampleRate).toInt)(t => sound(t / player.sampleRate))
        //val samples = new Array[Double]((length * player.sampleRate).toInt)
        //for t <- samples.indices do samples(t) = sound(t / player.sampleRate)
        //Array.range(0, (length * player.sampleRate).toInt).map(t => sound(t / player.sampleRate))
        filters.foldLeft(samples) {
            case (smpls, filter) => filter.useOn(smpls)
        }
    def filter(filter: Filter) = copy(filters = filters :+ filter)
}

trait Filter {
    def useOn(samples: Array[Double]): Array[Double]
}

case class FIRFilter(coefficients: Seq[Double]) extends Filter {
    def useOn(samples: Array[Double]): Array[Double] = 
        val res = new Array[Double](samples.length)
        for i <- coefficients.length until samples.length do
            res(i) = samples.slice(i - coefficients.length, i).zip(coefficients).map(_ * _).sum
        System.arraycopy(samples, 0, res, 0, coefficients.length)
        res


}

trait PassFilter extends Filter {
    def filter(x: Double, x1: Double): Double
    def useOn(samples: Array[Double]) =
        val it = samples.iterator
        it.next()
        (Iterator(samples(0)) ++ (it zip samples.iterator).map(filter)).toArray
}

case class LowPass(a: Double) extends PassFilter {
    def filter(x: Double, x1: Double) = a * x + (1 - a) * x1
}

case class HighPass(a: Double) extends PassFilter {
    def filter(x: Double, x1: Double) = a * x - (1 - a) * x1
}
