package no.morten.advent.day2

import no.morten.advent.day2.Result
import java.util.ResourceBundle

enum class Move(val points: Int) {
    Rock(1),
    Paper(2),
    Scissor(3)
}

enum class Result { Lose, Draw, Win }

fun parseResult(result: String) =
    when (result) {
        "X" -> Result.Lose
        "Y" -> Result.Draw
        "Z" -> Result.Win
        else -> throw IllegalArgumentException()
    }

fun parseMove(move: String) =
    when (move) {
        "A" -> Move.Rock
        "B" -> Move.Paper
        "C" -> Move.Scissor
        else -> throw IllegalArgumentException()
    }

fun parseResponse(response: String) =
    when (response) {
        "X" -> Move.Rock
        "Y" -> Move.Paper
        "Z" -> Move.Scissor
        else -> throw IllegalArgumentException()
    }

fun decideResponseFromResult(move: Move, result: Result): Move =
    when (move to result) {
        Move.Paper to Result.Win -> Move.Scissor
        Move.Paper to Result.Lose -> Move.Rock
        Move.Paper to Result.Draw -> Move.Paper

        Move.Scissor to Result.Win -> Move.Rock
        Move.Scissor to Result.Lose -> Move.Paper
        Move.Scissor to Result.Draw -> Move.Scissor

        Move.Rock to Result.Win -> Move.Paper
        Move.Rock to Result.Lose -> Move.Scissor
        Move.Rock to Result.Draw -> Move.Rock

        else -> throw java.lang.IllegalStateException()
    }

fun scoreMove(strategy: Strategy): Int {
    return strategy.response.points + when (strategy.move to strategy.response) {
        Move.Rock to Move.Rock,
        Move.Paper to Move.Paper,
        Move.Scissor to Move.Scissor, -> 3
        Move.Rock to Move.Paper,
        Move.Paper to Move.Scissor,
        Move.Scissor to Move.Rock -> 6
        else -> 0
    }
}

fun score(strategy: List<Strategy>): Int =
    strategy.sumOf { scoreMove(it) }

data class Strategy(val move: Move, val response: Move)

fun parseStrategyData(data: String): List<Strategy> {
    return data.split("\n").map {
        it.split(" ").let {
            Strategy(parseMove(it[0]), parseResponse(it[1]))
        }
    }
}

fun parseStrategyData2(data: String): List<Strategy> {
    return data.split("\n").map {
        it.split(" ").let {
            Strategy(parseMove(it[0]), decideResponseFromResult(parseMove(it[0]), parseResult(it[1])))
        }
    }
}