fun main() {
    fun extrapolateForward(string: String): Int {
        val startSequence = string.split(" ").map { it.toInt() }
        val sequences = mutableListOf(startSequence)

        var lastSequence = startSequence
        while (lastSequence.any { it != 0 }) {
            val newSequence = lastSequence.zipWithNext { a, b -> b - a }
            sequences += newSequence
            lastSequence = newSequence
        }

        val reversed = sequences.reversed()

        val extrapolatedSequences = mutableListOf<List<Int>>()
        for ((index, sequence) in reversed.withIndex()) {
            val prevSequenceDiff = if (index == 0) 0 else extrapolatedSequences[index - 1].last()
            extrapolatedSequences.add(sequence + (sequence.last() + prevSequenceDiff))
        }
        return extrapolatedSequences.last().last()
    }

    fun part1(input: List<String>): Int {
        return input.sumOf { extrapolateForward(it) }
    }

    fun extrapolateBackward(string: String): Int {
        val startSequence = string.split(" ").map { it.toInt() }
        val sequences = mutableListOf(startSequence)

        var lastSequence = startSequence
        while (lastSequence.any { it != 0 }) {
            val newSequence = lastSequence.zipWithNext { a, b -> b - a }
            sequences += newSequence
            lastSequence = newSequence
        }

        val reversed = sequences.reversed()

        val extrapolatedSequences = mutableListOf<List<Int>>()
        for ((index, sequence) in reversed.withIndex()) {
            val prevSequenceDiff = if (index == 0) 0 else extrapolatedSequences[index - 1].first()
            val extrapolatedSequence = mutableListOf(sequence.first() - prevSequenceDiff)
            extrapolatedSequence.addAll(sequence)
            extrapolatedSequences.add(extrapolatedSequence)
        }
        return extrapolatedSequences.last().first()
    }

    fun part2(input: List<String>): Int {
        return input.sumOf { extrapolateBackward(it) }

    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day09_test")
    val input = readInput("Day09")

    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    part1(input).println()
    part2(input).println()
    newpart1(input).println()
}
