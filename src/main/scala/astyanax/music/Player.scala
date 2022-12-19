package astyanax.music

import javax.sound.sampled.AudioFormat
import javax.sound.sampled.DataLine
import javax.sound.sampled.SourceDataLine
import javax.sound.sampled.AudioSystem

object Player {

    def rate(using player: Player) = player.sampleRate

    def scoped(op: Player ?=> Unit): Unit = 
        val sampleRate = 44100.0F
        val format = AudioFormat(sampleRate, 16, 1, true, false)
        val info = DataLine.Info(classOf[SourceDataLine], format)
        val line = AudioSystem.getLine(info).asInstanceOf[SourceDataLine]
        line.open(format)
        val buffer = Array.ofDim[Byte](10000)
        given Player(buffer, line, sampleRate)
        line.start()
        op
        line.drain()
        line.stop()

}

class Player(val buffer: Array[Byte], val line: SourceDataLine, val sampleRate: Float) {

    private var i = 0

    def play(value: Double): Unit =
        val var2 = (Short.MaxValue * (-1.0 max value min 1.0)).toInt.toShort
        buffer(i) = var2.toByte
        i += 1
        buffer(i) = (var2 >> 8).toByte
        i += 1
        if i >= buffer.length then
            line.write(buffer, 0, buffer.length)
            i = 0

    def play(values: Array[Double]): Unit = for value <- values do play(value)
    
}

