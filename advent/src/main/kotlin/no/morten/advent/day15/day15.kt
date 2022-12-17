package no.morten.advent.day15

import no.morten.advent.util.readResourceFile
import java.lang.IllegalStateException
import kotlin.collections.HashMap
import kotlin.collections.HashSet
import kotlin.math.abs
import kotlin.math.min

typealias Point = Pair<Int, Int>

val Point.x: Int
    get() = first

val Point.y: Int
    get() = second

var minX = Int.MAX_VALUE
var minY = Int.MAX_VALUE
var maxX = Int.MIN_VALUE
var maxY = Int.MIN_VALUE

fun main() {
    //val inputFile = "day15_test.txt"
    val inputFile = "day15.txt"
    val input = readResourceFile(inputFile)
    val sensors = parse(input)

    //val map = HashSet<Point>().toMutableSet()
    //val beacons = HashSet<Point>().toMutableSet()
    //fill(map, Point(10, 10), 3)
    //println(map)

//    println(outline(0 to 0, 3))
//
//    sensors.forEach { (k, v) ->
//        val d = manhatten(k, v)
//        beacons.add(v)
//        //println("$k -> $v = $d")
//        //fill(map, k, d)
//    }
    //fill(map, Point(8,7), 9)
    // ####B######################
    // ####B######################
    var count = 0
    println("$minX .. $maxX")
    println("$minY .. $maxY")
    val distance = sensors.map { it.key to manhatten(it.key, it.value) }.toMap()
    // val y = 2000000

    val limit = 4000000
    var i = 0
    val canditates = buildSet<Point> {
        for (s in sensors) {
            i += 1
            println(i)
            addAll(outline(s.key, distance[s.key]!!)
                .filter { it.x in 0 .. limit && it.y in 0 .. limit }
                .filter { c ->
                    sensors.keys.none { s -> manhatten(c, s) <= distance[s]!! }
                })
        }
    }
    println("${canditates.size} candidates")
//    val list = canditates.filter { c ->
//        sensors.keys.none { s -> manhatten(c, s) <= distance[s]!! }
//    }
    println(canditates.map {it.x.toLong()*4000000L + it.y.toLong()})
    return
//
//    loop@for (y in 0..4000000) {
//         var x = 0
//         println(y)
//         while (x <= 4000000) {
//             //println(x)
//            if (x to y in beacons) {
//                x+=1
//                //print("B")
//            } else if (sensors.containsKey(x to y)) {
//                //print("S")
//                x += (distance[x to y]!!/2).coerceAtLeast(1)
//            } else if (sensors.any { distance[it.key]!! >= manhatten(it.key, x to y) }) {
//                //print("#")
//                x += 1
//            } else {
//                println(x*4000000 + y)
//                break@loop;
//                x +=1
//            }
//        }
//        //println()
//    }

    //println(count)
// Your handheld device indicates that the distress signal is coming from a beacon nearby. The distress beacon is not
// detected by any sensor, but the distress beacon must have x and y coordinates each no lower than 0 and no larger than 4000000.
//
// To isolate the distress beacon's signal, you need to determine its tuning frequency, which can be found by multiplying
// its x coordinate by 4000000 and then adding its y coordinate.
//
// In the example above, the search space is smaller: instead, the x and y coordinates can each be at most 20. With this
// reduced search area, there is only a single position that could have a beacon: x=14, y=11.
// The tuning frequency for this distress beacon is 56000011.
//
// Find the only possible position for the distress beacon. What is its tuning frequency?
}

fun fill(map: MutableSet<Point>, k: Pair<Int, Int>, d: Int) {
    for (i in 0 .. d) {
        for (j in 0 .. d-i) {
            map.add(Point(k.x-j, k.y-i))
            map.add(Point(k.x+j, k.y-i))
            map.add(Point(k.x-j, k.y+i))
            map.add(Point(k.x+j, k.y+i))
        }
    }
}


//const val width = 10
//const val height = 10
//
//val map =
//    BitSet(width * height)
//
//inline fun getBit(p: Point) =
//    map[p.y * width + p.x]
//
//inline fun setBit(p: Point) =
//    map.set(p.y * width + p.x)


// Sensor at x=2, y=18: closest beacon is at x=-2, y=15
// Sensor at x=9, y=16: closest beacon is at x=10, y=16
fun parse(input: String): HashMap<Point, Point> {
    val regex = "Sensor at x=(-?\\d+), y=(-?\\d+): closest beacon is at x=(-?\\d+), y=(-?\\d+)".toRegex()
    val sensors = HashMap<Point, Point>()
    input.lines().map {
        val (x1, y1, x2, y2) = regex.find(it)?.groupValues?.drop(1)?.map { it.toInt() } ?: throw IllegalStateException()
        sensors[Point(x1, y1)] = Point(x2, y2)
        val d = manhatten(Point(x1, y1), Point(x2, y2))
        if (x1-d < minX) minX = x1-d
        if (x1+d > maxX) maxX = x1+d
        if (y1-d < minY) minY = y1-d
        if (y1+d > maxY) maxY = y1+d
        //println("$x1,$y1 -> $x2,$y2 (${manhatten(Point(x1, y1), Point(x2, y2))})")
    }
    return sensors
}

//      o               0,-4
//     0*o          -1,-3  1,-3
//    0***o         -2,-2  2,-2          x = 0, 0
//   0*****o        -3,-1  3,-1          d = 3
//  0***x***o       -4,0   4,0
//   0*****o        -3,1   3,1
//    0***o         -2,2   2,2
//     0*o          -1,3   1,3
//      0               0,4

fun outline(p: Point, d: Int) =
    buildSet {
        for (i in 0..d + 1) {
            val j = d - i + 1
            add(p.x - i to p.y - j)
            add(p.x - i to p.y + j)
            add(p.x + i to p.y - j)
            add(p.x + i to p.y + j)
        }
    }

fun manhatten(p1: Point, p2: Point) =
    abs(p1.x - p2.x) + abs(p1.y - p2.y)

