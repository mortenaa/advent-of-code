package no.morten.advent.day16

import no.morten.advent.util.readResourceFile
import java.lang.IllegalArgumentException
import java.lang.IllegalStateException

fun main() {
    //val inputFile = "day16_test.txt"
    val inputFile = "day16.txt"
    val input = readResourceFile(inputFile)
    val caves = parse(input)
    //println(caves)

    val edges = emptyMap<String, List<Edge>>().toMutableMap()
    val vertices = emptyMap<String, Vertex>().toMutableMap()

    val dis = emptyMap<Pair<String, String>, Int>().toMutableMap()

    val flowRooms = caves.values.filter { it.flow > 0 }.map { it.name }

    fun nextOpen(room: String, remainingTime: Int, open: List<String>): List<String> {
        return flowRooms.filter { it !in open }.filter { remainingTime - dis[room to it]!! - 1 > 0 }
    }

    fun totalPressuse(valves: List<String>): Int {
        var time = 30
        var pressure = 0
        var prev = valves.first()
        valves.drop(1).forEach {
            time -= (dis[prev to it]!! + 1)
            if (time > 0) {
                val p = caves[it]!!.flow * time
                //println("$it: $p")
                pressure += p
            }
            prev = it
        }
        return pressure
    }

    for (i in flowRooms + caves["AA"]!!.name) {
        for (j in flowRooms) {
            if (i != j) {
                val distance = shortestPath(caves, i, j)
                dis[i to j] = distance
                println("$i -> $j: $distance")
            }
        }
    }

    //println(nextOpen("AA", 30, emptyList()))
    //println(totalPressuse(listOf("AA", "DD", "BB", "JJ", "HH", "EE", "CC")))

//    val queue = ArrayDeque<State>()
//    queue.addLast(State(listOf("AA"), 30, emptyList()))
//    var max = 0
//    while (queue.isNotEmpty()) {
//        val s = queue.removeFirst()
//        val t = s.remainingTime
//        val next = nextOpen(s.path.last(), t, s.opened)
//        if (next.isEmpty()) {
//            val p = totalPressuse(s.path)
//            if (p > max) {
//                max = p
//                println("${s.path}: $max")
//            }
//        } else {
//            for (n in next) {
//                queue.addLast(State(
//                    path = s.path + n,
//                    remainingTime = s.remainingTime - dis[s.path.last() to n]!! - 1,
//                    opened = s.opened + n))
//            }
//        }
//    }
//    println(max)

    // Part 2

    val queue2 = ArrayDeque<State2>()
    queue2.addLast(State2(listOf(listOf("AA"), listOf("AA")), listOf(26, 26), emptyList(), 0))
    var max2 = 0
    var path1 = emptyList<String>()
    var path2 = emptyList<String>()
    while (queue2.isNotEmpty()) {
        val s = queue2.removeFirst()
        val t = s.remainingTime
        val i = if (t[0] > t[1]) 0 else 1
        val j = if (i == 0) 1 else 0

        val p = totalPressuse(s.path[0]) + totalPressuse(s.path[1])
        if (s.pressure > max2) {
            max2 = s.pressure
            println("${s.path}: $max2")
            path1 = s.path[0]
            path2 = s.path[1]
        }

        val next = nextOpen(s.path[i].last(), t[i], s.opened)
        if (s.opened.size == flowRooms.size) {
            val p = totalPressuse(s.path[0]) + totalPressuse(s.path[1])
            if (s.pressure > max2) {
                max2 = s.pressure
                println("${s.path}: $max2")
                path1 = s.path[0]
                path2 = s.path[1]
            }
        } else {
            for (n in next) {
                if (i == 0) {
                    val rem = s.remainingTime[0] - dis[s.path[0].last() to n]!! - 1
                    queue2.addLast(
                        State2(
                            path = listOf(s.path[0] + n, s.path[1]),
                            remainingTime = listOf(rem, s.remainingTime[1]),
                            opened = s.opened + n,
                            pressure = s.pressure + rem * caves[n]!!.flow
                        )
                    )
                } else {
                    val rem = s.remainingTime[1] - dis[s.path[1].last() to n]!! - 1
                    queue2.addLast(
                        State2(
                            path = listOf(s.path[0], s.path[1] + n),
                            remainingTime = listOf(s.remainingTime[0], rem),
                            opened = s.opened + n,
                            pressure = s.pressure + rem * caves[n]!!.flow
                        )
                    )
                }
            }
        }
    }

    println(max2)
    println(path1)
    println(path2)

}

data class State(val path: List<String>, val remainingTime: Int, val opened: List<String>)
data class State2(val path: List<List<String>>, val remainingTime: List<Int>, val opened: List<String>, val pressure: Int)


/*
 1  procedure BFS(G, root) is
 2      let Q be a queue
 3      label root as explored
 4      Q.enqueue(root)
 5      while Q is not empty do
 6          v := Q.dequeue()
 7          if v is the goal then
 8              return v
 9          for all edges from v to w in G.adjacentEdges(v) do
10              if w is not labeled as explored then
11                  label w as explored
12                  w.parent := v
13                  Q.enqueue(w)
 */
fun shortestPath(caves: Map<Name, Room>, from: Name, to: Name): Int {
    val queue = ArrayDeque<List<Name>>()
    val visited = HashSet<Name>().toMutableSet()
    visited.add(from)
    queue.addLast(listOf(from))
    while (queue.isNotEmpty()) {
        val vPath = queue.removeFirst()
        val v = vPath.last()
        if (v == to) {
            //println(vPath)
            return vPath.size - 1
        }
        for (edge in caves[v]!!.tunnels.filter { it !in visited }) {
            visited.add(edge)
            queue.addLast(vPath + edge)
        }
    }
    throw IllegalStateException()
}

data class Edge(val from: String, val to: String, val cost: Int)
data class Vertex(val id: String, val flow: Int)

typealias Name = String
typealias Tunnel = String
typealias FlowRate = Int
data class Room(val name: Name, val flow: FlowRate, val tunnels: List<Tunnel>)

fun parse(input: String): Map<Name, Room> {
    val regex = "Valve (\\w+) has flow rate=(\\d+); tunnels? leads? to valves? ([\\w, ]+)".toRegex()

    return input.lines().map {
        val groups = regex.find(it)?.groupValues ?: throw IllegalArgumentException()
        Room(
           name = groups[1],
           flow = groups[2].toInt(),
           tunnels = groups[3].split(", ")
       )
   }.associateBy { it.name }
}