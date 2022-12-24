package no.morten.advent.day22

import no.morten.advent.util.readResourceFile
typealias Matrix = Array<CharArray>
data class Pos(val x: Int, val y: Int, val heading: Int, val side: Char)

const val SIZE = 50

fun main() {
    //val inputFile = "day22_test.txt"
    val inputFile = "day22.txt"
    val input = readResourceFile(inputFile)
    val all = input.split("\n\n")[0].lines()
    val instructions = parseInstructions(input.split("\n\n")[1])
    val array: Matrix = all.map { it.padEnd(150, ' ').toCharArray() }.toTypedArray()
//    val A = matrixAt(array, 50,  0)
//    val B = matrixAt(array, 100, 0)
//    val C = matrixAt(array, 50, 50)
//    val D = matrixAt(array, 0, 100)
//    val E = matrixAt(array, 50,100)
//    val F = matrixAt(array, 0, 150)

    var pos = Pos(0, 0, 0, 'A')

//    println("A"); printMatrix(A)
//    println("B"); printMatrix(B)
//    println("C"); printMatrix(C)
//    println("D"); printMatrix(D)
//    println("E"); printMatrix(E)
//    println("F"); printMatrix(F)

//    assertPos(next(array, Pos(49, 0, 0, 'A')), Pos(0, 0, 0, 'B'))
//    assertPos(next(array, Pos(49, 49, 0, 'A')), Pos(0, 49, 0, 'B'))
//    assertPos(next(array, Pos(49, 1, 0, 'A')), Pos(0, 1, 0, 'B'))
//
//    assertPos(next(array, Pos(49, 0, 0, 'B')), Pos(49, 49, 2, 'E'))
//    assertPos(next(array, Pos(49, 49, 0, 'B')), Pos(49, 0, 2, 'E'))
//    assertPos(next(array, Pos(49, 1, 0, 'B')), Pos(49, 48, 2, 'E'))

    for (instruction in instructions) {
        when (instruction) {
            is Instruction.Left -> pos = pos.copy(heading = Math.floorMod((pos.heading - 1), 4)).also { println("Left") }
            is Instruction.Right -> pos = pos.copy(heading = Math.floorMod((pos.heading + 1), 4)).also { println("Right")}
            is Instruction.Move -> {
                repeat(instruction.steps) { pos = next(array, pos) }
                println("Move ${instruction.steps}")
            }
        }
        println("$pos")
    }
    println(pos)
    println(translate(pos))
    val t = translate(pos)
    println("${(t.second+1)*1000 + (t.first+1)*4 + pos.heading}")


}

fun assertPos(a: Pos, b: Pos) {
    assert(a == b) { "$a != $b" }
}

fun translate(pos: Pos): Pair<Int, Int> =
    when (pos.side) {
        'A' -> pos.x + 50  to pos.y
        'B' -> pos.x + 100 to pos.y
        'C' -> pos.x + 50  to pos.y + 50
        'D' -> pos.x       to pos.y + 100
        'E' -> pos.x + 50  to pos.y + 100
        'F' -> pos.x       to pos.y + 150
        else -> throw IllegalStateException()
    }

fun next(all: Matrix, pos: Pos): Pos {
    var newPos = pos.copy(
        x = if (pos.heading == 0) pos.x + 1 else if (pos.heading == 2) pos.x - 1 else pos.x,
        y = if (pos.heading == 1) pos.y + 1 else if (pos.heading == 3) pos.y - 1 else pos.y
    )
    if (newPos.x == SIZE) {
        newPos = when (pos.side) {
            'A' -> pos.copy(side = 'B', x = 0)
            'B' -> pos.copy(side = 'E', x = SIZE-1,  y = SIZE-pos.y-1,  heading = 2)
            'C' -> pos.copy(side = 'B', x = pos.y,   y = SIZE-1,        heading = 3)
            'D' -> pos.copy(side = 'E', x = 0)
            'E' -> pos.copy(side = 'B', x = SIZE-1,  y = SIZE-pos.y-1,  heading = 2)
            'F' -> pos.copy(side = 'E', x = pos.y,   y = SIZE-1,        heading = 3)
            else -> throw IllegalStateException()
        }
    } else if (newPos.x == -1) {
        newPos = when (pos.side) {
            'A' -> pos.copy(side = 'D', x = 0,       y = SIZE-pos.y-1,  heading = 0)
            'B' -> pos.copy(side = 'A', x = SIZE-1)
            'C' -> pos.copy(side = 'D', x = pos.y,   y = 0,             heading = 1)
            'D' -> pos.copy(side = 'A', x = 0,       y = SIZE-pos.y-1,  heading = 0)
            'E' -> pos.copy(side = 'D', x = SIZE-1)
            'F' -> pos.copy(side = 'A', x = pos.y,   y = 0,             heading = 1)
            else -> throw IllegalStateException()
        }
    } else if (newPos.y == SIZE) {
        newPos = when (pos.side) {
            'A' -> pos.copy(side = 'C', y = 0)
            'B' -> pos.copy(side = 'C', y = pos.x,   x = SIZE-1,        heading = 2)
            'C' -> pos.copy(side = 'E', y = 0)
            'D' -> pos.copy(side = 'F', y = 0)
            'E' -> pos.copy(side = 'F', y = pos.x,   x = SIZE-1,        heading = 2)
            'F' -> pos.copy(side = 'B', y = 0)
            else -> throw IllegalStateException()
        }
    } else if (newPos.y == -1) {
        newPos = when (pos.side) {
            'A' -> pos.copy(side = 'F', y = pos.x,   x = 0,             heading = 0)
            'B' -> pos.copy(side = 'F', y = SIZE-1)
            'C' -> pos.copy(side = 'A', y = SIZE-1)
            'D' -> pos.copy(side = 'C', y = pos.x,   x=0,               heading = 0)
            'E' -> pos.copy(side = 'C', y = SIZE-1)
            'F' -> pos.copy(side = 'D', y = SIZE-1)
            else -> throw IllegalStateException()
        }
    }

    val (x, y) = translate(newPos)
    return if (all[y][x] == '#')
        pos
    else
        newPos
}

fun printMatrix(a: Matrix) {
    for (l in a)
        println(l.joinToString(""))
}

fun matrixAt(matrix: Matrix, x: Int, y: Int): Matrix =
    Array(50) { i -> CharArray(50) { j -> matrix[i+y][j+x] } }
