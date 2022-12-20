package no.morten.advent.day20

import no.morten.advent.util.readResourceFile
import java.util.*
import kotlin.collections.ArrayDeque

data class Value(val id: Int, val v: Long)

fun main() {
    //val inputFile = "day20_test.txt"
    val inputFile = "day20.txt"
    val input = readResourceFile(inputFile)
    val numbers = input.lines().mapIndexed { i, v -> Value(i, v.toLong()) }
    val num = numbers.size
    var list = numbers.toMutableList()
    println(list.map { it.v }.joinToString())


    // a, b, c, old, d, e, new, f, g   -> (0, old) + (old + 1, new) + n + (new + 1, num)
    // a, b, new, c, d, old, e, f, g

    for (n in numbers) {
        val oldIndex = list.indexOf(n)
        list.removeAt(oldIndex)
        var newIndex = Math.floorMod(oldIndex + n.v, numbers.size - 1)
        list.add(newIndex, n)

        println(list.map { it.v }.joinToString())
    }
    val start = list.indexOfFirst { it.v == 0L }

    val sum = list[(start + 1000) % num].v + list[(start + 2000) % num].v + list[(start + 3000) % num].v
    println(sum)

    val key = 811589153L
    val numbers2 = numbers.map { it.copy(v = it.v * key) }
    var list2 = numbers2.toMutableList()
    repeat(10) {
        for (n in numbers2) {
            val oldIndex = list2.indexOf(n)
            list2.removeAt(oldIndex)
            var newIndex = Math.floorMod(oldIndex + n.v, numbers2.size - 1)
            list2.add(newIndex, n)

            //println(list2.map { it.v }.joinToString())
        }
    }
    val start2 = list2.indexOfFirst { it.v == 0L }

    val sum2 = list2[(start2 + 1000) % num].v + list2[(start2 + 2000) % num].v + list2[(start2 + 3000) % num].v
    println(sum2)
    /*
        var oldIndex = indices[i]
        var newIndex = (oldIndex + numbers[i]) % num
        if (newIndex < 0)
            newIndex += num

        Initial arrangement:
        1, 2, -3, 3, -2, 0, 4

        1 moves between 2 and -3:
        2, 1, -3, 3, -2, 0, 4

        2 moves between -3 and 3:
        1, -3, 2, 3, -2, 0, 4

        -3 moves between -2 and 0:
        1, 2, 3, -2, -3, 0, 4

        3 moves between 0 and 4:
        1, 2, -2, -3, 0, 3, 4

        -2 moves between 4 and 1:
        1, 2, -3, 0, 3, 4, -2

        0 does not move:
        1, 2, -3, 0, 3, 4, -2

        4 moves between -3 and 0:
        1, 2, -3, 4, 0, 3, -2
     */

}