package day05

import java.util.TreeMap

class IntervalMap<V>(vararg pairs :Pair<ComparableRange, V>) : MutableMap<Long, V> {
    private val content = TreeMap<ComparableRange, V>()

    init {
        pairs.forEach { put(it.first, it.second) }
    }

    companion object {
        fun <V> of(vararg pairs :Pair<LongRange, V>) = IntervalMap(*(pairs.map {
            Pair(ComparableRange(it.first), it.second)
        }.toTypedArray()))
    }

    fun put(key :ComparableRange, value :V) :List<V> {
        val overlappingKeys = content.keys.filter { it.contains(key.first)||it.contains(key.last) }
        var result = listOf<V>()
        if (overlappingKeys.isNotEmpty()) {
            val kL = overlappingKeys.first()
            val left = kL.leftExceed(key)
            val vL = content.remove(kL)!!
            if (left!=null)
                content.put(left, vL)
            val kR = overlappingKeys.last()
            result = overlappingKeys.drop(1).dropLast(1).map { content.remove(it) }.filterNotNull()
            val right = kR.rightExceed(key)
            val vR = if (kR!=kL) content.remove(kR)!! else vL
            if (right!=null)
                content.put(right, vR)
        }
        content.put(key, value)
        return result
    }

    override val entries :MutableSet<MutableMap.MutableEntry<Long, V>>
        get() = TODO("unimplemented")
    override val keys :MutableSet<Long>
        get() = IntervalSet.ofComparable(content.keys)
    override val size :Int
        get() = content.size

    override val values :MutableCollection<V>
        get() = content.values

    override fun clear() {
        content.clear()
    }

    override fun isEmpty() = content.isEmpty()
    override fun remove(key :Long) :V? {
        val interval = content.keys.find { it.contains(key) }
        if (interval==null)
            return null
        val value = content.remove(interval)!!
        val kL = interval.leftExceed(ComparableRange(key..key))
        if (kL!=null)
            content[kL] = value
        val kR = interval.rightExceed(ComparableRange(key..key))
        if (kR!=null)
            content[kR] = value
        else
            return value
        return null
    }

    override fun putAll(from :Map<out Long, V>) = from.forEach { k, v -> put(k, v) }

    override fun put(key :Long, value :V) = put(ComparableRange(key..key), value).firstOrNull()

    override fun get(key :Long) :V? {
        val i = content.keys.find { it.contains(key) }
        return if (i!=null)
            content[i]
        else
            null
    }

    override fun containsValue(value :V) = TODO("unimplemented")

    override fun containsKey(key :Long) = content.keys.any { it.contains(key) }
}
