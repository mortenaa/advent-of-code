package no.morten.advent.day19

import no.morten.advent.day16.Room
import no.morten.advent.util.readResourceFile
import java.lang.IllegalStateException


fun main() {
    val inputFile = "day19_test.txt"
    //val inputFile = "day19.txt"
    val input = readResourceFile(inputFile)
    val bluePrints = parse(input)
    println(bluePrints)
    //println(eval(listOf(Step.Wait, Step.Wait, Step.BuildClayRobot, Step.Wait, Step.BuildClayRobot, Step.Wait, Step.BuildClayRobot, Step.Wait, Step.Wait, Step.Wait,
    //Step.BuildObsidianRobot, Step.BuildClayRobot, Step.Wait, Step.Wait, Step.BuildObsidianRobot, Step.Wait, Step.Wait, Step.BuildGeodeRobot, Step.Wait, Step.Wait,
    //Step.BuildGeodeRobot, Step.Wait, Step.Wait, Step.Wait), bluePrint = bluePrints[0]))

    var sum = 0
    for (blueprint in bluePrints) {
        val (strategy, geods) = findOptimalStrategy(blueprint)
        // val g = eval(strategy, blueprint)
        println("Blueprint ${blueprint.id}: $geods")
        sum += (geods * blueprint.id)
    }
    println("Total: $sum")


}

fun eval(strategy: Strategy, bluePrint: Blueprint): Int {
    // var (ore, clay, obsidian, geode) = List(4) { 0 }
    // var (oreRobots, clayRobots, obsidianRobots, geodeRobots) = listOf(1, 0, 0, 0)
    assert(strategy.size == 24)
    var state = State(0, 0, 0, 0, 1, 0, 0, 0, 24, emptyList())
    var m = 1
    for (step in strategy) {
        println("== Minute $m ==")
        var newState = applyStep(state, bluePrint, step)
        println("Step: $step")
        println("State: $newState")
        m += 1
    }
    return strategy.mapIndexed { i, s -> if (s == Step.BuildGeodeRobot) (24-i-1) else 0}.sum()

}

data class State(
    val ore: Int,
    val clay: Int,
    val obsidian: Int,
    val geode: Int,
    val oreRobots: Int,
    val clayRobots: Int,
    val obsidianRobots: Int,
    val geodeRobots: Int,
    val remainingTime: Int,
    val steps: Strategy
)

fun nextSteps(state: State, bluePrint: Blueprint): List<Step> {
    if (state.remainingTime < 1) return emptyList()
    return buildSet {
        var needObsidian = false
        var needOre = false
        var needClay = false

        if (state.obsidian < bluePrint.geodeRobot.costObsidian) {
            needObsidian = true
        }
        if (state.ore < bluePrint.geodeRobot.costOre) {
            needOre = true
        }
        if (!needOre && !needObsidian){
            add(Step.BuildGeodeRobot)
        }

        if (needObsidian) {
            if (state.clay < bluePrint.obsidianRobot.costClay) {
                needClay = true
            }
            if (state.ore < bluePrint.obsidianRobot.costOre) {
                needOre = true
            }
            if (!needClay && !needOre) {
                add(Step.BuildObsidianRobot)
            }
        }

        if (needClay) {
            if (state.ore < bluePrint.clayRobot.costOre) {
                needOre = true
            } else {
                assert(state.ore >= bluePrint.clayRobot.costOre)
                add(Step.BuildClayRobot)
            }
        }

        if (needOre) {
            if (state.ore < bluePrint.oreRobot.costOre) {
                add(Step.Wait)
            } else {
                assert(state.ore >= bluePrint.oreRobot.costOre)
                add(Step.BuildOreRobot)
            }
        }

        add(Step.Wait)

    }.toList()
}

