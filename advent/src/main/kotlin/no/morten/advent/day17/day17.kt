package no.morten.advent.day17

import no.morten.advent.util.readResourceFile
import java.util.*
import kotlin.math.max


fun main() {
    val inputFile = "day17_test.txt"
    //val inputFile = "day17.txt"
    val input = readResourceFile(inputFile)
    val moves = parseMoves(input)

    val map = emptySet<Pair<Int, Long>>().toMutableSet()

    val shapes = listOf (
        listOf(0 to 0L, 1 to 0L, 2 to 0L, 3 to 0L),           // -
        listOf(1 to 0L, 0 to 1L, 1 to 1L, 2 to 1L, 1 to 2L),  // +
        listOf(0 to 0L, 1 to 0L, 2 to 0L, 2 to 1L, 2 to 2L),  // _|
        listOf(0 to 0L, 0 to 1L, 0 to 2L, 0 to 3L),           // |
        listOf(0 to 0L, 1 to 0L, 0 to 1L, 1 to 1L)            // o
    )

    var top = -1L
    var j = 0
    loop@for (i in 0 until 1000000000000L) {
    //loop@for (i in 0 until 2022) {
        val x = 2
        val y = top + 4
        var s = (shapes[(i % 5).toInt()]).at(x, y)

        var stopped = false
        while (!stopped) {
            var move = if (j % 2 == 0) moves[(j/2) % moves.size] else 'v'
            when (move) {
                '<' -> {
                    val newS = s.left()
                    if (newS.none() { it.first < 0 } && newS.none { it in map })
                        s = newS
                }
                '>' -> {
                    val newS = s.right()
                    if (newS.none() { it.first > 6} && newS.none { it in map })
                        s = newS
                }
                else -> {
                    val newS = s.down()
                    if (newS.none() { it.second < 0} && newS.none { it in map }) {
                        s = newS
                    } else {
                        map.addAll(s)
                        val max = s.maxBy { it.second }.second
                        if (
                            (0 to max in map || 0 to max - 1 in map || 0 to max - 2 in map) &&
                            (1 to max in map || 1 to max - 1 in map || 1 to max - 2 in map) &&
                            (2 to max in map || 2 to max - 1 in map || 2 to max - 2 in map) &&
                            (3 to max in map || 3 to max - 1 in map || 3 to max - 2 in map) &&
                            (4 to max in map || 4 to max - 1 in map || 4 to max - 2 in map) &&
                            (5 to max in map || 5 to max - 1 in map || 5 to max - 2 in map) &&
                            (6 to max in map || 6 to max - 1 in map || 6 to max - 2 in map)
                        ) {
                            //println("Removed at $max")
                            map.removeAll { it.second < max - 2 }
                        }
                        top = max(top, max)
                        stopped = true
                        //println("top: $top")
                    }
                }
            }
            j += 1
            //println("$j: $move")
            //printMap(map + s, top + 7)
        }
        //  1000000000000L
        if (i % 100000000L == 0L)
            println(i)
        //println("$i: ${top+1}")

    }
    println(top+1)
}

fun printMap(map: Set<Pair<Int, Int>>, top: Int) {
    for (y in top downTo 0) {
        for (x in 0 .. 6) {
            if (x to y in map)
                print("#")
            else
                print(".")
        }
        println()
    }
}

fun List<Pair<Int, Long>>.at(x: Int, y: Long) =
    this.map { it.copy(first = it.first + x, second =  it.second + y)}

fun List<Pair<Int, Long>>.left() =
    this.map { it.copy(first = it.first - 1) }

fun List<Pair<Int, Long>>.right() =
    this.map { it.copy(first = it.first + 1) }

fun List<Pair<Int, Long>>.down() =
    this.map { it.copy(second = it.second - 1) }


fun parseMoves(input: String) =
    input.trim().toCharArray()

/*
The tall, vertical chamber is exactly seven units wide.
Each rock appears so that its left edge is two units away from the left wall
and its bottom edge is three units above the highest rock in the room
(or the floor, if there isn't one).

####

.#.
###
.#.

..#
..#
###

#
#
#
#

##
##

 */