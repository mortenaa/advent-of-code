import no.morten.advent.util.readResourceFile

fun main() {
    //val inputFile = "day25_test.txt"
    val inputFile = "day25.txt"
    val input = readResourceFile(inputFile)
    var sum = 0L
    input.lines().forEach {
        sum += snafuToDecimal(it)
        //println("$it \t ${snafuToDecimal(it)}")
    }
    println(sum)
    println(decimalToSnafu(sum))
    println(snafuToDecimal(decimalToSnafu(sum)))

    return
    for (i in 0L .. 400L) {
        val s = decimalToSnafu(i)
        val j = snafuToDecimal(s)
        println("$i\t\t$s\t\t$j")
    }

}

fun decimalToSnafu_(decimal: Int): String {
    var snafu = ""
    val digits = arrayOf('0', '1', '2', '=', '-')
    snafu += digits[decimal % 5]
    if (decimal > 2) snafu += digits[((decimal+2)/5) % 5]
    if (decimal > 12) snafu += digits[((decimal+12)/25) % 5]
    if (decimal > 62) snafu += digits[((decimal+62)/125) % 5]
    return snafu.reversed()
}

fun decimalToSnafu(decimal: Long): String {
    var snafu = ""
    val digits = arrayOf('0', '1', '2', '=', '-')
    var shift = 0L
    var m = 1L
    while (true) {
        shift = m / 2L
        if (decimal > shift) snafu += digits[((decimal+shift)/m % 5).toInt()]
        else break
        m *= 5L
    }
    return if (snafu.isEmpty()) "0" else snafu.reversed()
}

fun snafuToDecimal(snafu: String): Long {
    var i = 1L
    var d = 0L
    for (s in snafu.reversed()) {
        d += when (s) {
            '0' -> 0
            '1' -> 1 * i
            '2' -> 2 * i
            '-' -> -1 * i
            '=' -> -2 * i
            else -> throw IllegalArgumentException()
        }
        i*=5L
    }
    return d
}


fun convertDecimalToOctal(decimal: Int): Int {
    var decimal = decimal
    var octalNumber = 0
    var i = 1

    while (decimal != 0) {
        octalNumber += decimal % 8 * i
        decimal /= 8
        i *= 10
    }

    return octalNumber
}

/**
  Decimal          SNAFU
        1              1            1
        2              2            2
        3             1=          5-2
        4             1-          5-1
        5             10          5+0
        6             11          5+1
        7             12          5+2
        8             2=        5+5-2
        9             2-        5+5-1
        10            20       5+5+0
        11            21
        12            22
        13
        15            1=0
        20            1-0
        2022         1=11-2               2*1
        12345        1-0---0
        314159265  1121-1110-1=0

          SNAFU  Decimal
          1=-0-2     1747
          12111      906
          2=0=      198
          21       11
          2=01      201
          111       31
          20012     1257
          112       32
          1=-1=      353
          1-12      107
          12        7
          1=        3
          122       37


 */