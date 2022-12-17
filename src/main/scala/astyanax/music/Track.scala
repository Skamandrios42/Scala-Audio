package astyanax.music

import scala.collection.mutable.ListBuffer

object Track { 
    def scoped(op: Track ?=> Unit): Track =
        given Track = Track()
        op
        summon[Track]

    extension (self: Sound) {
        def on(start: Double)(using track: Track) =
            track.sounds += (start -> self)
    }
}

class Track {
    val sounds = ListBuffer.empty[(Double, Sound)]

    def play(duration: Double)(using player: Player) =
        println("Generating Track...")
        val combined = sounds.toList.map((offset, sound) => (Sound.from(t => sound(t - offset)) -> 0.5)).sound
        combined.play(duration, msg = "Playing Track...")
}