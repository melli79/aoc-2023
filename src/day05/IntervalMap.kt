package day05

import java.util.TreeMap

class IntervalMap<V>(vararg pairs :Pair<ComparableRange, V>) : MutableMap<Long, V> {
    val intervalEntries = TreeMap<ComparableRange, V>()

    init {
        pairs.forEach { put(it.first, it.second) }
    }

    companion object {
        fun <V> of(vararg pairs :Pair<LongRange, V>) = IntervalMap(*(pairs.map {
            Pair(ComparableRange(it.first), it.second)
        }.toTypedArray()))
        fun <V> of(entries :Collection<Pair<ComparableRange, V>>) = IntervalMap(*(entries.toTypedArray()))
    }

    fun put(key :ComparableRange, value :V) :List<V> {
        val overlappingKeys = intervalEntries.keys.filter { it.contains(key.first)||it.contains(key.last) }
        var result = listOf<V>()
        if (overlappingKeys.isNotEmpty()) {
            val kL = overlappingKeys.first()
            val left = kL.leftExceed(key)
            val vL = intervalEntries.remove(kL)!!
            if (left!=null)
                intervalEntries.put(left, vL)
            val kR = overlappingKeys.last()
            result = overlappingKeys.drop(1).dropLast(1).map { intervalEntries.remove(it) }.filterNotNull()
            val right = kR.rightExceed(key)
            val vR = if (kR!=kL) intervalEntries.remove(kR)!! else vL
            if (right!=null)
                intervalEntries.put(right, vR)
        }
        intervalEntries.put(key, value)
        return result
    }

    override val entries :MutableSet<MutableMap.MutableEntry<Long, V>>
        get() = TODO("unimplemented")

    // warning: modifying this Set has no impact on the original Map
    override val keys :IntervalSet
        get() = IntervalSet.ofComparable(intervalEntries.keys)
    override val size :Int
        get() = intervalEntries.size

    override val values :MutableCollection<V>
        get() = intervalEntries.values

    override fun clear() {
        intervalEntries.clear()
    }

    override fun isEmpty() = intervalEntries.isEmpty()
    override fun remove(key :Long) :V? {
        val interval = intervalEntries.keys.find { it.contains(key) }
        if (interval==null)
            return null
        val value = intervalEntries.remove(interval)!!
        val kL = interval.leftExceed(ComparableRange(key..key))
        if (kL!=null)
            intervalEntries[kL] = value
        val kR = interval.rightExceed(ComparableRange(key..key))
        if (kR!=null)
            intervalEntries[kR] = value
        else
            return value
        return null
    }

    // This method only works on the intervals in the TreeMap
    fun remove(intervalKey :ComparableRange) = intervalEntries.remove(intervalKey)

    override fun putAll(from :Map<out Long, V>) = from.forEach { k, v -> put(k, v) }

    override fun put(key :Long, value :V) = put(ComparableRange(key..key), value).firstOrNull()

    override fun get(key :Long) :V? {
        val i = intervalEntries.keys.find { it.contains(key) }
        return if (i!=null)
            intervalEntries[i]
        else
            null
    }

    override fun containsValue(value :V) = TODO("unimplemented")

    override fun containsKey(key :Long) = intervalEntries.keys.any { it.contains(key) }
}
