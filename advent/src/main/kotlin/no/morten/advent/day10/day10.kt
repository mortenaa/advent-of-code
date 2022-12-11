package no.morten.advent.day10

import no.morten.advent.util.readResourceFile
import java.lang.IllegalArgumentException

sealed interface Instruction {
    object Noop : Instruction
    data class Addx(val v: Int) : Instruction
}

data class CpuState(val cycle: Int, val v: Int)

fun parseProgram(source: String): List<Instruction> {
    val instructions: List<Instruction> = source.lines().map {
        val parts = it.split(" ")
        when (parts[0]) {
            "noop" -> Instruction.Noop
            "addx" -> Instruction.Addx(parts[1].toInt())
            else -> throw IllegalArgumentException()
        }
    }
    return instructions
}

fun execute(prog: List<Instruction>): MutableList<CpuState> {
    var cycle = 0
    var x = 1
    val state = emptyList<CpuState>().toMutableList()
    prog.forEach {
        when (it) {
            is Instruction.Noop -> {
                cycle+=1
                state.add(CpuState(cycle, x))
            }
            is Instruction.Addx -> {
                cycle+=1
                state.add(CpuState(cycle, x))
                cycle+=1
                state.add(CpuState(cycle, x))
                x += it.v
            }
        }
    }
    cycle+=1
    state.add(CpuState(cycle, x))
    return state
}

fun main() {
    val source = readResourceFile("day10.txt");
    val prog = parseProgram(source)
    val state = execute(prog)
    val indexes = listOf(20, 60, 100, 140, 180, 220)
    indexes.forEach { println(state[it - 1]) }
    val sum = state.filter { it.cycle in indexes }.sumOf { it.cycle * it.v }
    println(sum)

    var p = 0
    for (cpu in state) {
        if (p in cpu.v -1 .. cpu.v +1) {
            print("#")
        } else {
            print(".")
        }
        p+=1
        if (p.mod(40) == 0) {
            println()
            p = 0
        }
    }
}