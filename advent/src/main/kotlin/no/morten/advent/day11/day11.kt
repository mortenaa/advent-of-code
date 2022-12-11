package no.morten.advent.day11

import no.morten.advent.util.readResourceFile

typealias Item = Long
typealias Id = Int

enum class Operator  { Multiply, Add }

sealed interface Argument {
    data class Value(val value: Long): Argument
    object Self: Argument { override fun toString() = "Self" }
}

data class Operation(
    val operator: Operator,
    val first: Argument,
    val second: Argument
) {
    fun eval(item: Item): Long =
        when (operator) {
            Operator.Add -> evalArgument(first, item) + evalArgument(second, item)
            Operator.Multiply -> evalArgument(first, item) * evalArgument(second, item)
        }

    private fun evalArgument(argument: Argument, item: Item): Long =
        when (argument) {
            is Argument.Self -> item
            is Argument.Value -> argument.value
        }
}

data class Test(
    val divisibleBy: Int,
    val ifTrue: Id,
    val ifFalse: Id
) {
    fun eval(level: Long): Boolean =
        level.mod(divisibleBy.toLong()) == 0.toLong()
}

data class Monkey(
    val id: Id,
    var items: MutableList<Item>,
    val operation: Operation,
    val test: Test)

fun String.parseArgument(): Argument =
    if (this == "old")
        Argument.Self
    else
        Argument.Value(this.toLong())

fun String.parseTest(trueInput: String, falseInput: String): Test {
    return Test(
        divisibleBy = this.substringAfter("by ").toInt(),
        ifTrue = trueInput.substringAfter("monkey ").toInt(),
        ifFalse = falseInput.substringAfter("monkey ").toInt()
    )
}

fun String.parseOperator(): Operator =
    when (this) {
        "*" -> Operator.Multiply
        "+" -> Operator.Add
        else -> throw IllegalArgumentException()
    }

fun String.parseOperation(): Operation {
    val parts = this.substringAfter("= ").split(" ")
    return Operation(
        operator = parts[1].parseOperator(),
        first = parts[0].parseArgument(),
        second = parts[2].parseArgument())
}

fun String.parseMonkey(): Monkey {
    val lines = this.split("\n")
    assert(lines.size == 6)
    val id: Id = lines[0].substringAfter(" ").substringBefore(":").toInt()
    val items: List<Item> = lines[1].substringAfter(": ").split(", ").map { it.toLong() }
    val operation: Operation = lines[2].parseOperation()
    val test: Test = lines[3].parseTest(lines[4], lines[5])
    return Monkey(
        id = id,
        items = items.toMutableList(),
        operation = operation,
        test = test
    )
}

fun parseMonkeys(input: String) =
    input.split("\n\n").map { it.parseMonkey() }

fun main() {

    val inputFile = "day11.txt"
    //val inputFile = "day11.txt"

    val data = readResourceFile(inputFile)
    val monkeys = parseMonkeys(data).associateBy { it.id }

    val activity: MutableMap<Id, Long> = emptyMap<Id, Long>().toMutableMap()

    val factor = monkeys.map { it.value.test.divisibleBy }.reduce { a, b -> a * b}

    println("Facotr: $factor")

    for (iteration in 1 .. 10000) {

        if (iteration % 100 == 0)
            println("Iteration: $iteration")

        monkeys.keys.sorted().forEach {
            val monkey = monkeys[it] ?: throw java.lang.IllegalStateException()

            for (item in monkey.items) {
                val level = monkey.operation.eval(item).mod(factor).toLong()
                val test = monkey.test.eval(level)
                val target = monkeys[if (test) monkey.test.ifTrue else monkey.test.ifFalse]!!
                target.items.add(level)
                activity.merge(monkey.id, 1) { old, _ -> old + 1 }
            }

            monkey.items = emptyList<Item>().toMutableList()
        }

    }

    val highest = activity.values.sortedDescending().subList(0, 2)
    println(highest)
    println(highest[0] * highest[1])


}