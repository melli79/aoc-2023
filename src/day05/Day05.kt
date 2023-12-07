package day05

import println
import readInput
import kotlin.math.abs
import kotlin.math.min

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
            val value = (map[last.second] ?: 0) +last.second
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

private fun readMaps(input :Iterator<String>) :Map<Pair<String, String>, IntervalMap<Long>> {
    val result = mutableMapOf<Pair<String, String>, IntervalMap<Long>>()
    while (input.hasNext()) {
        val categories = input.next().split(' ')[0].split('-')
        val src = categories[0];  val tar = categories[2]
        println("Reading map ${src}2${tar}...")
        val map = IntervalMap<Long>()
        while (input.hasNext()) {
            val line = input.next()
            if (line.isBlank())
                break
            val triple = line.split(' ').map { it.toLong() }
            val (tstart, sstart, length) = triple
            check(length > 0L)
            map.put(ComparableRange(sstart..< (sstart+length)), tstart - sstart)
        }
        result[Pair(src, tar)] = map
    }
    return result
}

private fun part2(inputLines :List<String>) :Long {
    val input = inputLines.iterator()
    val seedData = input.next().split(' ', '\t', '\n').drop(1).map { it.toLong() }
    val seedIntervals = seedData.windowed(2,2)
        .map { (first, length) -> ComparableRange(first..< (first+length)) }
        .let { IntervalSet.ofComparable(it) }
    println("The ${seedIntervals.size} seeds are: $seedIntervals")
    check(input.next().isBlank())
    val maps = readMaps(input)
    val path :List<String> = maps.keys.toMap().findPath(SEED, LOCATION)
    check(path.first() == SEED)
    check(path.last() == LOCATION)
    val loc2details = groupDetails(seedIntervals, path, maps)
    return loc2details.keys.minOf { if (it.first*it.last>0L)  min(abs(it.first), abs(it.last)) else 0L }
}

private fun groupDetails(
    seedRanges :IntervalSet,
    path :List<String>,
    maps :Map<Pair<String, String>, Map<Long, Long>>
) :Map<ComparableRange, List<Pair<String, LongRange>>> {
    val loc2details = mutableMapOf<ComparableRange, List<Pair<String, LongRange>>>()
    for (seedRange in seedRanges) {
        var last = Pair(SEED, seedRange)
        val details = mutableListOf(last)
        path.windowed(2).forEach { (src, tar) ->
            val map = maps[Pair(src, tar)]
            checkNotNull(map)
            TODO("implement")
//            val values = map.tryMap(last.second)
//            if (values.size==1) {
//                val value = values.first()
//                if (tar == LOCATION) {
//                    loc2details[value] = details
//                    println("location $value: ${details.joinToString { "${it.first}=${it.second}" }}")
//                } else
//                    details.add(Pair(tar, value))
//            }            last = Pair(tar, value)
        }
    }
    return loc2details
}

fun main() {
    check(part1(readInput("Day05/day05_test")) == 35L)

    val input = readInput("Day05/day05")
    part1(input).println()

    check(part2(readInput("Day05/day05_test")) == 5L)
    part2(input).println()
}
