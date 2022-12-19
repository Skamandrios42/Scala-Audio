package astyanax.music2

import scala.annotation.targetName

trait Composable[A] {
    def impl(elems: Seq[(A, Double)]): A
    extension (self: A) {
        def ++(that: A): A = impl(Seq(self -> 0.5, that -> 0.5))
    }
    extension (self: Seq[(A, Double)]) {
        @targetName("combineWeighed") 
        def combine = impl(self)
    }
    extension (self: Seq[A]) {
        @targetName("combine") 
        def combine = sum(self*)
    }
    @targetName("weighedSum")
    def sum(elems: (A, Double)*): A = impl(elems)
    @targetName("sum")
    def sum(elems: A*): A = sum(elems.map((_, 1.0 / elems.length))*)
}
