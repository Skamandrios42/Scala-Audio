package astyanax.utils

import scala.concurrent.duration.Duration

object Benchmark {

    private def mkString(time: Double) =
        if (time < 1000) f"$time%.3fms"
        else if (time < 60000) f"${time / 1000}%.3fs"
        else if (time < 3600000) f"${time / 60000}%.3fmin"
        else if (time < 86400000) f"${time / 3600000}%.3fh"
        else f"${time / 86400000}%.3fd"
  

    def measure[T](block: => T) = 
        val start = System.nanoTime()
        val res = block
        val stop = System.nanoTime()
        val diff = (stop - start) / 1e6
        println(mkString(diff.toDouble))
        res
}