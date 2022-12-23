package astyanax.music2

import astyanax.utils.Benchmark

object Filter {
    def apply(synth: Synth, coefficients: Seq[Double]) =
        val samples = synth.samples
        val res = new Array[Double](samples.length)
        for i <- coefficients.length until samples.length do
            res(i) = samples.slice(i - coefficients.length, i).zip(coefficients).map(_ * _).sum
        System.arraycopy(samples, 0, res, 0, coefficients.length)
        ArraySynth(res, synth.rate)

    def timed(synth: Synth, coefficients: Double => Seq[Double]) = 
        val samples = synth.samples
        val res = new Array[Double](samples.length)
        for i <- samples.indices do
            val allCoeffs = coefficients(i / synth.rate)
            val coeffs = allCoeffs.drop(allCoeffs.length - i)
            val sampleSlice = samples.slice(i - coeffs.length, i)
            res(i) = (sampleSlice zip coeffs).map(_ * _).sum
        ArraySynth(res, synth.rate)

    // def timed_old(synth: Synth, coefficients: Double => Seq[Double]) =
    //     val samples = synth.samples
    //     //println(samples.mkString(", "))
    //     val res = new Array[Double](samples.length)
    //     for i <- samples.indices do
    //         val coeffs = coefficients(i / synth.rate) // coefficients
    //         val start = (i - coeffs.length + 1) max 0
    //         //println(s"index = $i, ${coeffs.mkString("{", ",", "}")}, start = $start")
    //         for j <- start to i do
    //             val off = coeffs.length - i - 1 + start
    //             val add = samples(j) * coeffs(j - start + off)
    //             //print(s"add $add")
    //             res(i) += add
    //             //println()
    //     ArraySynth(res, synth.rate)
    
    // def timedFast(samples: Array[Double], coefficients: Double => Seq[Double]) =
    //     println(samples.mkString(", "))
    //     val res = new Array[Double](samples.length)
    //     for i <- samples.indices do
    //         val coeffs = coefficients(i) // coefficients
    //         val start = (i - coeffs.length + 1) max 0
    //         println(s"index = $i, ${coeffs.mkString("{", ",", "}")}, start = $start")
    //         for j <- start to i do
    //             val off = coeffs.length - i - 1 + start
    //             val add = samples(j) * coeffs(j - start + off)
    //             print(s"add $add")
    //             res(i) += add
    //             println()
    //     res
}

// @main def testtimedFast =
//     val array = Array.range(2, 10).map(_.toDouble)
//     println(Filter.timedFast(array, t => Seq(1, t, t-1)).mkString(", "))
