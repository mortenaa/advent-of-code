import no.morten.advent.util.readResourceFile
import kotlin.math.abs

val input = """
    R 4
    U 4
    L 3
    D 1
    R 4
    D 1
    L 5
    R 2
""".trimIndent()

val test2 = """
    R 5
    U 8
    L 8
    D 3
    R 17
    D 10
    L 25
    U 20
""".trimIndent()

enum class Move { R, L, U, D }

fun parseMoves(input: String): List<Move> =
    input.lines().flatMap {
        val m = it.split(" ")
        val move = Move.valueOf(m[0])
        val num = m[1].toInt()
        List(num) { move }
    }

fun doMoves(moves: List<Move>): Int {
    var head = (0 to 0)
    var tail = (0 to 0)
    val visited = setOf(tail).toMutableSet()

    for (move in moves) {
        head = when (move) {
            Move.U -> head.copy(second = head.second + 1)
            Move.R -> head.copy(first = head.first + 1)
            Move.D -> head.copy(second = head.second - 1)
            Move.L -> head.copy(first = head.first - 1)
        }
        if (abs(head.first - tail.first) > 1 || abs(head.second - tail.second) > 1) {
            tail = when (move) {
                Move.U -> head.copy(second = head.second - 1)
                Move.R -> head.copy(first = head.first - 1)
                Move.D -> head.copy(second = head.second + 1)
                Move.L -> head.copy(first = head.first + 1)
            }
        }
        visited.add(tail)
    }
    return visited.size
}

fun doMovesPart2(moves: List<Move>): Int {

    var rope = List(10) { 0 to 0}.toMutableList()
    val visited = setOf(rope.last()).toMutableSet()
    var num = 0
    for (move in moves) {
        rope[0] = when (move) {
            Move.U -> rope[0].copy(second = rope[0].second + 1)
            Move.R -> rope[0].copy(first = rope[0].first + 1)
            Move.D -> rope[0].copy(second = rope[0].second - 1)
            Move.L -> rope[0].copy(first = rope[0].first - 1)
        }
        for (i in 1 until 10) {
            var current = rope[i]
            val ahead = rope[i-1]

            if (abs(current.first - ahead.first) <=1 && abs(current.second - ahead.second) <= 1)
                continue

            if (current.first == ahead.first) {
                if (ahead.second > current.second) {
                    current = current.copy(second = current.second + 1)
                } else {
                    current = current.copy(second = current.second - 1)
                }
            } else if (current.second == ahead.second) {
                if (ahead.first > current.first) {
                    current = current.copy(first = current.first + 1)
                } else {
                    current = current.copy(first = current.first - 1)
                }
            } else if (ahead.first > current.first && ahead.second > current.second) {
                current = current.copy(first = current.first + 1, second = current.second + 1)
            } else if (ahead.first > current.first && ahead.second < current.second) {
                current = current.copy(first = current.first + 1, second = current.second - 1)
            } else if (ahead.first < current.first && ahead.second > current.second) {
                current = current.copy(first = current.first - 1, second = current.second + 1)
            } else if (ahead.first < current.first && ahead.second < current.second) {
                current = current.copy(first = current.first - 1, second = current.second - 1)
            }
            rope[i] = current
        }
        visited.add(rope.last())
        num+=1
        println("After move $num")
        draw(rope)
    }

    return visited.size

}

fun draw(rope: List<Pair<Int, Int>>): Unit {
    for (y in 4 downTo 0) {
        for (x in 0..5) {
            val index = rope.indexOf(x to y)
            val s = if (index == -1) "." else if (index == 0) "H" else index.toString()
            print(s)
        }
        println()
    }

}

fun main() {
    val moves = parseMoves(readResourceFile("day9.txt"))
    println(moves)
    val visited = doMoves(moves)
    println(visited)
    val part2 = doMovesPart2(moves)
    println(part2)
}