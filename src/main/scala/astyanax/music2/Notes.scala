package astyanax.music2

class Notes(a: Double) {
    def all = Seq(
        "a", "ais", "b",
        "h", "c", "cis", 
        "des", "d", "dis",
        "e", "f", "fis", 
        "ges", "g", "gis",
        "as",
    )
    
    def get(note: String): Double = note match
        case "a"    => a * math.pow(2, ( 1 - 1) / 12.0)
        case "ais"  => a * math.pow(2, ( 2 - 1) / 12.0)
        case "b"    => a * math.pow(2, ( 2 - 1) / 12.0)
        case "h"    => a * math.pow(2, ( 3 - 1) / 12.0)
        case "c"    => a * math.pow(2, ( 4 - 1) / 12.0)
        case "cis"  => a * math.pow(2, ( 5 - 1) / 12.0)
        case "des"  => a * math.pow(2, ( 5 - 1) / 12.0)
        case "d"    => a * math.pow(2, ( 6 - 1) / 12.0)
        case "dis"  => a * math.pow(2, ( 7 - 1) / 12.0)
        case "e"    => a * math.pow(2, ( 8 - 1) / 12.0)
        case "f"    => a * math.pow(2, ( 9 - 1) / 12.0)
        case "fis"  => a * math.pow(2, (10 - 1) / 12.0)
        case "ges"  => a * math.pow(2, (10 - 1) / 12.0)
        case "g"    => a * math.pow(2, (11 - 1) / 12.0)
        case "gis"  => a * math.pow(2, (12 - 1) / 12.0)
        case "as"   => a * math.pow(2, (12 - 1) / 12.0)
    
    def get(note: String, i: Int): Double = get(note) * math.pow(2, i)
}
