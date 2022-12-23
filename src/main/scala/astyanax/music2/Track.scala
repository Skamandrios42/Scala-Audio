package astyanax.music2

import scala.collection.mutable.ListBuffer

object Track { 

    def repeat(times: Int)(op: => Unit) = 
        var value = 0
        while value < times do
            op
            value += 1

    def scoped(op: Track ?=> Unit): Track =
        given Track = Track()
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

class Track {
    val sounds: ListBuffer[(Double, () => Synth)] = ListBuffer.empty[(Double, () => Synth)]

    def force =
        println("Preparing Track...")
        val prepBar = ProgressBar(30, sounds.length)
        val res = sounds.map { case (amp, sound) => 
            val res = (amp, sound())
            prepBar.update(1)
            res
        }
        println()
        res


    def play(using player: Player) =
        val synths = force
        println("Generating Track...")
        val duration = synths.map(x => x(0) + x(1).length).max
        val array = new Array[Double](((duration * player.sampleRate).ceil).toInt)
        val bar = ProgressBar(30, synths.length)
        for sound <- synths do
            val samples = sound(1).samples
            val offset = (sound(0) * player.sampleRate).toInt
            for i <- samples.indices do
                array(offset + i) += samples(i)
            bar.update(1)
        println()
        println(f"Playing Track (${duration}%.2fs)...")
        player.play(array)
        println("Track played successfully.")
        
}