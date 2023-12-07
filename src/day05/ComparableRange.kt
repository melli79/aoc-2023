package day05

data class ComparableRange(val content :LongRange) : Comparable<ComparableRange> {
    val first = content.first
    val last = content.last
    val endExclusive :Long
        get() = last +1L

    override fun toString() = "ComparableRange($first..$last)"

    operator fun contains(element :Long) = content.contains(element)

    override fun compareTo(other :ComparableRange) = when {
        last < other.first -> -5
        other.last<first -> 5
        first < other.first ->
            if (other.last<=last) -1
            else -2
        other.first < first ->
            if (last<=other.last) 1
            else 2
        else -> // first==other.first
            if (last<other.last) -1
            else if (other.last<last) 1
            else 0 // true equality
    }

    fun shift(offset :Long) = ComparableRange((first + offset)..(last + offset))
}
