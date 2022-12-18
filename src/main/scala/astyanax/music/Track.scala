package astyanax.music

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
        for sound <- sounds do
            print("adding sound... ")
            Console.flush()
            val samples = sound(1).samples
            print("sampling... ")
            Console.flush()
            val offset = (sound(0) * player.sampleRate).toInt
            for i <- samples.indices do
                array(offset + i) += samples(i)
            println("added sound... ")
        println("Playing Track...")
        player.play(array)
        
}