package astyanax.music

@main def test = Player.scoped {
    for i <- 50 to 500 by 50 do 
        Saw(i).play(2)

    for i <- 100 to 0 by -10 do
        Sound.sum(
            Sine(400) -> 0.5,
            Sine(400 + i) -> 0.5
        ).play(1)

    Thread.sleep(1000)

    Sound.sum(
        Sine(1 * 440) -> 0.2,
        Sine(2 * 440) -> 0.2,
        Sine(3 * 440) -> 0.2,
        Sine(4 * 440) -> 0.2,
        Sine(5 * 440) -> 0.1,
        Sine(6 * 440) -> 0.1,
    ).play(10)
}

// @main def tempest = Player.scoped {

//     def freq(t: Double) = 
//         val res = (400 + (100 - 6 * t)) max 400
//         if res < 400 then println(res)
//         res
//     Sound.sum(
//         Sine(400) -> 0.5,
//         Sine(t => (400 + (100 - 6 * t)) max 400, _ => 1) -> 0.5
//     ).play(30)

    // for i <- 100 to 0 by -1 do
    //     Tone.sum(
    //         Sine(400) -> 0.5,
    //         Sine(400 + i) -> 0.5
    //     ).play(0.3)
// }

@main def shepherd = Player.scoped {

    val base = 200
    val top = 800
    var f = 0

    while true do
        Sound.sum(
            Sine(((f +   0) % top) + base) -> 0.5,
            Sine(((f + 100) % top) + base) -> 0.5,
            Sine(((f + 200) % top) + base) -> 0.5,
            Sine(((f + 300) % top) + base) -> 0.5,
            Sine(((f + 400) % top) + base) -> 0.5,
            Sine(((f + 500) % top) + base) -> 0.5,
        ).play(0.05)
        f = (f + 1) % top

}

// @main def shepherd_Pro = Player.scoped {
//     Sound.sum(
//         Sine(t => (2 * t +   0) % 200 + 50, t => 1.0) -> 0.25,
//         Sine(t => (2 * t +  50) % 200 + 50, t => 1.0) -> 0.25,
//         Sine(t => (2 * t + 100) % 200 + 50, t => 1.0) -> 0.25,
//         Sine(t => (2 * t + 150) % 200 + 50, t => 1.0) -> 0.25,
//     ).play(60)
// }

// def surround(f: Double) = Sound.sum(
//     Sine(1.003 * f) -> 0.2,
//     Sine(1.001 * f) -> 0.2,
//     Sine(1.000 * f) -> 0.2,
//     Sine(0.009 * f) -> 0.2,
//     Sine(0.007 * f) -> 0.2,
// )

// def harmonized(f: Double, speed: Double, constr: (Double => Double, Double => Double) => Sound) = Sound.sum(
//     constr(_ =>  1 * f, t => 1.0 - math.pow(speed, -t)) -> 0.3,      //  - math.pow(speed, -t)   
//     constr(_ =>  2 * f, t => 1.0 - math.pow(speed, -t)) -> 0.2,      //  - math.pow(speed, -t)   
//     constr(_ =>  3 * f, t => 1.0 - math.pow(speed, -t)) -> 0.2,      //  - math.pow(speed, -t)   
//     constr(_ =>  4 * f, t => 1.0 - math.pow(speed, -t)) -> 0.1,      //  - math.pow(speed, -t)   
//     constr(_ =>  5 * f, t => 1.0 - math.pow(speed, -t)) -> 0.05,     //  - math.pow(speed, -t)   
//     constr(_ =>  6 * f, t => 1.0 - math.pow(speed, -t)) -> 0.05,     //  - math.pow(speed, -t)   
//     constr(_ =>  7 * f, t => 1.0 - math.pow(speed, -t)) -> 0.05,     //  - math.pow(speed, -t)   
//     constr(_ =>  8 * f, t => 1.0 - math.pow(speed, -t)) -> 0.01,     //  - math.pow(speed, -t)   
//     constr(_ =>  9 * f, t => 1.0 - math.pow(speed, -t)) -> 0.01,     //  - math.pow(speed, -t)   
//     constr(_ => 10 * f, t => 1.0 - math.pow(speed, -t)) -> 0.01,     //  - math.pow(speed, -t)   
//     constr(_ => 11 * f, t => 1.0 - math.pow(speed, -t)) -> 0.01,     //  - math.pow(speed, -t)   
//     constr(_ => 12 * f, t => 1.0 - math.pow(speed, -t)) -> 0.01,     //  - math.pow(speed, -t)   
// )

