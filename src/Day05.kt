fun main() {
    fun part1(input: List<String>): Long {
        val seeds = input.first().substringAfter(": ").split(" ").map { it.toLong() }

        val maps = input.drop(2).joinToString(separator = "\n").split("\n\n").map { mapWithHeader ->
            RangeMap(mapWithHeader.split("\n").drop(1))
        }

        return seeds.minOf { seed ->
            maps.fold(seed) { acc, map ->
                map.convertSourceToDestination(acc)
            }
        }
    }

    fun part2(input: List<String>): Long {
        val seedRanges = input.first().substringAfter(": ").split(" ")
            .chunked(2)
            .map { (start, length) ->
                start.toLong()..<start.toLong() + length.toLong()
            }

        val mapsReversed = input.drop(2).joinToString(separator = "\n").split("\n\n").map { mapWithHeader ->
            RangeMap(mapWithHeader.split("\n").drop(1))
        }.reversed()

        return generateSequence(0L, Long::inc).first { potentialLocation ->
            val seed = mapsReversed.fold(potentialLocation) { acc, map ->
                map.convertDestinationToSource(acc)
            }
            seedRanges.any { seed in it }
        }
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}


class RangeMap(rangeStrings: List<String>) {
    private val ranges: List<Range> = rangeStrings.map { line ->
        line.split(" ")
            .map { it.toLong() }
            .let { (destination, source, length) -> Range(source, destination, length) }
    }

    fun convertSourceToDestination(source: Long): Long {
        val range = ranges.firstOrNull { it.containsSource(source) }
        return range?.convertSourceToDestination(source) ?: source
    }

    fun convertDestinationToSource(destination: Long): Long {
        val range = ranges.firstOrNull { it.containsDestination(destination) }
        return range?.convertDestinationToSource(destination) ?: destination
    }

    private class Range(val sourceStart: Long, val destinationStart: Long, val length: Long) {
        fun containsSource(value: Long): Boolean = value in sourceStart..<sourceStart + length
        fun containsDestination(value: Long): Boolean = value in destinationStart..<destinationStart + length

        fun convertSourceToDestination(value: Long): Long {
            require(containsSource(value)) { "Value $value is not in range $this" }
            return destinationStart + (value - sourceStart)
        }

        fun convertDestinationToSource(value: Long): Long {
            return if (value in destinationStart..destinationStart + length) {
                sourceStart + (value - destinationStart)
            } else {
                value
            }
        }

    }
}


