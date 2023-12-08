package day02

import println
import readInput
import kotlin.math.max

private fun part1(input :List<String>) :Long {
    val rMax = 12;  val gMax = 13;  val bMax = 14
    return input.sumOf { line ->
        val tokens = line.split("[\\s:,;]+".toRegex())
        check(tokens.size >= 4)
        val it = tokens.iterator()
        check(it.next()=="Game")
        val number = it.next().toLong()
        val (r, g, b) = minimizeCubes(it)
        if (r<=rMax && g<=gMax && b<=bMax)
            number
        else
            0L
    }
}

private fun minimizeCubes(input :Iterator<String>) :Triple<Int, Int, Int> {
    var r = 0; var g = 0; var b = 0
    while (input.hasNext()) {
        val n = input.next().toInt()
        when (val color = input.next()) {
            "red" -> r = max(r, n)
            "green" -> g = max(g, n)
            "blue" -> b = max(b, n)
            else -> println("unknown color $color.")
        }
    }
    return Triple(r, g, b)
}

private fun part2(input :Collection<String>) :Long {
    return input.sumOf { line ->
        val tokens = line.split("[\\s:,;]+".toRegex())
        check(tokens.size >= 4)
        val it = tokens.iterator()
        check(it.next()=="Game")
        it.next()
        val (r, g, b) = minimizeCubes(it)
        r*g*b.toLong()
    }
}

fun main() {
    val t1 = part1(readInput("day02/day02_test"))
    check(t1 == 8L) { "expected 8, but was $t1." }
    val input = readInput("day02/day02")
    part1(input).println()  // wrong: 178,

    println("\nPart 2")
    val t2 = part2(readInput("day02/day02_test"))
    check(t2 == 2286L) { "expected 2286, but was $t2." }
    part2(input).println()
}
