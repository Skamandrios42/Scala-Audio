package astyanax.music2

object Music {

    def apply(debugging: Boolean)(op: Player ?=> Track ?=> Unit) = {
        Player.scoped {
            Track.scoped(debugging) {
                op
            }.play
        }
    }
}
