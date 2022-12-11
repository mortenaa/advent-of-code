package no.morten.advent.day3

import java.io.File

fun Char.priority() =
    when (this.code) {
        in 'a'.code .. 'z'.code -> this.code - 'a'.code + 1
        in 'A'.code .. 'Z'.code -> this.code - 'A'.code + 27
        else -> throw IllegalArgumentException()
    }

fun String.compartments() =
    this.subSequence(0, this.length / 2) to this.subSequence(this.length / 2, this.length)

fun overlap(a: CharSequence, b: CharSequence) =
    a.toSet() intersect b.toSet()

fun priority(sack: String) =
    sack.compartments().let {
        overlap(it.first, it.second)
    }.map {
        it.priority()
    }.sum()

fun part1(): Int {
    val input = read("list/src/main/resources/day3.txt")!!.split("\n")
    return input.map { priority(it) }.sum()
}

fun part2(): Int {
    val input = read("list/src/main/resources/day3.txt")!!.split("\n")
    return input.chunked(3).map {
        val common = (it[0].toSet() intersect it[1].toSet()) intersect it[2].toSet()
        common.first().priority()
    }.sum()
}

fun read(path: String): String =
    File(path).readText()

fun main() {
    println("Part 1: ${part1()}")
    println("Part 2: ${part2()}")
}