// @main def full_tempered = Player.scoped {
//     Sound.sum(
//         harmonized(440, 2) -> 0.25,
//         harmonized(523.25, 1.5) -> 0.25,
//         harmonized(659.26, 1.2) -> 0.25,
//         harmonized(880, 1.1) -> 0.25,
//     ).play(10)
// }

// def unison(voices: Int, wave: Sound) =
//     Sound.sum(Seq.fill(voices)(wave -> 1.0 / voices)*) 

// @main def full_natural = Player.scoped {
//     Sound.sum(
//         harmonized(440, 2) -> 0.25,
//         harmonized(440 * 6.0 / 5.0, 1.5) -> 0.25,
//         harmonized(440 * 3.0 / 2.0, 1.2) -> 0.25,
//         harmonized(440 * 2.0, 1.1) -> 0.25,
//     ).play(10)
// }

// @main def full_minor7 = Player.scoped {
//     Sound.sum(
//         harmonized(440, 2) -> 0.25,
//         harmonized(554.37, 1.5) -> 0.25,
//         harmonized(659.26, 1.2) -> 0.25,
//         harmonized(783.99, 1.1) -> 0.25,
//     ).play(2)
//     Sound.sum(
//         harmonized(440, 2) -> 0.25,
//         harmonized(587.33, 1.5) -> 0.25,
//         harmonized(739.99, 1.2) -> 0.25,
//     ).play(2)
// }

@main def scale = Player.scoped {
    val notes = Notes(443)
    Seq.range(0, 2).flatMap(i => Seq("a", "h", "c", "d", "e", "f", "gis").map(notes.get(_, i))).foreach { f =>
        println(f)
        Wave.unison(10, f, Triangle.apply).detune(2).sound.play(1)
    }
    Wave.unison(10, notes.get("a", 2), Triangle.apply).sound.play(1)
}

// @main def compare = Player.scoped {
//     Sine(440).play(1)
//     Thread.sleep(1000)
//     surround(440).play(2)
// }

@main def uni = Player.scoped {
    Sound.sum(
        Wave.unison(20, 220, Triangle.apply).detune(2).sound -> 0.4,
        Wave.unison(20, 440, Triangle.apply).detune(2).sound -> 0.3,
        Wave.unison(20, 880, Triangle.apply).detune(2).sound -> 0.3,
    ).play(3)
}




@main def dominantsept = Player.scoped {
    val notes = Notes(440)
    Seq("a", "cis", "e", "g").map(s => Wave.unison(10, notes.get(s), Triangle.apply).detune(1).sound -> 0.25).sound.play(3)
    Seq("d", "fis").map(notes.get).map(f => Wave.unison(10, f, Triangle.apply).detune(1).sound -> 0.25).sound.play(3)
}


//def env(t: Double) = (1.0 - 5 * math.pow((t - 1 - math.sqrt(0.2)), 2)) max 0

def env(t: Double) = (10 * t) min math.pow(0.1, t - 0.2) min 0.3 max 0.0
def baseEnv(t: Double, end: Double, top: Double) = 
    if t < end then (2 * t) min top * math.pow(0.8, t - 0.2) max 0.0
    else 0

@main def shifting = Player.scoped {
    val notes = Notes(440)
    val sound = Wave.unison(20, notes.get("a"), Triangle.apply).detune(2).sound
    delay(sound *> env, 1, 10, math.pow(0.5, _)).play(10)

}

def delay(sound: Sound, time: Double, amount: Int, decay: Int => Double) = 
    Seq.tabulate(amount + 1)(i => (Sound.from(t => sound(t - i * time)) -> decay(i))).sound

import Track.on

