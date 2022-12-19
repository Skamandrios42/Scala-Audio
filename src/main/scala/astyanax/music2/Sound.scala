package astyanax.music2

import scala.annotation.targetName

object Sound extends Composable[Sound] {
    def impl(elems: Seq[(Sound, Double)]): Sound = CombinedSound(elems)

    def from(f: Double => Double) = new Sound {
        def apply(t: Double) = f(t)
    }
}

trait Sound extends (Double => Double) {
    def apply(t: Double): Double

    def play(duration: Double, msg: String = "")(using player: Player): Unit =    
        player.play(Array.tabulate((duration * player.sampleRate).toInt)(t => apply(t / player.sampleRate)))
    
    def scaleAmplitude(envelope: Envelope) = EnvelopedSound(this, envelope, true)
    def scaleFrequency(envelope: Envelope) = EnvelopedSound(this, envelope, false)
    def <*>(envelope: Envelope) = scaleAmplitude(envelope)
}

case class Noise(amplitude: Double) extends Sound {
    def apply(t: Double): Double = amplitude * (math.random() * 2 - 1)
}

case class CombinedSound(sounds: Seq[(Sound, Double)]) extends Sound {
    def apply(t: Double): Double = sounds.foldLeft(0.0) { case (sum, (sound, amp)) => sum + amp * sound(t) }
}

case class EnvelopedSound(sound: Sound, envelope: Envelope, amplitude: Boolean) extends Sound {
    def apply(t: Double): Double = 
        if amplitude then envelope(t) * sound(t)
        else sound(envelope(t) * t)
}

trait Wave extends Sound {
    def frequency: Double
    def amplitude: Double
    def copy(frequency: Double = frequency, amplitude: Double = amplitude): Wave
}

object Sine extends ((Double, Double) => Sine)
object Square extends ((Double, Double) => Square)
object Saw extends ((Double, Double) => Saw)
object Triangle extends ((Double, Double) => Triangle)

case class Sine(frequency: Double, amplitude: Double = 1.0) extends Wave {
    def apply(t: Double) = amplitude * math.sin(2 * math.Pi * t * frequency)
}

case class Square(frequency: Double, amplitude: Double = 1.0) extends Wave {
    def apply(t: Double) = amplitude * math.sin(2 * math.Pi * t * frequency).sign
}

case class Saw(frequency: Double, amplitude: Double = 1.0) extends Wave {
    def apply(t: Double) = amplitude * 2 * (t * frequency - math.floor(0.5 + t * frequency))
}

case class Triangle(frequency: Double, amplitude: Double = 1.0) extends Wave {
    def apply(t: Double) = amplitude * 2 * math.abs(t * frequency - math.floor(0.5 + t * frequency))
}
