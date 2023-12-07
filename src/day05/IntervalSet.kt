package day05

import java.util.TreeSet
import kotlin.math.max
import kotlin.math.min

class IntervalSet(vararg ranges :ComparableRange) : MutableSet<Long> {
    private val content = TreeSet<ComparableRange>()

    init {
        ranges.forEach { addInterval(it) }
    }

    companion object {
        fun of(vararg ranges :LongRange) = IntervalSet(*(ranges.map { ComparableRange(it) }.toTypedArray()))
        fun of(ranges :Collection<LongRange>) = IntervalSet(*ranges.map { ComparableRange(it) }.toTypedArray())
        fun ofComparable(ranges :Collection<ComparableRange>) = IntervalSet(*ranges.toTypedArray())
    }

    override val size :Int
        get() = min(content.sumOf { it.endExclusive-it.first }, Int.MAX_VALUE.toLong()).toInt()

    override fun clear() {
        TODO("Not yet implemented")
    }

    override fun addAll(elements :Collection<Long>) :Boolean {
        TODO("Not yet implemented")
    }

    override fun add(element :Long) :Boolean {
        TODO("Not yet implemented")
    }

    fun addInterval(range :ComparableRange) :Boolean {
        val overlaps = content.filter { range.compareTo(it) in -2..2 }
        if (overlaps.isEmpty())
            return content.add(range)
        val first = min(overlaps.first().first, range.first)
        val last = max(overlaps.last().last, range.last)
        content.removeAll(overlaps)
        return content.add(ComparableRange(first..last))
    }

    override fun isEmpty() = content.isEmpty()

    override fun iterator() :MutableIterator<Long> {
        TODO("Not yet implemented")
    }

    override fun retainAll(elements :Collection<Long>) :Boolean {
        TODO("Not yet implemented")
    }

    override fun removeAll(elements :Collection<Long>) :Boolean {
        elements.forEach { remove(it) }
        return true
    }

    override fun remove(element :Long) :Boolean {
        TODO("Not yet implemented")
    }

    override fun containsAll(elements :Collection<Long>) = elements.all { contains(it) }

    override fun contains(element :Long) = content.any { it.contains(element) }
}

data class ComparableRange(val content :LongRange) : Comparable<ComparableRange> {
    val first = content.first
    val last = content.last
    val endExclusive = content.endExclusive

    operator fun contains(element :Long) = content.contains(element)

    override fun compareTo(other :ComparableRange) = when {
        last < other.first -> -5
        other.last<first -> 5
        first < other.first ->
            if (other.last<last) -1
            else -2
        other.first < first ->
            if (last<=other.last) 1
            else 2
        else -> // first==other.first
            if (last<other.last) -1
            else if (other.last<last) 1
            else 0 // true equality
    }
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
