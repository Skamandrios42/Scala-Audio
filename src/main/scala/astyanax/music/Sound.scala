package astyanax.music

object Sound {
    def sum(components: (Sound, Double)*) = new Sound {
        def apply(t: Double): Double = components.map((tone, weight) => tone(t) * weight).sum
    }
    extension (self: (Seq[(Sound, Double)])) {
        def sound = Sound.sum(self*)
    }
}

trait Sound extends (Double => Double) { tone =>
    def play(duration: Double)(using player: Player): Unit =
        val values = Array.range(0, (duration * player.sampleRate).toInt).map(t => tone(t / player.sampleRate))
        player.play(values)
}

object Wave {

    
    def harmonize(voices: Int, f: Double, speed: Double, constr: (Double => Double, Double => Double) => Wave = Sine.apply) =
        Seq.tabulate(voices)(i => constr(_ => i * f, t => 1.0 - math.pow(speed, -t)) -> 1.0 / voices)
    def unison(voices: Int, f: Double, speed: Double = 10, constr: (Double => Double, Double => Double) => Wave = Sine.apply) =
        Seq.fill(voices)(constr(_ => f, t => 1.0 - math.pow(speed, -t)) -> 1.0 / voices)

    extension (self: (Seq[(Wave, Double)])) {
        def detune(range: Double): Seq[(Wave, Double)] =
            self.map { (x, a) => 
                val diff = (math.random() * 2 * range) - range
                (x.copy(frequency = t => x.frequency(t) + diff), a)
            }
    }
    
}

trait Wave extends Sound {
    def frequency: Double => Double
    def amplitude: Double => Double
    def copy(frequency: Double => Double = frequency, amplitude: Double => Double = amplitude): Wave
}

object Sine {
    def apply(frequency: Double) = new Sine(_ => frequency, _ => 1.0)
}
object Square {
    def apply(frequency: Double) = new Square(_ => frequency, _ => 1.0)
}
object Saw {
    def apply(frequency: Double) = new Saw(_ => frequency, _ => 1.0)
}
object Triangle {
    def apply(frequency: Double) = new Triangle(_ => frequency, _ => 1.0)
}

case class Sine(frequency: Double => Double, amplitude: Double => Double) extends Wave {
    def apply(t: Double) = amplitude(t) * math.sin(2 * math.Pi * t * frequency(t))
}

case class Square(frequency: Double => Double, amplitude: Double => Double) extends Wave {
    def apply(t: Double) = amplitude(t) * math.sin(2 * math.Pi * t * frequency(t)).sign
}

case class Saw(frequency: Double => Double, amplitude: Double => Double) extends Wave {
    def apply(t: Double) = amplitude(t) * 2 * (t * frequency(t) - math.floor(0.5 + t * frequency(t)))
}

case class Triangle(frequency: Double => Double, amplitude: Double => Double) extends Wave {
    def apply(t: Double) = amplitude(t) * 2 * math.abs(t * frequency(t) - math.floor(0.5 + t * frequency(t)))
}