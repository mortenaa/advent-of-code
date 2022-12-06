package no.morten.advent.day5

import java.io.File

fun makemoves(inOder: Boolean): String {
    val input = read("list/src/main/resources/day5.txt")!!
    val (crates, moves) = input.split("\n\n").let { it[0] to it[1] }
    val cr = parseCrates(crates)
    val mv = parseMoves(moves)

    for (m in mv) {
        val from = m.second - 1
        val to = m.third - 1
        val num = m.first
        val s = cr[from].takeLast(num)
        cr[to].addAll(if (inOder) s else s.reversed())
        cr[from] = cr[from].take(cr[from].size-num).toMutableList()
    }

    val word =cr.map { it.last() }.joinToString(separator = "")
    return word
}

fun parseMoves(moves: String): List<Triple<Int, Int, Int>> {
    return moves.split("\n").map { it.split(" ").let { Triple(it[1].toInt(), it[3].toInt(), it[5].toInt()) } }
}

fun parseCrates(crates: String): MutableList<MutableList<Char>> {
    val lines = crates.split("\n")
    val num = lines.last().trim().split("\\s+".toRegex()).size
    val c = mutableListOf<MutableList<Char>>()
    val l = lines.map { it.padEnd(num*4 + 2) }.reversed().drop(1)
    for (i in 0 until  num) {
        val x = i*4 + 1
        val s = l.map { it[x] }.filter { it != ' ' }
        c.add(s.toMutableList())
    }
    return c
}

fun read(path: String): String =
        File(path).readText()

fun main() {
    println("Part 1: ${makemoves(false)}")
    println("Part 2: ${makemoves(true)}")
}
