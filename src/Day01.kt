fun main() {
    fun part1(input: List<String>): Int {
        return input.sumOf { line ->
            val first = line.first { it.isDigit() }
            val last = line.last { it.isDigit() }
            "$first$last".toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val digits = listOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
        return input.sumOf { line ->
            val firstString = line.findAnyOf(digits) ?: Pair(Int.MAX_VALUE, "0")
            val lastString = line.findLastAnyOf(digits) ?: Pair(Int.MIN_VALUE, "0")

            val firstDigit = line.withIndex().firstOrNull { it.value.isDigit() } ?: IndexedValue(Int.MAX_VALUE, '0')
            val lastDigit = line.withIndex().lastOrNull { it.value.isDigit() } ?: IndexedValue(Int.MIN_VALUE, '0')

            val firstValue = if (firstString.first < firstDigit.index) {
                digits.indexOf(firstString.second)
            } else firstDigit.value.digitToInt()

            val lastValue = if (lastString.first > lastDigit.index) {
                digits.indexOf(lastString.second)
            } else lastDigit.value.digitToInt()

            "$firstValue$lastValue".toInt()
        }
    }

    val testInput1 = readInput("Day01_test1")
    check(part1(testInput1) == 142)
    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
