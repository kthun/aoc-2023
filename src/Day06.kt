import kotlin.math.ceil
import kotlin.math.floor
import kotlin.math.pow

fun main() {
    fun part1(input: List<String>): Int {
        val durations = input[0].substringAfter(": ").split(" ").filter { it.isNotBlank() }.map { it.toLong() }
        val recordDistances = input[1].substringAfter(": ").split(" ").filter { it.isNotBlank() }.map { it.toLong() }
        val races = IntRange(0, durations.lastIndex)
            .map { Race(durations[it], recordDistances[it]) }

        return races
            .map { it.winningChargeTimes() }
            .map { it.last - it.first + 1 }
            .reduce { acc, numWinningChargeTimes -> acc * numWinningChargeTimes }
    }

    fun part2(input: List<String>): Long {
        val duration = input[0].substringAfter(": ").filter { it.isDigit() }.toLong()
        val recordDistance = input[1].substringAfter(": ").filter { it.isDigit() }.toLong()
        val race = Race(duration, recordDistance)
        return race.numWinningChargeTimes()
    }

// test if implementation meets criteria from the description, like:
    val testInput = readInput("Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503L)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}

class Race(val duration: Long, val recordDistance: Long) {
    fun winningChargeTimes(): IntRange {
        val duration = duration.toInt()
        val recordDistance = recordDistance.toInt()

        val discriminant = duration.toDouble().pow(2) - 4 * recordDistance
        val winningChargeTimes = when (discriminant) {
            in Double.NEGATIVE_INFINITY..<0.0 -> IntRange.EMPTY
            in 0.0 - 1e-6..0.0 + 1e-6 -> {
                val time = duration / 2
                time..time
            }

            else -> {
                val time1 = (duration - discriminant.pow(0.5)) / 2 + 1e-6
                val time2 = (duration + discriminant.pow(0.5)) / 2 - 1e-6
                val rangeStart = ceil(time1).toInt()
                val rangeEnd = floor(time2).toInt()
                rangeStart..rangeEnd
            }
        }
        return winningChargeTimes
    }

    fun numWinningChargeTimes(): Long {
        val discriminant = duration.toDouble().pow(2) - 4 * recordDistance
        val numWinningChargeTimes = when (discriminant) {
            in Double.NEGATIVE_INFINITY..<0.0 -> 0
            in 0.0 - 1e-6..0.0 + 1e-6 -> 1
            else -> {
                val time1 = (duration - discriminant.pow(0.5)) / 2 + 1e-6
                val time2 = (duration + discriminant.pow(0.5)) / 2 - 1e-6
                floor(time2).toLong() - ceil(time1).toLong() + 1
            }
        }
        return numWinningChargeTimes
    }
}
