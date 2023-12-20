import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        val cards: List<ScratchCard> = input.map { ScratchCard(it) }
         return cards.sumOf { it.score() }
    }

    fun part2(input: List<String>): Int {
        val cards: List<ScratchCard> = input.map { ScratchCard(it) }
        val numCards = cards.associate { it.cardId to 1 }.toMutableMap()

        for (cardId in numCards.keys) {
            val card = cards.find { it.cardId == cardId }!!
            val numMatches = card.numMatches()
            for (i in 1..numMatches) {
                numCards[cardId + i] = numCards[cardId + i]!! + numCards[cardId]!!
            }
        }
        return numCards.values.sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}

class ScratchCard(s: String) {
    val cardId: Int = s.substringAfter("Card ").substringBefore(":").trim().toInt()
    private val selectedNumbers: Set<Int> = s.substringAfter(": ").substringBefore(" |")
        .split(" ").filter{ it.isNotBlank() }.map { it.toInt() }.toSet()
    private val winningNumbers: Set<Int> = s.substringAfter("| ").split(" ").filter { it.isNotBlank() }.map { it.toInt() }.toSet()

    fun score(): Int {
        val numMatches = numMatches()
        return if (numMatches > 0)  2.0.pow(numMatches - 1).toInt() else 0
    }

    fun numMatches(): Int {
        return selectedNumbers.intersect(winningNumbers).size
    }

}
