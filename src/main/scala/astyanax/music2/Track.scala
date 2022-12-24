//> using lib "org.scala-lang.modules::scala-parallel-collections:1.0.4"
package astyanax.music2

import scala.collection.mutable.ListBuffer
import scala.collection.parallel.CollectionConverters._
import astyanax.utils.Benchmark
import scala.collection.parallel.ForkJoinTaskSupport
import java.util.concurrent.ForkJoinPool

object Track { 

    def repeat(times: Int)(op: => Unit) = 
        var value = 0
        while value < times do
            op
            value += 1

    def scoped(debugging: Boolean)(op: Track ?=> Unit): Track =
        given Track = Track(debugging)
        op
        summon[Track]

    extension (self: => Synth) {
        def on(start: Double)(using track: Track) =
            track.sounds += (start -> (() => self))
    }

    def empty(using track: Track) = {
        track.sounds.clear()
    }
}

class Track(debugging: Boolean) {
    val sounds: ListBuffer[(Double, () => Synth)] = ListBuffer.empty[(Double, () => Synth)]

    def debug(str: String) = if debugging then println(str)

    def force =
        debug("Preparing Track...")
        val prepBar = ProgressBar(30, sounds.length)
        val res = sounds.par.map { case (amp, sound) => 
            val res = (amp, sound())
            if debugging then prepBar.update(1)
            res
        }
        debug("")
        res


    def play(using player: Player) =
        val synths = force
        debug("Generating Track...")
        val duration = synths.map(x => x(0) + x(1).length).max
        val array = new Array[Double](((duration * player.sampleRate).ceil).toInt)
        val bar = ProgressBar(30, synths.length)
        for sound <- synths.seq do
            val samples = sound(1).samples
            val offset = (sound(0) * player.sampleRate).toInt
            for i <- samples.indices do
                array(offset + i) += samples(i)
            if debugging then bar.update(1)
        debug("")
        debug(f"Playing Track (${duration}%.2fs)...")
        player.play(array, debugging)
        debug("Track played successfully.")
        
}