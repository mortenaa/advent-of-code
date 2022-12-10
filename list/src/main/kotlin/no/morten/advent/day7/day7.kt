package no.morten.advent.day7

import no.morten.advent.list.readResourceFile


fun parseData(filename: String) {

    var dir = emptyList<String>().toMutableList()
    var sizes = HashMap<String, Int>()

    readResourceFile(filename).lines().forEach {
        when {
            it == "$ cd /" -> { dir = listOf("/").toMutableList() }
            it == "$ cd .." -> { dir.removeLast() }
            it.startsWith("$ cd ") -> dir.add(it.split(" ")[2])
            it.startsWith("dir ") -> {}
            it == "$ ls" -> {}
            else -> {
                it.split(" ").let {
                    val size = it[0].toInt()
                    val name = it[1]
                    for (i in 0 until dir.size) {
                        val key = dir.subList(0, i+1).joinToString(",")
                        val value = sizes.getOrDefault(key, 0) + size
                        sizes.set(key, value)
                    }
                }
            }
        }
    }
    val part1 = sizes.filter { it.value <= 100000 }.values.sum()

    val available = 70000000
    val needed = 30000000
    val used = sizes.get("/")!!
    val free = available - used

    val part2 = sizes.filter { free + it.value >= needed }.values.min()

    println(part1)
    println(part2)
}

fun main() {
    parseData("day7.txt")
}