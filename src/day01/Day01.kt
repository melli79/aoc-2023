package day01

import println
import readInput

private fun part1(input: List<String>): Int = input.sumOf { line ->
    val firstDigit = line.firstOrNull { c -> c.isDigit() }?.digitToInt()
    assert(firstDigit != null)
    val lastDigit = line.last { it.isDigit() }.digitToInt()
    10*firstDigit!! + lastDigit
}

val pattern = "[0-9]|one|two|three|four|five|six|seven|eight|nine".toPattern()
private fun part2(input: List<String>): Int = input.sumOf { line ->
    val results = pattern.matcher(line).results().toList()
    val first = results.firstOrNull()?.group()
    check(first!=null) { "line '$line' contained no digit" }
    val firstDigit = toDigit(first)
    val last = results.last().group()
    val lastDigit = toDigit(last)
    10*firstDigit + lastDigit
}

fun toDigit(input :String) :Int = if(input.length == 1)
        input[0].digitToInt()
    else when(input) {
        "one" -> 1
        "two" -> 2
        "three" -> 3
        "four" -> 4
        "five" -> 5
        "six" -> 6
        "seven" -> 7
        "eight" -> 8
        "nine" -> 9
        "zero", "null" -> 0
        else -> throw IllegalArgumentException("Input '$input' is not a digit")
    }

fun main() {

    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142) { "${part1(testInput)} is not 142" }

    val input = readInput("Day01")
    val result1 = part1(input)
    result1.println()

    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281) { "${part2(testInput2)} is not 281" }
    part2(input).println()
}