fun applyStep(state: State, bluePrint: Blueprint, step: Step): State {
    var (ore, clay, obsidian, geode) =
        listOf(state.oreRobots, state.clayRobots, state.obsidianRobots, state.geodeRobots)
    var (oreRobots, clayRobots, obsidianRobots, geodeRobots) = listOf(state.oreRobots, state.clayRobots, state.obsidianRobots, state.geodeRobots)
    when (step) {
        Step.BuildOreRobot -> {
            assert(state.ore + ore >= bluePrint.oreRobot.costOre)
            ore -= bluePrint.oreRobot.costOre
            oreRobots += 1
        }
        Step.BuildClayRobot -> {
            assert(state.ore + ore >= bluePrint.clayRobot.costOre)
            ore -= bluePrint.clayRobot.costOre
            clayRobots += 1
        }
        Step.BuildObsidianRobot -> {
            assert(state.ore + ore >= bluePrint.obsidianRobot.costOre)
            assert(state.clay + clay >= bluePrint.obsidianRobot.costClay)
            ore -= bluePrint.obsidianRobot.costOre
            clay -= bluePrint.obsidianRobot.costClay
            obsidianRobots += 1
        }
        Step.BuildGeodeRobot -> {
            assert(state.ore + ore >= bluePrint.geodeRobot.costOre)
            assert(state.obsidian + obsidian >= bluePrint.geodeRobot.costObsidian)
            ore -= bluePrint.geodeRobot.costOre
            obsidian -= bluePrint.geodeRobot.costObsidian
            geodeRobots += 1
        }
        Step.Wait -> { }
    }
    return state.copy(
        ore = state.ore + ore,
        clay = state.clay + clay,
        obsidian = state.obsidian + obsidian,
        geode = state.geode + geode,
        oreRobots = oreRobots,
        clayRobots = clayRobots,
        obsidianRobots = obsidianRobots,
        geodeRobots = geodeRobots,
        remainingTime = state.remainingTime - 1,
        steps = state.steps + step
    ).also {
        assert(it.ore >= 0)
        assert(it.clay >= 0)
        assert(it.obsidian >= 0)
    }
}

fun findOptimalStrategy(bluePrint: Blueprint): Pair<Strategy, Int> {
    var current = State(
        ore = 0, clay = 0, obsidian = 0, geode = 0,
        oreRobots = 1, clayRobots = 0, obsidianRobots = 0, geodeRobots = 0,
        remainingTime = 24, steps = emptyList())
    var optimal = current
    var max = 0
    val queue = ArrayDeque<State>()
    val seen = setOf<Strategy>().toMutableSet()
    seen.add(current.steps)
    queue.addLast(current)
    while (queue.isNotEmpty()) {
        current = queue.removeFirst()
        //println("current: $current")
        if (current.remainingTime == 0) {
            if (current.geode > max) {
                max = current.geode
                optimal = current
                println("New optimal: ${current.steps}")
            }
        } else {
            for (step in nextSteps(current, bluePrint)) {
                val newState = applyStep(current, bluePrint, step)
                if (newState.steps !in seen) {
                    queue.addLast(applyStep(current, bluePrint, step))
                    seen.add(newState.steps)
                }
            }
        }
    }
    return optimal.steps to optimal.geode
}

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

/*
Blueprint 1:
  Each ore robot costs 4 ore.
  Each clay robot costs 2 ore.
  Each obsidian robot costs 3 ore and 14 clay.
  Each geode robot costs 2 ore and 7 obsidian.
 */
fun parse(input: String): List<Blueprint> {
    var regexp = "Blueprint (\\d+):\\s+Each ore robot costs (\\d+) ore.\\s+Each clay robot costs (\\d+) ore.\\s+Each obsidian robot costs (\\d+) ore and (\\d+) clay.\\s+Each geode robot costs (\\d+) ore and (\\d+) obsidian.".toRegex()
    val matches = regexp.findAll(input)
    println(matches.count())
    return matches.map {
        val (id, oreRobotOre, clayRobotOre, obsidianRobotOre, obsidianRobotClay, geodeRobotOre, geodeRobotObsidian) = it.destructured
        Blueprint(
            id = id.toInt(),
            oreRobot = OreRobot(oreRobotOre.toInt()),
            clayRobot = ClayRobot(clayRobotOre.toInt()),
            obsidianRobot = ObsidianRobot(obsidianRobotOre.toInt(), obsidianRobotClay.toInt()),
            geodeRobot = GeodeRobot(geodeRobotOre.toInt(), geodeRobotObsidian.toInt())
        )
    }.toList()
}


data class OreRobot(val costOre: Int)
data class ClayRobot(val costOre: Int)
data class ObsidianRobot(val costOre: Int, val costClay: Int)
data class GeodeRobot(val costOre: Int, val costObsidian: Int)
data class Blueprint(
    val id:Int,
    val oreRobot: OreRobot,
    val clayRobot: ClayRobot,
    val obsidianRobot: ObsidianRobot,
    val geodeRobot: GeodeRobot)
enum class Step { Wait, BuildOreRobot, BuildClayRobot, BuildObsidianRobot, BuildGeodeRobot }
typealias Strategy = List<Step>
