package astyanax.music2

object Music {

    def apply(op: Player ?=> Track ?=> Unit) = {
        Player.scoped {
            Track.scoped {
                op
            }.play
        }
    }
}
