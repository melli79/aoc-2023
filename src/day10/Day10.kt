package day10

import println
import readInput
import java.lang.System.out

typealias Grid<T> = List<Array<T>>
operator fun <T> Grid<T>.get(p :Point) = this[p.r][p.c]
operator fun <T> Grid<T>.set(p :Point, value :T) {
    this[p.r][p.c] = value
}

fun <T> Grid<T>.asGrid(transform :(T)->String ={ it.toString() })
    = this.joinToString("\n") { it.joinToString("", transform= transform) }

private fun part1(input :List<String>) :Int {
    val grid :Grid<Tile> = input.map { line -> line.map { Tile.of(it)!! }.toTypedArray() }
    println("Read Grid of size ${grid.size}x${grid[0].size}.")
    val start = grid.findStart()
    check(start!=null) { "Cannot find the start tile" }
    println("Starting at $start...")
    val distances = (grid.indices).map { (grid[0].indices).map { -1 }.toTypedArray() }
    var d = 0
    distances[start] = d
    var poss = Direction.entries.map { Pair(start, it) }
    while (poss.size>1) {
        d++
        print(d);  out.flush()
        poss = poss.map { (p :Point, dir) ->
            val newPos = p+dir
            if (grid.isValidPosition(newPos) && distances[newPos]<0) {
                val d1 = grid[newPos].turn(dir)
                if (d1!=null) {
                    distances[newPos] = d
                    Pair(newPos, d1)
                } else
                    null
            } else
                null
        }.filterNotNull()
    }
    println()
    return d
}

private fun Grid<Tile>.findStart() = mapIndexed { r :Int, line ->
    val c = line.indexOfFirst { it == Tile.Start }
    if (c >= 0)
        Point(r, c)
    else
        null
}.filterNotNull().firstOrNull()

private fun <T> List<Array<T>>.isValidPosition(p :Point) =
    p.r in indices && p.c in this[0].indices

private fun part2(input :List<String>) :Int? {
    TODO("unimplemented")
}

fun main() {
    val t1 = part1(readInput("day10/day10_test1"))
    check(t1 == 4) { "expected 4, but got $t1." }
    val t2 = part1(readInput("day10/day10_test2"))
    check(t2 == 8) { "expected 8, but got $t2." }
    val t3 = part1(readInput("day10/day10_test3"))
    check(t3 == 8) { "expected 8, but was $t3." }
    val input = readInput("day10/day10")
    part1(input).println()

    val tB = part2(readInput("day10/day10_test1"))
    check(tB == 5) { "expected 5, but got $tB." }
    part2(input).println()
}
