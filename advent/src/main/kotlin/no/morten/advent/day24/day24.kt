package no.morten.advent.day24

import no.morten.advent.util.readResourceFile

data class Point(val row: Int, val col: Int)
data class SearchState(val turn: Int, val path: List<Point>)
data class FilterState(val turn: Int, val point: Point)

fun main() {
    //val inputFile = "day24_test.txt"
    val inputFile = "day24.txt"
    val input = readResourceFile(inputFile)
    val height = input.lines().size - 2
    val width = input.lines().first().length - 2
    println("$width x $height")

    val leftBlizzards = emptySet<Point>().toMutableSet()
    val rightBlizzards = emptySet<Point>().toMutableSet()
    val upBlizzards = emptySet<Point>().toMutableSet()
    val downBlizzards = emptySet<Point>().toMutableSet()
    fun leftBlizzards(turn: Int) = leftBlizzards.map { it.copy(col = Math.floorMod(it.col - turn, width)) }
    fun rightBlizzards(turn: Int) = rightBlizzards.map { it.copy(col = Math.floorMod(it.col + turn, width)) }
    fun upBlizzards(turn: Int) = upBlizzards.map { it.copy(row = Math.floorMod(it.row - turn, height)) }
    fun downBlizzards(turn: Int) = downBlizzards.map { it.copy(row = Math.floorMod(it.row + turn, height)) }

    var start = Point(0, 0)
    var end = Point(0, 0)



    input.lines().withIndex().forEach { line ->
        line.value.withIndex().forEach { char ->
            when (char.value) {
                '<' -> leftBlizzards.add(Point(row = line.index-1, col = char.index-1))
                '>' -> rightBlizzards.add(Point(row = line.index-1, col = char.index-1))
                '^' -> upBlizzards.add(Point(row = line.index-1, col = char.index-1))
                'v' -> downBlizzards.add(Point(row = line.index-1, col = char.index-1))
                '.' -> {
                    if (line.index == 0)
                        start = Point(row = line.index - 1, col = char.index - 1)
                    else if (line.index == height+1)
                        end = Point(row = line.index-1, col = char.index-1)
                    }
                }
            }
        }
    println("Start: $start")
    println("End: $end")


    fun printMap(pos: Point, turn: Int) {
        for (i in -1 .. height) {
            for (j in -1 .. width) {
                var c = '.'
                var n = 0
                var p = Point(row = i, col = j)
                if (p in leftBlizzards(turn)) { c = '<'; n += 1 }
                if (p in rightBlizzards(turn)) { c = '>'; n += 1 }
                if (p in upBlizzards(turn)) { c = '^'; n += 1 }
                if (p in downBlizzards(turn)) { c = 'v'; n += 1 }
                if (n > 1) c = n.toString().last()
                if (i == -1) c = '#'
                if (i == height) c = '#'
                if (j == -1) c = '#'
                if (j == width) c = '#'
                if (i == start.row && j == start.col) c = '.'
                if (i == end.row && j == end.col) c = '.'
                if (i == pos.row && j == pos.col) c = 'E'
                print(c)
            }
            println()
        }
    }

    fun Point.down(turn: Int): Point? {
        val p = this.copy(row = this.row + 1)
        if (p.row >= height)
            return if (p == end)
                p
            else null
        return if (p !in upBlizzards(turn) && p !in leftBlizzards(turn) && p !in rightBlizzards(turn))
            p
        else null
    }

    fun Point.up(turn: Int): Point? {
        val p = this.copy(row = this.row - 1)
        if (p.row <0)
            return if (p == start)
                p
            else return null
        return if (p !in downBlizzards(turn) && p !in leftBlizzards(turn) && p !in rightBlizzards(turn))
            p
        else null
    }

    fun Point.right(turn: Int): Point? {
        if (this.row == -1) return null
        val p = this.copy(col = this.col + 1)
        if (p.col >= width)
            return null
        return if (p !in downBlizzards(turn) && p !in leftBlizzards(turn) && p !in upBlizzards(turn))
            p
        else null
    }

    fun Point.left(turn: Int): Point? {
        if (this.row == height) return null
        val p = this.copy(col = this.col - 1)
        if (p.col == -1)
            return null
        return if (p !in downBlizzards(turn) && p !in rightBlizzards(turn) && p !in upBlizzards(turn))
            p
        else null
    }

    fun Point.stay(turn: Int): Point? {
        return if (this !in downBlizzards(turn) && this !in rightBlizzards(turn) && this !in upBlizzards(turn) && this !in leftBlizzards(turn))
            this.copy()
        else null
    }

    fun paths(current: SearchState): List<SearchState> {
        val p = current.path.last()
        val t = current.turn + 1
        return listOf(p.down(t), p.right(t), p.left(t), p.stay(t), p.up(t)).filterNotNull().map {
            current.copy(turn = t, path = current.path + it)
        }
    }

    printMap(start, 0)

    /*
 9      while Q is not empty:
10          u ← vertex in Q with min dist[u]
11          remove u from Q
12
13          for each neighbor v of u still in Q:
14              alt ← dist[u] + Graph.Edges(u, v)
15              if alt < dist[v]:
16                  dist[v] ← alt
17                  prev[v] ← u
18
19      return dist[], prev[]
     */
    fun search(from: Point, to: Point, turn: Int): SearchState {
        val seen = emptySet<FilterState>().toMutableSet()
        val queue = ArrayDeque<SearchState>()
        queue.addLast(SearchState(turn, listOf(from)))
        var shortest = SearchState(Int.MAX_VALUE, emptyList())
        var current: SearchState
        while (queue.isNotEmpty()) {
            current = queue.removeFirst()
            current.path.last().let {
                if (it.row < 0) println("outside: $it")
                if (it.col < 0) println("outside: $it")
                if (it.row >= height) println("outside: $it")
                if (it.col >= width) println("outside: $it")
            }
            //seen.add(FilterState(current.turn, current.path.last()))
//        if (current.turn > longest) {
//            println("longest step: ${current.turn}, queue size: ${queue.size}")
//            longest = current.turn
//        }
            if (current.path.lastOrNull() == to && current.turn < shortest.turn) {
                shortest = current
                println("Shortest: $shortest")
            }
            if (current.turn < shortest.turn)
                paths(current).filter { FilterState(it.turn, it.path.last()) !in seen }.forEach {
                    queue.addLast(it)
                    seen.add(FilterState(it.turn, it.path.last()))
                }
        }
        return shortest
    }

    val s = search(start, end, 0)
    val t = search(end, start, s.turn)
    val u = search(start, end, t.turn)
    println(s)
    println(t)
    println(u)
//    for (s in shortest.path.withIndex()) {
//        println("Minute ${s.index}")
//        printMap(s.value, s.index)
//    }

}


