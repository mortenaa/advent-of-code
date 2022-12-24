package no.morten.advent.day23

import no.morten.advent.util.readResourceFile

/*
    ....#..
    ..###.#
    #...#.#
    .#...##
    #.###..
    ##.#.##
    .#..#..
 */

enum class Direction { North, South, West, East }
inline fun Direction.next() = when (this) {
    Direction.North -> Direction.South
    Direction.South -> Direction.West
    Direction.West -> Direction.East
    Direction.East -> Direction.North
}

fun main() {
    //val inputFile = "day23_test.txt"
    //val inputFile = "day23_test2.txt"
    val inputFile = "day23.txt"
    val input = readResourceFile(inputFile)
    val map = buildSet {
        input.lines().withIndex().forEach { line ->
            line.value.withIndex().forEach { char ->
                if (char.value == '#')
                    add(line.index to char.index)
            }
        }
    }.toMutableSet()

    var direction = Direction.North

    var proposedMoves: MutableMap<Pair<Int, Int>, List<Pair<Int, Int>>>
        = emptyMap<Pair<Int, Int>, List<Pair<Int, Int>>>().toMutableMap()

    printMap(map)
    var n = 0
    while (true) {
        n +=  1
        var moves = 0
        for (point in map) {
            if (!hasSpace(point, map)) {
                println("$point has no space")
                proposedMove(point, map, direction)?.let {
                    proposedMoves.compute(it) { _, v -> if (v == null) listOf(point) else v + point }
                    println("$point wants to move to $it")
                }
            }
        }
        proposedMoves.forEach { m ->
            if (m.value.size == 1) {
                map.remove(m.value.first())
                map.add(m.key)
                moves += 1
            }
        }
        proposedMoves.clear()
        direction = direction.next()

        if (moves == 0) {
            println("No moves in round $n")
            return
        }

    }
    println(map)
    val minX = map.minOf { it.second }
    val minY = map.minOf { it.first }
    val maxX = map.maxOf { it.second }
    val maxY = map.maxOf { it.first }

    println("${maxX-minX} to ${maxY-minY}")

    val empty = ((maxX-minX+1)*(maxY-minY+1) - map.size)
    println("empty: $empty")

}

fun printMap(map: Set<Pair<Int, Int>>) {
    val minX = map.minOf { it.second }
    val minY = map.minOf { it.first }
    val maxX = map.maxOf { it.second }
    val maxY = map.maxOf { it.first }
    for (i in minY .. maxY) {
        for (j in minX .. maxX) {
            if (i to j in map)
                print('#')
            else
                print('.')
        }
        println()
    }
}

fun proposedMove(point: Pair<Int, Int>, map: Set<Pair<Int, Int>>, direction: Direction): Pair<Int, Int>? {
    var m: Pair<Int, Int>? = null
    var d = direction
    repeat(4) {
        m = canMove(point, d, map)
        if (m != null) return m
        d = d.next()
    }
    return null
}

inline fun canMove(point: Pair<Int, Int>, direction: Direction, map: Set<Pair<Int, Int>>): Pair<Int, Int>? =
    when (direction) {
        Direction.North -> if (
                   point.first-1 to point.second !in map
                && point.first-1 to point.second+1 !in map
                && point.first-1 to point.second-1 !in map) point.first-1 to point.second else null
        Direction.South -> if (
                   point.first+1 to point.second !in map
                && point.first+1 to point.second+1 !in map
                && point.first+1 to point.second-1 !in map) point.first+1 to point.second else null
        Direction.West -> if (
                   point.first   to point.second-1 !in map
                && point.first-1 to point.second-1 !in map
                && point.first+1 to point.second-1 !in map) point.first to point.second-1 else null
        Direction.East -> if (
                   point.first   to point.second+1 !in map
                && point.first-1 to point.second+1 !in map
                && point.first+1 to point.second+1 !in map) point.first to point.second+1 else null
    }

inline fun hasSpace(point: Pair<Int, Int>, map: Set<Pair<Int, Int>>) =
               point.first + 1 to point.second     !in map
            && point.first - 1 to point.second     !in map
            && point.first     to point.second + 1 !in map
            && point.first     to point.second - 1 !in map

            && point.first + 1 to point.second + 1 !in map
            && point.first + 1 to point.second - 1 !in map
            && point.first - 1 to point.second + 1 !in map
            && point.first - 1 to point.second - 1 !in map

/*

During the first half of each round, each Elf considers the eight positions adjacent to themself.
If no other Elves are in one of those eight positions, the Elf does not do anything during this round.
Otherwise, the Elf looks in each of four directions in the following order and proposes moving one step
in the first valid direction:

If there is no Elf in the N, NE, or NW adjacent positions, the Elf proposes moving north one step.
If there is no Elf in the S, SE, or SW adjacent positions, the Elf proposes moving south one step.
If there is no Elf in the W, NW, or SW adjacent positions, the Elf proposes moving west one step.
If there is no Elf in the E, NE, or SE adjacent positions, the Elf proposes moving east one step.
After each Elf has had a chance to propose a move, the second half of the round can begin. Simultaneously,
each Elf moves to their proposed destination tile if they were the only Elf to propose moving to that position.
If two or more Elves propose moving to the same position, none of those Elves move.

Finally, at the end of the round, the first direction the Elves considered is moved to the end of the list
of directions. For example, during the second round, the Elves would try proposing a move to the south first,
then west, then east, then north. On the third round, the Elves would first consider west, then east, then north, then south.

 */