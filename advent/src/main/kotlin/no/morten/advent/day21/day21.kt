package no.morten.advent.day21

import no.morten.advent.util.readResourceFile
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

val NUMBER = "-?\\d+".toRegex()
val OPERATION = "(\\w+) (-?[\\+\\-\\*\\/]) (-?\\w+)".toRegex()
val ROOT = "root"
val ME = "humn"

sealed interface Expression
data class Operation(val a: Expression, val b: Expression, val o: Operator): Expression
data class Constant(val c: Long): Expression
data class Variable(val name: String): Expression
data class Unknown(val name: String): Expression
enum class Operator(val o: Char) {Add('+'), Sub('-'), Mul('*'), Div('/')}
fun parse(input: String): MutableMap<String, Expression> {
    val map = emptyMap<String, Expression>().toMutableMap()
    input.lines().forEach {
        val name = it.substringBefore(":")
        val x = it.substringAfter(": ")
        if (name == "humn") {
            map[name] = Unknown("humn")
        } else

            if (NUMBER.matches(x)) {
            map[name] = Constant(x.toLong())
        } else if (OPERATION.matches(x)) {
            val (first, operation, second) = OPERATION.find(x)!!.destructured
            val op = when (operation.first()) {
                '+' -> Operator.Add
                '-' -> Operator.Sub
                '*' -> Operator.Mul
                '/' -> Operator.Div
                else -> throw IllegalStateException()
            }
            val exp = Operation(Variable(first), Variable(second), op)
            map[name] = exp
        }
    }
    var done = false
    while (!done) {
        done = true
        for ((name, exp) in map) {
            if (exp is Variable) {
                if (map[exp.name] is Constant) {
                    map[name] = map[exp.name]!!
                }
            } else if (exp is Operation) {
                val a = if (exp.a is Variable) map[exp.a.name]
                    else exp.a
                val b = if (exp.b is Variable) map[exp.b.name]
                    else exp.b
                if (a is Constant && b is Constant) {
                    done = false
                    val e = when (exp.o) {
                        Operator.Add -> Constant(a.c + b.c)
                        Operator.Sub -> Constant(a.c - b.c)
                        Operator.Mul -> Constant(a.c * b.c)
                        Operator.Div -> Constant(a.c / b.c)
                    }
                    map[name] = e
                }
            }
        }
    }
    println(map)
    return map
}

fun printFromNode(exp: Expression, map: MutableMap<String, Expression>): String {
    return when (exp) {
        is Constant -> "${exp.c}"
        is Operation -> "(${printFromNode(exp.a, map)} ${exp.o.o} ${printFromNode(exp.b, map)})"
        is Unknown -> "x"
        is Variable -> "${printFromNode(map[exp.name]!!, map)}"
    }
}

@OptIn(ExperimentalTime::class)
fun main() {
    //val inputFile = "day21_test.txt"
    val inputFile = "day21.txt"
    val input = readResourceFile(inputFile)

    val original  =input.lines().map { it.substringBefore(":") to it.substringAfter(": ")}.toMap()
    var map = original.toMutableMap()
    val (FIRST, _, SECOND) = OPERATION.find(map[ROOT]!!)!!.destructured

    val exps = parse(input)
    val first = printFromNode(exps[FIRST]!!, exps)
    val second = printFromNode(exps[SECOND]!!, exps)
    println(first)
    println(second)
    return

    val used = setOf(ROOT).toMutableSet()


    println("${FIRST} ${SECOND}")
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

    //map = original.toMutableMap()
//    val T = 8226036122233L
//    var R = Long.MAX_VALUE
//    var L = 0L
//    while (L <= R) {
//        val m = (L/2+R/2)
//        val a = eval(original.toMutableMap(), FIRST, SECOND, m).first
//        println("$L - $R: $a - $T")
//        if (a < T) {
//            R = m - 1
//        } else if (a > T) {
//            L = m + 1
//        } else {
//            println("Found $a")
//        }
//    }

    for (i in 20000000000000L .. 20000000000100L) {
        val result = eval(original.toMap().toMutableMap(), FIRST, SECOND, i)
        println("$i: $result")
    }

}

fun eval(map: MutableMap<String, String>, first: String, second: String, value: Long): Pair<Long, Long> {
    map[ME] = value.toString()
    while (isNumber(map[first]!!) == null || isNumber(map[second]!!) == null) {
        //println("${map[first]} ${map[second]}")

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