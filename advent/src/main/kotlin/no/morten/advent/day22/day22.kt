package no.morten.advent.day22

import no.morten.advent.util.readResourceFile
import java.lang.Math.floorMod
import java.lang.Math.min
import kotlin.time.ExperimentalTime




@OptIn(ExperimentalTime::class)
fun main() {
    //val inputFile = "day22_test.txt"
    val inputFile = "day22.txt"
    val input = readResourceFile(inputFile)
    val maze = input.split("\n\n")[0].lines()
    val instructions = parseInstructions(input.split("\n\n")[1])
    val width = maze.maxOf { it.length }
    val height = maze.size
    println("$width x $height")
    val array = maze.map { it.padEnd(width, ' ').toCharArray() }.toTypedArray()

    val colMin = IntArray(width) { height }
    val colMax = IntArray(width) { 0 }
    val rowMin = IntArray(height) { width }
    val rowMax = IntArray(height) { 0 }

    for ((i, s) in array.withIndex()) {
        val minX = s.indexOfFirst { it != ' ' }
        val maxX = s.indexOfLast { it != ' ' }
        if (minX  < rowMin[i]) { rowMin[i] = minX }
        if (maxX > rowMax[i]) { rowMax[i] = maxX }
    }

    for (j in 0 until width) {
        for (i in 0 until height) {
            if (array[i][j] != ' ')
                if (i < colMin[j]) {
                    colMin[j] = i
                    break
                }
        }
        for (i in height-1 downTo 0) {
            if (array[i][j] != ' ')
                if (i > colMax[j]) {
                    colMax[j] = i
                    break
                }
        }

    }

    println(colMin.zip(colMax).joinToString())
    for (s in array.withIndex())
        println("${s.value.joinToString("")}    : ${rowMin[s.index]} - ${rowMax[s.index]}")

    println(instructions.joinToString("\n"))
    var heading = 0  // > v < ^
    val startY = 0
    val startX = rowMin[0]
    var pos = startX to startY

    fun newPos(pos: Pair<Int, Int>, heading: Int, steps: Int, array: Array<CharArray>): Pair<Int, Int> {
        var x = pos.first
        var y = pos.second
        var min = 0
        var max = 0
        var num = 0
        var step = 1
        var p = 0
        var arr = "".toCharArray()
        when (heading) {
            0 -> {
                min = rowMin[y]; max = rowMax[y]
                num = steps; step = 1; p = x - min
                arr = array.row(pos.second, min, max)
            }
            1 -> {
                min = colMin[x]; max = colMax[x]
                num = steps; step = 1; p = y - min
                arr = array.col(pos.first, min, max)
            }
            2 -> {
                min = rowMin[y]; max = rowMax[y]
                num = -steps; step = -1; p = x - min
                arr = array.row(pos.second, min, max)
            }
            3 -> {
                min = colMin[x]; max = colMax[x]
                num = -steps; step = -1; p = y - min
                arr = array.col(pos.first, min, max)
            }
        }
        if (arr.contains('#')) {
            repeat(steps) {
                val newP = floorMod(p+step, arr.size)
                if (arr[newP] == '#')
                    return if (heading % 2 == 0) p + min to y else x to p + min
                else
                    p = newP
            }
            return if (heading % 2 == 0) p + min to y else x to p + min
        } else {
            return if (heading % 2 == 1)
                    floorMod(p + num,  arr.size) + min to y
                else
                    x to floorMod(p + num, arr.size + min)
        }
    }
    println("heading: $heading, x: ${pos.first}, y: ${pos.second}")
    for (instruction in instructions) {
        when (instruction) {
            is Instruction.Left -> heading = floorMod((heading - 1), 4).also { println("Left") }
            is Instruction.Right -> heading = floorMod((heading + 1), 4).also { println("Right")}
            is Instruction.Move -> pos = newPos(pos, heading, instruction.steps, array).also { println("Move ${instruction.steps}") }
        }
        println("heading: $heading, x: ${pos.first}, y: ${pos.second}")
    }
    println("${(pos.second+1)*1000 + (pos.first+1)*4 + heading}")

}

inline fun Array<CharArray>.col(col: Int, min: Int, max: Int) = (min .. max).map { this[it][col] }.toCharArray()
inline fun Array<CharArray>.row(row: Int, min: Int, max: Int) = (min .. max).map { this[row][it] }.toCharArray()

sealed interface Instruction {
    data class Move(val steps: Int): Instruction
    object Left: Instruction
    object Right: Instruction
}



fun parseInstructions(input: String): List<Instruction> {
    var s = input
    return buildList {
        while (s.length > 0) {
            val m = "^\\d+".toRegex().find(s)
            if (m != null) {
                add(Instruction.Move(m.value.toInt()))
                s = s.drop(m.value.length)
            } else if (s.first() == 'L') {
                add(Instruction.Left)
                s = s.drop(1)
            } else {
                add(Instruction.Right)
                s = s.drop(1)
            }
        }
    }
}
