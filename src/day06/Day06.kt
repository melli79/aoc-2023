package day06

import println
import readInput
import java.lang.System.out
import java.math.BigInteger

val a = 1
val v0 = 0
private fun part1(inputLines :List<String>) :BigInteger {
    val input = inputLines.iterator()
    val times = input.next().split("\\s+".toRegex()).drop(1).map { it.toInt() }
    println("times: $times")
    val thresholds = input.next().split("\\s+".toRegex()).drop(1).map { it.toLong() }
    return times.zip(thresholds).fold(BigInteger.ONE) { p :BigInteger, (t, m) ->
        var count = 0
        print("$t s race gets you above $m: ");  out.flush()
        for (s in (1..< t).reversed()) {
            val d = computeRacingDistance(s, t)
            if (m < d) {
                print("."); out.flush()
                count++
            }
        }
        println()
        p*count.toBigInteger()
    }
}

private fun computeRacingDistance(split :Int, time :Int) = (v0 + a * split).toLong() * (time - split).toLong()

private fun part2(inputLines :List<String>) :Int {
    val input = inputLines.iterator()
    val time = input.next().split("\\s+".toRegex()).drop(1).joinToString("").toInt()
    println("times: $time ms")
    val threshold = input.next().split("\\s+".toRegex()).drop(1).joinToString("").toLong()
    var m = 0
    val split = time/2
    check( computeRacingDistance(split, time) > threshold )
    var l = split
    while (m+1<l) {
        val mid = (m+l) /2
        val d = computeRacingDistance(mid, time)
        if (threshold<d)
            l = mid
        else
            m = mid
    }
    println("$time s race starts winning at $l ms.");
    var M = split;  var u = time
    while (M+1<u) {
        val mid = (M+u) / 2
        val d = computeRacingDistance(mid, time)
        if (threshold<d)
            M = mid
        else
            u = mid
    }
    println("you can win until split $M ms.")
    return M-m
}

fun main() {
    val t1 = part1(readInput("day06/day06_test"))
    check(t1 == 288.toBigInteger()) { "expexted: 288, but was $t1." }

    val input = readInput("day06/day06")
    part1(input).println()

    val t2 = part2(readInput("day06/day06_test"))
    check(t2 == 71503) { "expected: 940'200 but was $t2" }
    part2(input).println()
}
