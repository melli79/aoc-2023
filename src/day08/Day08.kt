package day08

import println
import readInput
import java.lang.System.out
import kotlin.math.abs

enum class Direction(val symbol :Char) {
    RIGHT('R'), LEFT('L');

    companion object {
        fun of(symbol :Char) = entries.find { it.symbol==symbol }
    }

    override fun toString() = symbol.toString()
}

data class Node(val left :String, val right :String) {
    override fun toString() = "($left, $right)"
}

private fun part1(input :Iterator<String>) :Long {
    val sequence = input.next().map { Direction.of(it)!! }
    check(input.next().isBlank())
    val network = readNetwork(input)
    var pos = "AAA"
    var ip = 0
    var count = 0L
    while (pos!="ZZZ") {
        val node = network[pos]
        checkNotNull(node)
        pos = when (sequence[ip]) {
            Direction.LEFT -> node.left
            Direction.RIGHT -> node.right
        }
        ip = (ip+1) % sequence.size
        count++
    }
    return count
}

private fun readNetwork(input :Iterator<String>) :MutableMap<String, Node> {
    val network = mutableMapOf<String, Node>()
    while (input.hasNext()) {
        val line = input.next()
        val parts = line.split(" ")
        val name = parts[0]
        val left = parts[2].trim('(', ',')
        val right = parts[3].trim(')')
        network[name] = Node(left, right)
    }
    return network
}

private fun part2(input :Iterator<String>) :Long {
    val sequence = input.next().map { Direction.of(it)!! }
    check(input.next().isBlank())
    val network = readNetwork(input)
    val starts = network.keys.filter { it.endsWith('A') }
    val periods = starts.map { computePeriod(it, sequence, network) }
    println("periods:"+ periods.joinToString { "${it.first} % ${it.second}" })
    val period = periods.fold(1L) { pr, (_,p) -> lcm(pr, p) }
    println("period= $period")
    return period
}

private fun computePeriod(
    s :String,
    sequence :List<Direction>,
    network :Map<String, Node>
) :Pair<Long, Long> {
    var ip = 0; var p = s
    var count = 0L
    var offset = 0L; var period = 0L
    while (true) {
        val d = sequence[ip]
        val node = network[p]
        checkNotNull(node)
        p = when (d) {
            Direction.LEFT -> node.left
            Direction.RIGHT -> node.right
        }
        ip = (ip + 1) % sequence.size
        count++
        if (p.endsWith('Z')) when {
            offset == 0L -> offset = count
            period == 0L -> period = count - offset
            else -> break
        }
        if (count % 100_000L == 0L) {
            print("."); out.flush()
            if (count % 10_000_000L == 0L) println()
        }
    }
    return Pair(offset, period)
}

fun gcd(a :Long, b :Long) :Long {
    var r0 = abs(a);  var r1 = abs(b)
    while (r1>0L) {
        val r2 = r0 % r1
        r0 = r1
        r1 = r2
    }
    return r0
}

fun lcm(a :Long, b :Long) = a/gcd(a,b) * b

fun main() {
    val t1 = part1(readInput("day08/day08_test").iterator())
    check(t1 == 2L) { "expected 5, but was $t1" }
    val input = readInput("day08/day08")
    part1(input.iterator()).println()

    print("\nPart 2: ")
    val t2 = part2(readInput("day08/day08_test2").iterator())
    check(t2 == 6L) { "expected 5, but was $t2" }
    part2(input.iterator()).println()
}
