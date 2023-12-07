package day07

import println
import readInput

enum class Card(val symbol :Char) {
    TWO('2'), THREE('3'), FOUR('4'), FIVE('5'),
    SIX('6'), SEVEN('7'), EIGHT('8'), NINE('9'), TEN('T'),
    JACK('J'), QUEEN('Q'), KING('K'), ACE('A');

    companion object {
        fun of(symbol :Char) :Card? = entries.find { it.symbol==symbol }
    }

    override fun toString() = symbol.toString()
}

enum class Rank {
    FiveOfAKind, FourOfAKind, FullHouse, ThreeOfAKind, TwoPairs, OnePair, HighCard;

    companion object {
        fun compute(cards :Array<Card>) :Rank {
            // this implementation is 90% AI Coding
            val cardCounts = cards.groupBy { it }.mapValues { it.value.size }
            val maxCount = cardCounts.values.maxOrNull() ?: 0
            return when {
                maxCount == 5 -> FiveOfAKind
                maxCount == 4 -> FourOfAKind
                cardCounts.size == 2 && maxCount == 3 -> FullHouse
                cardCounts.size > 2 && maxCount == 3 -> ThreeOfAKind
                cardCounts.size == 3 && maxCount == 2 -> TwoPairs
                cardCounts.size == 4 && maxCount == 2 -> OnePair
                else -> HighCard
            }
        }
    }
}

data class Hand(val cards :Array<Card>) :Comparable<Hand> {
    val rank = Rank.compute(cards)
    override fun compareTo(other :Hand) =
        compareByDescending<Hand> { it.rank }
        .thenComparator { h1, h2 ->
            // also this snippet is AI Coding
            h1.cards.indices.firstOrNull { i -> h1.cards[i]!=h2.cards[i] }
                ?.let { h1.cards[it].compareTo(h2.cards[it]) } ?: 0
        }
        .compare(this, other)

    override fun toString() = cards.joinToString("")+"($rank)"

    override fun equals(other :Any?) :Boolean {
        return other is Hand && compareTo(other)==0
    }

    override fun hashCode() = 23 + cards.contentHashCode()
}

fun handOf(cards :Array<Card>) = Hand(cards)

private fun part1(input :Collection<String>) :Long {
    val bids = input.map { line ->
        val parts = line.split("\\s+".toRegex())
        check(parts.size==2)
        Pair(parts[0].map { Card.of(it)!! }.toTypedArray().let { handOf(it) },
            parts[1].toLong())
    }.sortedBy { it.first }
    println("The hands in order are: "+bids.joinToString { it.first.toString() })
    return bids.mapIndexed { rk, (_, bid) -> (rk+1)*bid }.sum()
}

enum class CardWithJoker(val symbol :Char) {
    JOKER('J'),
    TWO('2'), THREE('3'), FOUR('4'), FIVE('5'),
    SIX('6'), SEVEN('7'), EIGHT('8'), NINE('9'), TEN('T'),
    QUEEN('Q'), KING('K'), ACE('A');

    companion object {
        fun of(symbol :Char) :CardWithJoker? = entries.find { it.symbol==symbol }
    }

    override fun toString() = symbol.toString()
}

data class HandWithJokerCards(val cards :Array<CardWithJoker>) :Comparable<HandWithJokerCards> {
    val rank = Rank.compute(cards)
    override fun compareTo(other :HandWithJokerCards) =
        compareByDescending<HandWithJokerCards> { it.rank }
            .thenComparator { h1, h2 ->
                h1.cards.indices.firstOrNull { i -> h1.cards[i]!=h2.cards[i] }
                    ?.let { h1.cards[it].compareTo(h2.cards[it]) } ?: 0
            }
            .compare(this, other)

    override fun toString() = cards.joinToString("")+"($rank)"

    override fun equals(other :Any?) :Boolean {
        return other is HandWithJokerCards && compareTo(other)==0
    }

    override fun hashCode() = 29 + cards.contentHashCode()
}

fun handOf(cards :Array<CardWithJoker>) = HandWithJokerCards(cards)

private fun Rank.Companion.compute(cards :Array<CardWithJoker>) :Rank {
    val cardCounts = cards.groupBy { it }.mapValues { it.value.size }
    // this modification was my idea
    val numJokers = cardCounts[CardWithJoker.JOKER] ?: 0
    val nonJokerCardCounts = cardCounts.entries.filter { (k, _) -> k != CardWithJoker.JOKER }
    val maxCount = (nonJokerCardCounts.maxOfOrNull { it.value } ?: 0) +numJokers
    val numDifferentCards = nonJokerCardCounts.size
    return when {
        maxCount == 5 -> Rank.FiveOfAKind
        maxCount == 4 -> Rank.FourOfAKind
        numDifferentCards == 2 && maxCount == 3 -> Rank.FullHouse
        numDifferentCards > 2 && maxCount == 3 -> Rank.ThreeOfAKind
        numDifferentCards == 3 && maxCount == 2 -> Rank.TwoPairs
        numDifferentCards == 4 && maxCount == 2 -> Rank.OnePair
        else -> Rank.HighCard
    }
}

private fun part2(input :Collection<String>) :Long {
    val bids = input.map { line ->
        val parts = line.split("\\s+".toRegex())
        check(parts.size==2)
        Pair(parts[0].map { CardWithJoker.of(it)!! }.toTypedArray().let { handOf(it) },
            parts[1].toLong())
    }.sortedBy { it.first }
    println("The hands in order are: "+bids.joinToString { it.first.toString() })
    return bids.mapIndexed { rk, (_, bid) -> (rk+1)*bid }.sum()
}

fun main() {
    val t1 = part1(readInput("Day07/day07_test"))
    check(t1 == 6440L) { "expected 6440, but got $t1." }
    val input = readInput("Day07/day07")
    part1(input).println()

    println("\nPart 2:")
    val t2 = part2(readInput("Day07/day07_test"))
    check(t2 == 5905L) { "expected 5, but git $t2." }
    part2(input).println()
}
