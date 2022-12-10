package no.morten.advent.day8

import no.morten.advent.list.readResourceFile

fun main() {
    val data = readResourceFile("day8.txt")

    val forrest = parseData(data)

    val vissible = emptySet<Pair<Int, Int>>().toMutableSet()

    forrest.forEachIndexed { rowIndex, row ->
        vissible.addAll(vissible(row).map { rowIndex to it })
    }

    for (colIndex in 0 until forrest.first().size) {
        val column = forrest.map { it[colIndex] }.toIntArray()
        vissible.addAll(vissible(column).map { it to colIndex })
    }

    println(vissible.size)

    println(visible(forrest[2][0],lookUp(forrest, 2 to 0)))
    println(visible(forrest[2][0],lookDown(forrest, 2 to 0)))
    println(visible(forrest[2][0],lookRight(forrest, 2 to 0)))
    println(visible(forrest[2][0],lookLeft(forrest, 2 to 0)))

    var max = 0
    for (row in 0 until forrest.size) {
        for (col in 0 until forrest.first().size) {
            val pos = row to col
            val v = visible(forrest[pos.first][pos.second], lookUp(forrest, pos)) *
                    visible(forrest[pos.first][pos.second], lookDown(forrest, pos)) *
                    visible(forrest[pos.first][pos.second], lookRight(forrest, pos)) *
                    visible(forrest[pos.first][pos.second], lookLeft(forrest, pos))
            if (v > max) {
                println(pos to v)
                println(lookUp(forrest, pos))
                println(lookDown(forrest, pos))
                println(lookLeft(forrest, pos))
                max = v
            }
        }
    }
}

fun visible(me: Int, sight: List<Int>): Int {
    var num = 0;
    for (i in 0 until sight.size) {
        num++
        if (sight[i] >= me)
            break
    }
    return num
}

fun lookRight(forrest: List<IntArray>, position: Pair<Int, Int>): List<Int> {
    return forrest[position.first].drop(position.second+1)
}

fun lookLeft(forrest: List<IntArray>, position: Pair<Int, Int>): List<Int> {
    return forrest[position.first].dropLast(forrest.first().size-position.second).reversed()
}

fun lookUp(forrest: List<IntArray>, position: Pair<Int, Int>): List<Int> {
    val view = emptyList<Int>().toMutableList()
    for (i in 0 until position.first)
        view.add(forrest[i][position.second])
    return view.reversed()
}

fun lookDown(forrest: List<IntArray>, position: Pair<Int, Int>): List<Int> {
    val view = emptyList<Int>().toMutableList()
    for (i in position.first+1 until forrest.size)
        view.add(forrest[i][position.second])
    return view
}

fun vissible(line: IntArray): Set<Int> {
    var highest = -1
    val visible = emptySet<Int>().toMutableSet()
    line.forEachIndexed { index, value ->
        if (value > highest) {
            highest = value
            visible.add(index)
        }
    }
    highest = -1
    line.reversed().forEachIndexed { index, value ->
        if (value > highest) {
            highest = value
            visible.add(line.size - 1 - index)
        }
    }
    return visible
}



fun parseData(data: String): List<IntArray> =
    data.lines().map { it.toCharArray().map { it.toString().toInt() }.toIntArray() }