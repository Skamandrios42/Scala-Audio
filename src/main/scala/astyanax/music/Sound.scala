package astyanax.music

object Sound {

    def noise(amplitude: Double) = new Sound {
        def apply(t: Double): Double = amplitude * (math.random() * 2 - 1)
    }
    def from(f: Double => Double) = new Sound {
        def apply(t: Double) = f(t)
    }
    def sum(components: (Sound, Double)*) = new Sound {
        def apply(t: Double): Double = components.map((tone, weight) => tone(t) * weight).sum
    }
    extension (self: (Seq[(Sound, Double)])) {
        def sound = Sound.sum(self*)
    }

    extension (self: Sound) {
        def <*(preprocessor: Double => Double): Sound = (t: Double) => self(preprocessor(t) * t)
        def *>(postprocessor: Double => Double): Sound = (t: Double) => postprocessor(t) * (self(t))
    }
}

trait Sound extends (Double => Double) { tone =>
    def play(duration: Double, msg: String = "")(using player: Player): Unit =
        val values = Array.range(0, (duration * player.sampleRate).toInt).map(t => tone(t / player.sampleRate))
        if msg != "" then println(msg)
        player.play(values)
    
    def delay(time: Double, amount: Int, decay: Int => Double) = 
        Seq.tabulate(amount + 1)(i => (Sound.from(t => apply(t - i * time)) -> decay(i))).sound
}

object Wave {

    def harmonize(voices: Int, f: Double, wave: (Double, Double) => Wave = Sine.apply) =
        Seq.tabulate(voices)(i => wave(i * f, 1.0) -> 1.0 / voices)
    def unison(voices: Int, f: Double, wave: (Double, Double) => Wave = Sine.apply) =
        Seq.fill(voices)(wave(f, 1.0) -> 1.0 / voices)

    extension (self: (Seq[(Wave, Double)])) {
        def detune(range: Double): Seq[(Wave, Double)] =
            self.map { (x, a) => 
                val diff = (math.random() * 2 * range) - range
                (x.copy(frequency = x.frequency + diff), a)
            }
    }
    
}

trait Wave extends Sound {
    def frequency: Double
    def amplitude: Double
    def copy(frequency: Double = frequency, amplitude: Double = amplitude): Wave
}

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
