package no.morten.advent.day13

import no.morten.advent.util.readResourceFile
import java.util.StringTokenizer

sealed interface Token {
    object Open : Token { override fun toString() = "[" }
    object Close : Token { override fun toString() = "]" }
    data class Value(val value: Int) : Token { override fun toString() = "$value" }
}

fun main() {
    //val inputFile = "day13_test.txt"
    val inputFile = "day13.txt"
    val input = readResourceFile(inputFile)
    var sum = 0
    val all: MutableList<List<Any>> = emptyList<List<Any>>().toMutableList()
    input.split("\n\n").forEachIndexed { i, list ->
        val a = parseList(list.substringBefore("\n"))
        val b = parseList(list.substringAfter("\n"))
        all.add(a as List<Any>)
        all.add(b as List<Any>)
        val c = compareLists(a, b)
        if (c <= 0)
            sum += i+1
        println("Index ${i+1}: $c")
        println(a)
        println(b)
        println("\n")
    }
    all.add(parseList("[[2]]"))
    all.add(parseList("[[6]]"))
    println(sum)
    println(all)
    val sorted = all.sortedWith { a, b ->
        val c = compareLists(a, b)
        println(a)
        println(b)
        println(c)
        c
    }
    println(sorted)
    val index1 = sorted.indexOfFirst { it.size == 1 && it.first() is List<*> && (it.first() as List<Any>).size == 1 &&  ((it.first() as List<Int>).first() is Int) && ((it.first() as List<Int>).first() == 2)} + 1
    val index2 = sorted.indexOfFirst { it.size == 1 && it.first() is List<*> && (it.first() as List<Any>).size == 1 &&  ((it.first() as List<Int>).first() is Int) && ((it.first() as List<Int>).first() == 6)} + 1
    println(index1 * index2)
    println(index1)
    println(index2)
    println(sorted[index1-1])
    println(sorted[index2-1])

}

fun compareLists(aList: List<Any>, bList: List<Any>): Int {

//    if (aList.isEmpty()) {
//        return if (bList.isEmpty()) 0 else -1
//    } else if (bList.isEmpty()) return 1

    var aRest = aList
    var bRest = bList
    var aStack = ArrayDeque<Any>()
    var bStack = ArrayDeque<Any>()

    do {
        if (aRest.isEmpty() && bRest.isEmpty()) {
            if (aStack.isEmpty() && bStack.isEmpty()) {
                return 0
            } else {
                aRest = aStack.removeLast() as List<Any>
                bRest = bStack.removeLast() as List<Any>
                continue
            }
        } else if (aRest.isEmpty())
            return -1
        else if (bRest.isEmpty())
            return 1

        var a = aRest.first()
        var b = bRest.first()
        aRest = aRest.drop(1)
        bRest = bRest.drop(1)

        if (a is Int && b is Int) {
            if (a < b)
                return -1
            else if (b < a)
                return 1
            else
                continue
        }

        if (a is Int && b is List<*>) {
            a = listOf(a)
        } else if (a is List<*> && b is Int) {
            b = listOf(b)
        }

        if (a is List<*> && b is List<*>) {
            aStack.addLast(aRest)
            bStack.addLast(bRest)
            aRest = a as List<Any>
            bRest = b as List<Any>
            continue
        }

    } while (true)

}

fun parseList(input: String): List<Any> {
    val stack = ArrayDeque<List<Any>>()
    var current = emptyList<Any>()

    StringTokenizer(input, "[],", true).toList().forEach {
        when {
            it == "," -> { }
            it == "[" -> {
                stack.addLast(current)
                current = emptyList()
            }
            it == "]" -> {
                val s = stack.removeLast()
                current = s + listOf(current)
            }
            else -> {
                current = current + (it.toString().toInt())
            }
        }
    }
    println(current)
    return (current.first() as List<Any>)
}