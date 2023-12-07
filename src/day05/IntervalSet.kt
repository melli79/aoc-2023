package day05

import java.util.TreeSet
import kotlin.math.max
import kotlin.math.min

class IntervalSet(vararg ranges :ComparableRange) : MutableSet<Long> {
    val intervals = TreeSet<ComparableRange>()

    init {
        ranges.forEach { addInterval(it) }
    }

    companion object {
        fun of(vararg ranges :LongRange) = IntervalSet(*(ranges.map { ComparableRange(it) }.toTypedArray()))
        fun of(ranges :Collection<LongRange>) = IntervalSet(*ranges.map { ComparableRange(it) }.toTypedArray())
        fun ofComparable(ranges :Collection<ComparableRange>) = IntervalSet(*ranges.toTypedArray())
    }

    override fun toString() = intervals.joinToString(" u ") { "${it.first}..${it.last}" }

    override val size :Int
        get() = min(intervals.sumOf { it.endExclusive-it.first }, Int.MAX_VALUE.toLong()).toInt()

    override fun clear() {
        TODO("unimplemented")
    }

    override fun addAll(elements :Collection<Long>) :Boolean {
        TODO("unimplemented")
    }

    override fun add(element :Long) :Boolean {
        TODO("unimplemented")
    }

    fun addInterval(range :ComparableRange) :Boolean {
        val overlaps = intervals.filter { range.compareTo(it) in -2..2 }
        if (overlaps.isEmpty())
            return intervals.add(range)
        val first = min(overlaps.first().first, range.first)
        val last = max(overlaps.last().last, range.last)
        intervals.removeAll(overlaps)
        return intervals.add(ComparableRange(first..last))
    }

    override fun isEmpty() = intervals.isEmpty()

    override fun iterator() :MutableIterator<Long> {
        TODO("unimplemented")
    }

    override fun retainAll(elements :Collection<Long>) :Boolean {
        TODO("unimplemented")
    }

    override fun removeAll(elements :Collection<Long>) :Boolean {
        elements.forEach { remove(it) }
        return true
    }

    override fun remove(element :Long) :Boolean {
        TODO("unimplemented")
    }

    override fun containsAll(elements :Collection<Long>) = elements.all { contains(it) }

    override fun contains(element :Long) = intervals.any { it.contains(element) }
}

@Suppress("DEPRECATION")
fun LongRange.leftExceed(removal :LongRange) :LongRange? {
    if (first<removal.first)
        return first ..<min(endExclusive, removal.first)
    return null
}

fun LongRange.rightExceed(removal :LongRange) :LongRange? {
    if (removal.last < last)
        return removal.last ..last
    return null
}

fun ComparableRange.leftExceed(removal :ComparableRange) :ComparableRange? =
    content.leftExceed(removal.content)?.let { ComparableRange(it) }

fun ComparableRange.rightExceed(removal :ComparableRange) :ComparableRange? =
    content.rightExceed(removal.content)?.let { ComparableRange(it) }

fun intersect(keyRange :ComparableRange, first :ComparableRange) =
    max(keyRange.first, first.first)..min(keyRange.last, first.last)