@main def simpleTrack = Player.scoped {
    val notes = Notes(440)
    def note(note: String, i: Int) = 
        Synth.from(Wave.unison(20, notes.get(note, i), Triangle.apply).detune(2).sound, env, 4)
    def base(note: String, i: Int, length: Double) = 
        Synth.from(Sine(notes.get(note, i)), baseEnv(_, end = length, top = 0.1), length)
    val c = Counter(0, 0.3)

    val track = Track.scoped {
        base("g", -3, 8 * c.diff - 0.1) on c.value
        note("g", -2) on c()
        note("d", -1) on c()
        note("h",  0) on c()
        note("a",  0) on c()
        note("h",  0) on c()
        note("d", -1) on c()
        note("h",  0) on c()
        note("d", -1) on c()
        
        base("g", -3, 8 * c.diff - 0.1) on c.value
        note("g", -2) on c()
        note("d", -1) on c()
        note("h",  0) on c()
        note("a",  0) on c()
        note("h",  0) on c()
        note("d", -1) on c()
        note("h",  0) on c()
        note("d", -1) on c()

        base("c", -3, 8 * c.diff - 0.1) on c.value
        note("g", -2) on c()
        note("e", -1) on c()
        note("c",  0) on c()
        note("h",  0) on c()
        note("c",  0) on c()
        note("e", -1) on c()
        note("c",  0) on c()
        note("e", -1) on c()
        
        base("c", -3, 8 * c.diff - 0.1) on c.value
        note("g", -2) on c()
        note("e", -1) on c()
        note("c",  0) on c()
        note("h",  0) on c()
        note("c",  0) on c()
        note("e", -1) on c()
        note("c",  0) on c()
        note("e", -1) on c()

        base("fis", -3, 8 * c.diff - 0.1) on c.value
        note("g", -2) on c()
        note("fis", -1) on c()
        note("c", 0) on c()
        note("h", 0) on c()
        note("c", 0) on c()
        note("fis", -1) on c()
        note("c", 0) on c()
        note("fis", -1) on c()

        base("fis", -3, 8 * c.diff - 0.1) on c.value
        note("g", -2) on c()
        note("fis", -1) on c()
        note("c", 0) on c()
        note("h", 0) on c()
        note("c", 0) on c()
        note("fis", -1) on c()
        note("c", 0) on c()
        note("fis", -1) on c()

        base("g", -3, 8 * c.diff - 0.1) on c.value
        note("g", -2) on c()
        note("g", -1) on c()
        note("h",  0) on c()
        note("a",  0) on c()
        note("h",  0) on c()
        note("g", -1) on c()
        note("h",  0) on c()
        note("g", -1) on c()

        base("g", -3, 8 * c.diff - 0.1) on c.value
        note("g", -2) on c()
        note("g", -1) on c()
        note("h",  0) on c()
        note("a",  0) on c()
        note("h",  0) on c()
        note("g", -1) on c()
        note("h",  0) on c()
        note("fis", -1) on c()

    }
    track.play

}

class Counter(var value: Double, val diff: Double) {
    def apply(): Double =
        val before = value
        value += diff
        before
}


@main def awake = Player.scoped {

    val notes = Notes(440)
    val t = Timer(0.2)

    def key(note: String, i: Int) =
        val f = notes.get(note, i)
        val noise = Sound.noise(0.2)
        val start = Wave.unison(10, notes.get(note, i + 1), Sine.apply).detune(5).sound
        val end = Wave.harmonize(3, notes.get(note, i), Triangle.apply).detune(1).sound
        val chorus = Wave.unison(6, notes.get(note, i), Square.apply).detune(1).sound
        def env(f: Double => Double): Double => Double = t => if t > 0 then f(t) min 1.0 max 0.0 else 0.0
        def env_noise(t: Double) = (-8 * t + 1)
        def env_start(t: Double) = (math.pow(2, -8 * t))
        def env_end(t: Double) = 0.6 - (math.pow(2, -8 * t)) min (math.pow(3, -2 * t))
        val sound = Sound.sum(
            start *> env(env_start) -> 0.4,
            noise *> env(env_noise) -> 0.2,
            end *> env(env_end) -> 0.35,
            chorus *> env(env_end(_)) -> 0.05,
        )
        Synth.from(delay(sound, 0.4, 8, math.pow(0.5, _)), 4)
        
    Track.scoped {
        Synth.from(key("a", 0), 5) on t.next
        Synth.from(key("a", 0), 5) on t.next
        Synth.from(key("as", 0), 5) on t.next
        Synth.from(key("a", 0), 5) on t.next
        Synth.from(key("c", 0), 5) on t.next
        t.wait(7.0)
        Synth.from(key("g", -1), 5) on t.next
        Synth.from(key("g", -1), 5) on t.next
        Synth.from(key("d", -1), 5) on t.next
        Synth.from(key("d", 0), 5) on t.next
        Synth.from(key("a", -1), 5) on t.next
        Synth.from(key("a", 0), 5) on t.next
        Synth.from(key("as", 0), 5) on t.next
        Synth.from(key("a", 0), 5) on t.next
        Synth.from(key("c", 0), 5) on t.next
    }.play
    println("finishing main")
}

@main def testFilters() = 
    println(FIRFilter(Seq(0.4, 0.6)).useOn(Array(1, 2, 3, 1, 1)).mkString("[", ", ", "]"))
    Player.scoped {
    val sound = Sound.noise(0.2)
    val synth = Synth.from(sound, 2)
    Track.scoped {
        synth on 0
        synth.filter(FIRFilter(Seq.fill(100)(0.01))) on 3
    }.play
}

    