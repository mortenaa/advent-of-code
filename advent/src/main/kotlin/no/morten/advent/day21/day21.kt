package no.morten.advent.day21

import no.morten.advent.util.readResourceFile
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

val NUMBER = "-?\\d+".toRegex()
val OPERATION = "(\\w+) (-?[\\+\\-\\*\\/]) (-?\\w+)".toRegex()
val ROOT = "root"
val ME = "humn"

@OptIn(ExperimentalTime::class)
fun main() {
    //val inputFile = "day21_test.txt"
    val inputFile = "day21.txt"
    val input = readResourceFile(inputFile)



    val used = setOf(ROOT).toMutableSet()

    val original  =input.lines().map { it.substringBefore(":") to it.substringAfter(": ")}.toMap()
    var map = original.toMutableMap()
    val (FIRST, _, SECOND) = OPERATION.find(map[ROOT]!!)!!.destructured
    println("${map[FIRST]} ${map[SECOND]}")
    val time = measureTime {
        while (isNumber(map["root"]!!) == null) {
            for ((k, v) in map) {
                if (NUMBER.matches(v)) {

                } else if (OPERATION.matches(v)) {
                    val (x, o, y) = OPERATION.find(v)!!.destructured
                    val xx = map[x]!!
                    val yy = map[y]!!
                    var xv: Long? = null
                    var yv: Long? = null
                    if (NUMBER.matches(xx)) {
                        xv = xx.toLong()
                    }
                    if (NUMBER.matches(yy)) {
                        yv = yy.toLong()
                    }
                    if (xv != null && yv != null) {
                        map[k] = when (o.first()) {
                            '+' -> "${xv + yv}"
                            '-' -> "${xv - yv}"
                            '*' -> "${xv * yv}"
                            '/' -> "${xv / yv}"
                            else -> throw IllegalStateException()
                        }
                    }
                } else {
                    throw IllegalStateException()
                }
            }
        }
    }
    println(map["root"])
    println(time)

    map = original.toMutableMap()
    val T = 8226036122233L
    var R = Long.MAX_VALUE
    var L = 0L
    while (L <= R) {
        val m = (L+R)/2
        val a = eval(map.toMutableMap(), FIRST, SECOND, m).first
        if (a < T) {

        } else if (a > T) {

        } else {
            
        }
    }

    for (i in 0 .. 301L) {
        val result = eval(map.toMutableMap(), FIRST, SECOND, i)
        println("$i: $result")
    }

}

fun eval(map: MutableMap<String, String>, first: String, second: String, value: Long): Pair<Long, Long> {
    map[ME] = value.toString()
    while (isNumber(map[first]!!) == null || isNumber(map[second]!!) == null) {
        for ((k, v) in map) {
            if (NUMBER.matches(v)) {

            } else if (OPERATION.matches(v)) {
                val (x, o, y) = OPERATION.find(v)!!.destructured
                val xx = map[x]!!
                val yy = map[y]!!
                var xv: Long? = null
                var yv: Long? = null
                if (NUMBER.matches(xx)) {
                    xv = xx.toLong()
                }
                if (NUMBER.matches(yy)) {
                    yv = yy.toLong()
                }
                if (xv != null && yv != null) {
                    map[k] = when (o.first()) {
                        '+' -> "${xv + yv}"
                        '-' -> "${xv - yv}"
                        '*' -> "${xv * yv}"
                        '/' -> "${xv / yv}"
                        else -> throw IllegalStateException()
                    }
                }
            } else {
                throw IllegalStateException()
            }
        }
    }
    return map[first]!!.toLong() to map[second]!!.toLong()
}

fun isNumber(i: String): Long? {
    return if (i.first().code in '0'.code .. '9'.code)
        i.toLong()
    else
        null
}