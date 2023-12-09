package day09

import println
import readInput

class NSeq(val values :MutableList<Long>) {
    val isAllZeros = values.all { it==0L }
    var derived :NSeq? =null

    fun predict() :Long {
        if (isAllZeros) {
            values.add(0L)
            return 0L
        }
        if (derived==null)
            derived = NSeq(values.differentiate().toMutableList())
        val result = values.last() + derived!!.predict()
        values.add(result)
        return result
    }

    fun backPredict() :Long {
        if (isAllZeros) {
            return 0L
        }
        if (derived==null)
            derived = NSeq(values.differentiate().toMutableList())
        val result = values.first() - derived!!.backPredict()
        values.add(result)
        return result
    }
}

fun Iterable<Long>.differentiate() = windowed(2).map { (fir, sec) -> sec-fir }

private fun part1(input :List<String>) = input.sumOf { line ->
    val series = NSeq(line.split("\\s+".toRegex()).map { it.toLong() }.toMutableList())
    series.predict()
}

private fun part2(input :List<String>) = input.sumOf { line ->
    val series = NSeq(line.split("\\s+".toRegex()).map { it.toLong() }.toMutableList())
    series.backPredict()
}

fun main() {
    val t1 = part1(readInput("day09/day09_test"))
    check(t1 == 114L) { "expected 114, but got $t1." }
    val input = readInput("day09/day09")
    part1(input).println()

    println("\nPart 2")
    val t2 = part2(readInput("day09/day09_test"))
    check(t2 == 2L) { "expected 2, but got $t2." }
    part2(input).println()
}
