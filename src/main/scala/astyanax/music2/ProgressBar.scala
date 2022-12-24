package astyanax.music2

class ProgressBar(val length: Int, val toDo: Int) {
    var progress = 0
    def update(done: Int) = synchronized {
        progress += done
        val a = ((progress.toDouble / toDo) * length).toInt
        print(s"\r[${"#" * a}${" " * (length - a)}]")
    }
}
