fun main() {
    fun part1(input: List<String>): Int {
        val hands = input.map { line ->
            val (cards, bid) = line.split(" ")
            Hand(cards, bid.toInt())
        }
        val sortedHands = hands.sortedWith(Hand::compareHandStrengthTo).reversed()

        return sortedHands.withIndex().sumOf { (i, hand) -> hand.bid * (i + 1) }
    }

    fun part2(input: List<String>): Int {
        val hands = input.map { line ->
            val (cards, bid) = line.split(" ")
            Hand(cards, bid.toInt(), true)
        }
        val sortedHands = hands.sortedWith(Hand::compareHandStrengthTo).reversed()

        return sortedHands.withIndex().sumOf { (i, hand) -> hand.bid * (i + 1) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day07_test")
    val input = readInput("Day07")

    check(part1(testInput) == 6440)
    part1(input).println()

    check(part2(testInput) == 5905)
    part2(input).println()
}

private class Hand(val cards: String, val bid: Int, val useJokers: Boolean = false) {
    companion object {
        const val CARDVALUES = "AKQJT98765432"
        const val CARDVALUESWITHJOKERS = "AKQT98765432J"
    }

    val handType = HandType.from(cards, useJokers)

    fun compareHandStrengthTo(other: Hand, useJokers: Boolean = false): Int {
        return when {
            handType > other.handType -> 1
            handType < other.handType -> (-1)
            else -> compareCardValues(other)
        }
    }

    fun compareCardValues(other: Hand): Int {
        val thisHandCardValues = if (useJokers) CARDVALUESWITHJOKERS else CARDVALUES

        cards.indices.forEach { i ->
            val thisCard = cards[i]
            val thatCard = other.cards[i]
            if (thisCard != thatCard) {
                return thisHandCardValues.indexOf(thisCard).compareTo(thisHandCardValues.indexOf(thatCard))
            }
        }
        return 0
    }

    override fun toString(): String {
        return "$cards ($handType), bid: $bid"
    }

}

private enum class HandType {
    FIVE_OF_A_KIND,
    FOUR_OF_A_KIND,
    FULL_HOUSE,
    THREE_OF_A_KIND,
    TWO_PAIRS,
    ONE_PAIR,
    HIGH_CARD;

    companion object {
        fun from(cards: String, useJokers: Boolean): HandType {
            var cards = cards
            var jokerCount = 0
            if (useJokers) {
                jokerCount = cards.count { it == 'J' }
                cards = cards.replace("J", "")
            }

            val cardCounts = cards.groupingBy { it }.eachCount()
            val counts = cardCounts.values.sortedDescending().toMutableList()

            if (useJokers) {
                if (jokerCount == 5) {
                    return FIVE_OF_A_KIND
                } else {
                    counts[0] += jokerCount
                }
            }

            return when (counts) {
                listOf(5) -> FIVE_OF_A_KIND
                listOf(4, 1) -> FOUR_OF_A_KIND
                listOf(3, 2) -> FULL_HOUSE
                listOf(3, 1, 1) -> THREE_OF_A_KIND
                listOf(2, 2, 1) -> TWO_PAIRS
                listOf(2, 1, 1, 1) -> ONE_PAIR
                else -> HIGH_CARD
            }
        }
    }
}
