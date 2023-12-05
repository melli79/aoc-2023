package day05

import println
import readInput

const val SEED = "seed"
const val LOCATION = "location"

private fun part1(inputLines :List<String>) :Long {
    val input = inputLines.iterator()
    val seedInput = input.next().split(' ', '\t', '\n')
    check(seedInput[0]=="seeds:")
    val seeds = seedInput.drop(1).map { it.toLong() }
    println("The ${seeds.size} seeds are: ${seeds.joinToString()}")
    check(input.next().isBlank())
    val maps = readMaps(input)
    val path :List<String> = maps.keys.toMap().findPath(SEED, LOCATION)
    check(path.first() == SEED)
    check(path.last() == LOCATION)
    val loc2details = findDetails(seeds, path, maps)
    return loc2details.keys.min()
}

private fun findDetails(
    seeds :List<Long>,
    path :List<String>,
    maps :Map<Pair<String, String>, Map<Long, Long>>
) :Map<Long, List<Pair<String, Long>>> {
    val loc2details = mutableMapOf<Long, List<Pair<String, Long>>>()
    for (seed in seeds) {
        var last = Pair(SEED, seed)
        val details = mutableListOf(last)
        path.windowed(2).forEach { (src, tar) ->
            val map = maps[Pair(src, tar)]
            checkNotNull(map)
            val value = map[last.second] ?: last.second
            if (tar == LOCATION) {
                loc2details[value] = details
                println("location $value: ${details.joinToString { "${it.first}=${it.second}" }}")
            } else
                details.add(Pair(tar, value))
            last = Pair(tar, value)
        }
    }
    return loc2details
}

private fun <V> Map<V, V>.findPath(src :V, tar :V) :List<V> {
    val result = mutableListOf<V>()
    var step = src;  result.add(step)
    while (step!=tar) {
        val next = get(step)
        checkNotNull(next)
        result.add(next)
        step = next
    }
    return result
}

private fun readMaps(input :Iterator<String>) :Map<Pair<String, String>, Map<Long, Long>> {
    val result = mutableMapOf<Pair<String, String>, Map<Long, Long>>()
    while (input.hasNext()) {
        val categories = input.next().split(' ')[0].split('-')
        val src = categories[0];
        val tar = categories[2]
        println("Reading map ${src}2${tar}...")
        val map = mutableMapOf<Long, Long>()
        while (input.hasNext()) {
            val line = input.next()
            if (line.isBlank())
                break
            val triple = line.split(' ').map { it.toLong() }
            val (tstart, sstart, length) = triple
            check(length > 0L)
            (0L..< length).forEach { offset ->
                map[sstart + offset] = tstart + offset
            }
        }
        result[Pair(src, tar)] = map
    }
    return result
}

private fun part2(input :List<String>) :Int {
    TODO("unimplemented")
}

fun main() {
    check(part1(readInput("Day05/day05_test")) == 35L)

    val input = readInput("Day05/day05")
    part1(input).println()

    check(part2(readInput("Day05/day05_test2")) == 5)
    part2(input).println()
}
