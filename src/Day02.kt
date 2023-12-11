fun main() {
    fun part1(input: List<String>): Int {
        val availableCubes = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14)

        return input.filter { it.arePossibleCubeDraws(availableCubes) }.sumOf { it.substringAfter("Game ").substringBefore(":").toInt() }
    }

    fun part2(input: List<String>): Int {
        return input
            .map { it.substringAfter(": ").minimumCubesMap() }
            .sumOf { it.values.reduce(Int::times) }

    }

    val testInput = readInput("Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}

private fun String.minimumCubesMap(): Map<String, Int> {
    return this
        .split("; ")
        .map { draw ->
            draw.split(", ")
                .map { it.split(" ") }
                .associate { (count, color) -> color to count.toInt() }
        }
        .fold(mapOf()) { acc, colorCount ->
            colorCount.entries.fold(acc) { acc2, (color, count) ->
                acc2 + (color to maxOf(acc2.getOrDefault(color, 0), count))
            }
        }
}

private fun String.arePossibleCubeDraws(availableCubes: Map<String, Int>): Boolean {
    val draws = substringAfter(": ")
        .split("; ")
        .map { draw ->
            draw.split(", ")
                .map { it.split(" ") }
                .associate { (count, color) -> color to count.toInt() }
        }

    return draws.all { colorCount ->
        colorCount.all { (color, count) ->
            availableCubes.getOrDefault(color, 0) >= count
        }
    }
}
