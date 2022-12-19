package astyanax.music2

import scala.collection.mutable.ListBuffer

object Track { 
    def scoped(op: Track ?=> Unit): Track =
        given Track = Track()
        op
        summon[Track]

    extension (self: Synth) {
        def on(start: Double)(using track: Track) =
            track.sounds += (start -> self)
    }
}

class Track {
    val sounds = ListBuffer.empty[(Double, Synth)]

    def play(using player: Player) =
        println("Generating Track...")
        val duration = sounds.map(x => x(0) + x(1).length).max
        val array = new Array[Double](((duration * player.sampleRate).ceil).toInt)
        val bar = ProgressBar(30, sounds.length)
        for sound <- sounds do
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