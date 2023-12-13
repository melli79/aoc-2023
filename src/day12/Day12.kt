package day12

import println
import readInput

typealias Task = Pair<String, List<Int>>

private fun part1(input :List<String>) :Long {
    val tasks = input.map { line ->
        val (mask, constraintString) = line.split("\\s+".toRegex())
        val constraints = constraintString.split(',').map { it.toInt() }
        Pair(mask, constraints)
    }
    val result = tasks.sumOf { countOptions(it) }
    println("the maximum number of unknown positions per line is $maxOpen.")
    return result
}

var maxOpen = 0

fun countOptions(task :Task) :Long {
    val numVars = task.first.count { it=='?' }
    if (numVars> maxOpen)
        maxOpen = numVars
    return if (numVars<15)
        countSomeOptions(task)
    else {
        println("combining $numVars options...")
        countManyOptions(task.first, task.second)
    }
}

private fun countSomeOptions(task :Task) :Long {
    val opens = task.first.mapIndexed { pos, c :Char -> Pair(pos, c) }
        .filter { (_, c) -> c=='?' }.map { (pos, _) -> pos }.toTypedArray()
    var count = 0L
    for (pattern in 0uL..<(1uL shl opens.size)) {
        val option = task.first.toCharArray()
        for (i in opens.indices) {
            option[opens[i]] = if ((pattern and (1uL shl i)) != 0uL) '#' else '.'
        }
        val groups = option.joinToString("").trim('.')
            .split("\\.+".toRegex())
            .map { it.length }
        if (groups == task.second)
            count++
    }
    return count
}

// following Ilya's idea of dynamic programming
private fun countManyOptions(pattern :String, groups :List<Int>) :Long {
    val mask = createMask(groups)
    val n = pattern.length + 1
    val m = mask.size
    //       pattern index     mask index
    val dpPt = Array(n) { LongArray(m) { 0L } }
    val dpSh = Array(n) { LongArray(m) { 0L } }

    dpPt[0][0] = 1L // there is a unique empty pattern solution
    dpSh[0][0] = 1L
    for (i in 1..< n) {
        val s = pattern[i-1]
        if (s=='.' || s=='?') {
            Pair(dpPt, dpSh).copyWithPoint(i)
        } else { // appending a sharp gives no solution, b/c 0 means no sharps
            // start with 0
        }
        for (j in 1..< m) {
            when (s) {
                '.' -> Pair(dpPt, dpSh).considerPoint(mask[j], i, j)
                '#' -> Pair(dpPt, dpSh).considerSharp(mask[j], i, j)
                else -> {
                    Pair(dpPt, dpSh).considerPoint(mask[j], i, j)
                    Pair(dpPt, dpSh).considerSharp(mask[j], i, j)
                }
            }
        }
    }
    return dpPt.last().last()
}

// appending a dot gives a corresponding solution
private fun Pair<Array<LongArray>,Array<LongArray>>.copyWithPoint(i :Int) {
    first[i][0] = first[i-1][0]
    second[i][0] = second[i-1][0]
}

private fun Pair<Array<LongArray>,Array<LongArray>>.considerPoint(m :Boolean, i :Int, j :Int) {
    if (!m) {
        first[i][j] += first[i-1][j]
        second[i][j] += first[i-1][j]
    }
}

private fun Pair<Array<LongArray>,Array<LongArray>>.considerSharp(m :Boolean, i :Int, j :Int) {
    if (m) {
        first[i][j] += second[i-1][j-1]
        second[i][j] += second[i-1][j-1]
    } else {
        first[i][j] += second[i][j-1]
    }
}

private fun createMask(groups :List<Int>) =
    (listOf(false) + groups.flatMap { listOf(true).repeat(it.toUShort()) + listOf(false) })
        .toBooleanArray()

fun <E> List<E>.repeat(times :UShort) = (1..(times.toInt())).flatMap { this }

private fun part2(input :List<String>) :Long {
    val tasks = input.map { line ->
        val (mask, constraintString) = line.split("\\s+".toRegex())
        val constraints = constraintString.split(',').map { it.toInt() }
        Pair(mask+'?'+mask+'?'+mask+'?'+mask+'?'+mask, constraints+constraints+constraints+constraints+constraints)
    }
    val result = tasks.sumOf { countOptions(it) }
    println("the maximum number of unknown positions per line is $maxOpen.")
    return result
}

fun main() {
    val t1 = part1(readInput("day12/day12_test"))
    check(t1 == 21L) { "expected 21, but got $t1." }
    val input = readInput("day12/day12")
    part1(input).println()

    println("\nPart 2")
    val t2 = part2(readInput("day12/day12_test"))
    check(t2 == 525152L) { "expected 525152, but got $t2." }
    part2(input).println()
}
