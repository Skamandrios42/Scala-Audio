package astyanax.music2

class Timer(val step: Double) {
    var now: Double = 0
    def next: Double = next(1)
    def next(steps: Double): Double =
        val before = now
        now += steps * step
        before
    def wait(steps: Double): Unit = next(steps)
}