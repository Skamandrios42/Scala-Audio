package astyanax.music2

object Envelope {
    def apply(f: Double => Double, lower: Double = 0.0, upper: Double = 1.0, border: Double = 0.0) = 
        new Envelope(f, lower, upper, border)
}

case class Envelope(f: Double => Double, lower: Double, upper: Double, border: Double) extends (Double => Double) {
    def apply(t: Double): Double = 
      if t > border then f(t) max lower min upper else 0.0
}
