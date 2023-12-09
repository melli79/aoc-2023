package day03

import println
import readInput
import kotlin.math.max
import kotlin.math.min

private fun part1(input :List<String>) :Long {
    val grid = input.map { line -> line.replace('.', ' ') }
    val numbers :List<List<Triple<Int, Int, Long>>> = grid.map { findNumbers(it) }
    val markedNumbers = grid.flatMapIndexed { r :Int, line ->
        line.mapIndexed { c :Int, ch -> Pair(c, ch) }
            .filter { it.second!=' ' && !it.second.isDigit() }
            .flatMap { (c :Int, _) -> (max(0, r-1)..min(numbers.size-1, r+1))
                .flatMap { r1 ->
                    numbers[r1].filter { (b,e,_) -> c in (b-1)..(e+1) }
                        .map { (c,_,n) -> Triple(r1,c,n) }
                }
            }
    }.toSet()
    return markedNumbers.sumOf { it.third }
}

fun findNumbers(line :String) :List<Triple<Int, Int, Long>> {
    val result = mutableListOf<Triple<Int, Int, Long>>()
    var start = -1
    for (pos in line.indices) {
        if (line[pos].isDigit()) {
            if (start<0)
                start = pos
        } else if (start>=0) {
            result.add(Triple(start, pos-1, line.slice(start..< pos).toLong()))
            start = -1
        }
    }
    if (start>=0)
        result.add(Triple(start, line.length -1, line.slice(start..< line.length).toLong()))
    return result
}

private fun part2(input :List<String>) :Long {
    val grid = input.map { line -> line.replace('.', ' ') }
    val numbers :List<List<Triple<Int, Int, Long>>> = grid.map { findNumbers(it) }
    val gears :List<List<Triple<Int, Int, Long>>> = grid.flatMapIndexed { r :Int, line :String ->
        val shafts = line.mapIndexed { c :Int, ch -> Pair(c, ch) }.filter { (_, ch) -> ch=='*' }
        shafts.map { (c :Int,_) -> (max(0,r-1)..min(numbers.size-1, r+1))
            .flatMap { r1 ->
                numbers[r1].filter { (b,e,_) -> c in (b-1)..(e+1) }
                    .map { (b,_, n :Long) -> Triple(r1, b, n) }
            }

        }.filter { it.size==2 }
    }
    return gears.sumOf { it.first().third * it.last().third }
}

fun main() {
    val t1 = part1(readInput("day03/day03_test"))
    check(t1 == 4361L) { "expected 4361, but got $t1." }
    val input = readInput("day03/day03")
    part1(input).println()

    println("\nPart 2")
    val ex2 = 467L*35 + 755L*598
    val t2 = part2(readInput("day03/day03_test"))
    check(t2 == ex2) { "expected $ex2, but got $t2." }
    part2(input).println()
}
