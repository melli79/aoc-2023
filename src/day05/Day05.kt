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

private fun part2(inputLines :List<String>) :Long? {
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
//    println(loc2details.intervalEntries.entries.joinToString("\n") { "${it.key}:  "+it.value.joinToString { (k,v) -> "$k: $v" }})
    return loc2details.keys.intervals.minOfOrNull {
        if (sgn(it.first)==sgn(it.last))
            min(abs(it.first), abs(it.last))
        else 0L
    }
}

private fun groupDetails(
    seedRanges :IntervalSet,
    path :List<String>,
    maps :Map<Pair<String, String>, IntervalMap<Long>>
) :IntervalMap<List<Pair<String, ComparableRange>>> {
    val result = IntervalMap.of(seedRanges.intervals.map { Pair(it, mutableMapOf(Pair(SEED, 0L))) })
    path.windowed(2).forEach { (src, tar) ->
        val map = maps[Pair(src, tar)]
        checkNotNull(map)
        for (entry in result.intervalEntries.entries.toList()) {
            val offset = entry.value[src]!!
            val interval = entry.key
            val mappings = map.gatherMappings(interval.shift(offset))
            if (mappings.size == 1) {
                entry.value[tar] = mappings.values.first() + offset
            } else {
                result.remove(entry.key)
                mappings.intervalEntries.forEach { key, v ->
                    val value = entry.value.toMap().toMutableMap()
                    value[tar] = v+offset
                    result.put(key.shift(-offset), value)
                }
            }
        }
    }
    return result.shuffleToLocation()
}

private fun IntervalMap<MutableMap<String, Long>>.shuffleToLocation() = intervalEntries.map {
        val offset :Long = it.value[LOCATION]!!
        val interval = it.key
        Pair(interval.shift(offset), it.value.entries
            .filter { (k, _) -> k != LOCATION }
            .map { (k, v :Long) -> Pair(k, interval.shift(v - offset)) })
    }.toTypedArray().let { IntervalMap(*it) }

private fun IntervalMap<Long>.gatherMappings(keyRange :ComparableRange) :IntervalMap<Long> {
    val overlaps = keys.intervals.filter { keyRange.compareTo(it) in -2..2 }
    val first = overlaps.firstOrNull()
    val left = if (first!=null) keyRange.leftExceed(first)  else keyRange
    val last = overlaps.lastOrNull()
    val right = if (last!=null) keyRange.rightExceed(last)  else null
    return ((if (left!=null) listOf(Pair(left, 0L))  else emptyList()) + overlaps.map {
        val key = intersect(keyRange, it)
        Pair(ComparableRange(key), this.getOrDefault(key.first, 0L))
    } +if(right!=null) listOf(Pair(right, 0L))  else emptyList()).toTypedArray()
        .let { IntervalMap(*it) }
}

fun main() {
    check(part1(readInput("Day05/day05_test")) == 35L)

    val input = readInput("Day05/day05")
    part1(input).println()
    println("\nPart 2")

    val t2 = part2(readInput("Day05/day05_test"))
    check(t2 == 46L) { "expected 46, but got $t2." }
    part2(input).println()
}

fun sgn(x :Long) = when {
    x > 0L -> 1
    x < 0L -> -1
    else -> 0
}
