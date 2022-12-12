package no.morten.advent.day12

import no.morten.advent.util.readResourceFile
import java.lang.Integer.MAX_VALUE

typealias Map = Array<IntArray>
typealias Position = Pair<Int, Int>
typealias Start = Position
typealias End = Position
typealias MapAndPoints = Triple<Map, Start, End>
typealias Distance = Int

const val INFINITY = MAX_VALUE

fun main() {
    //val inputFile = "day12_test.txt"
    val inputFile = "day12.txt"
    val input = readResourceFile(inputFile)
    val (map, start, end) = parseMap(input)
    printMap(map)
    println(start to end)
    shortestPath(map, start, end)
    shortestPathToFloor(map, end)
}

fun printMap(map: Map) {
    for (i in map) {
        for (j in i) {
            print(String.format("%03d ", j))
        }
        println()
    }
}

fun parseMap(input: String): MapAndPoints {
    var start = 0 to 0
    var end = 0 to 0
    val map = input.lines().mapIndexed { i, r ->
        r.mapIndexed { j, c ->
            when {
                c == 'S' -> { start = i to j; 0 }
                c == 'E' -> { end = i to j; 'z'.code - 'a'.code}
                else -> { c.code - 'a'.code}
            }
        }.toIntArray()
    }.toTypedArray()
    assert(map[start.first][start.second] == 0) { "wrong start level" }
    assert(map[end.first][end.second] == 0) { "wrong target level" }
    return MapAndPoints(map, start, end)
}

fun shortestPath(map: Map, start: Start, end: End) {

    val visited = HashSet<Position>()
    val distance = HashMap<Position, Distance>()
    val prev = HashMap<Position, Position>()
    val level = HashMap<Position, Int>()
    val height = map.size
    val width = map.first().size
    for (i in map.indices) {
        for (j in map[i].indices) {
            distance[i to j] = INFINITY
            level[i to j] = map[i][j]
        }
    }
    distance[start] = 0

    var current = start
    while (end !in visited) {
        val current = distance.filter { it.key !in visited }.toList().minBy { it.second }.first
        val nearest = buildList {
            if (current.first > 0) add(current.copy(first = current.first - 1))
            if (current.first < height - 1) add(current.copy(first = current.first + 1))
            if (current.second > 0) add(current.copy(second = current.second - 1))
            if (current.second < width - 1) add(current.copy(second = current.second + 1))
        }.filter { it !in visited }.filter { (level[it]!! - level[current]!!) <= 1}.sortedBy { distance[it]!! }

        visited.add(current)

        for (node in nearest) {
            val d = distance[current]!! + 1
            if (d < distance[node]!!) {
                distance[node] = d
                prev[node] = current
            }
        }
        println(current to nearest)
    }
    var n = end
    val path = buildList {
        while (n != start) {
            add(n)
            n = prev[n]!!
        }
        add(start)
    }
    println(path.reversed())
    println(path.size - 1)
}

fun shortestPathToFloor(map: Map, start: Position) {

    val visited = HashSet<Position>()
    val distance = HashMap<Position, Distance>()
    val prev = HashMap<Position, Position>()
    val level = HashMap<Position, Int>()
    val height = map.size
    val width = map.first().size
    for (i in map.indices) {
        for (j in map[i].indices) {
            distance[i to j] = INFINITY
            level[i to j] = map[i][j]
        }
    }
    distance[start] = 0

    var current = start
    var end: Position? = null
    loop@while (end == null) {
        val current = distance.filter { it.key !in visited }.toList().minBy { it.second }.first
        val nearest = buildList {
            if (current.first > 0) add(current.copy(first = current.first - 1))
            if (current.first < height - 1) add(current.copy(first = current.first + 1))
            if (current.second > 0) add(current.copy(second = current.second - 1))
            if (current.second < width - 1) add(current.copy(second = current.second + 1))
        }.filter { it !in visited }.filter { (level[current]!! - level[it]!!) <= 1}.sortedBy { distance[it]!! }

        visited.add(current)

        for (node in nearest) {
            val d = distance[current]!! + 1
            if (d < distance[node]!!) {
                distance[node] = d
                prev[node] = current
            }
        }
        end = nearest.find { level[it] == 0 }
        println(current to nearest)
    }
    var n = end
    val path = buildList {
        while (n != start) {
            add(n)
            n = prev[n]!!
        }
        add(start)
    }
    println(path.reversed())
    println(path.size - 1)
}

// 1  function Dijkstra(Graph, source):
// 2
// 3      for each vertex v in Graph.Vertices:
// 4          dist[v] ← INFINITY
// 5          prev[v] ← UNDEFINED
// 6          add v to Q
// 7      dist[source] ← 0
// 8
// 9      while Q is not empty:
//10          u ← vertex in Q with min dist[u]
//11          remove u from Q
//12
//13          for each neighbor v of u still in Q:
//14              alt ← dist[u] + Graph.Edges(u, v)
//15              if alt < dist[v]:
//16                  dist[v] ← alt
//17                  prev[v] ← u
//18
//19      return dist[], prev[]