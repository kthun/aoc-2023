typealias SchemaRow = List<Element>
typealias Schema = List<SchemaRow>

fun main() {
    fun part1(input: List<String>): Int {
        val engineSchematic = input.mapIndexed { i, s -> extractElements(s, i) }
        return engineSchematic
            .findParts()
            .sumOf { it.value }
    }


    fun part2(input: List<String>): Int {
        val engineSchematic = input.mapIndexed { i, s -> extractElements(s, i) }
        return engineSchematic
            .findGears()
            .sumOf { gear -> gear.first * gear.second }
    }

    val testInput = readInput("Day03_test")

    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}

private fun extractElements(input: String, row: Int): SchemaRow = buildList {
    var currentNumberStart = -1
    var currentNumberString = ""
    for ((index, c) in input.withIndex()) {
        when {
            c.isDigit() -> {
                if (currentNumberStart == -1) {
                    currentNumberStart = index
                }
                currentNumberString += c
            }

            else -> {
                if (c != '.') {
                    this.add(Symbol(c, index, row))
                }
                if (currentNumberString.isNotEmpty()) {
                    this.add(Number(currentNumberString, currentNumberStart, index - 1, row))
                    currentNumberStart = -1
                    currentNumberString = ""
                }
            }
        }
        if (currentNumberString.isNotEmpty() && index == input.length - 1) {
            this.add(Number(currentNumberString, currentNumberStart, index, row))
        }
    }
}

private fun Schema.findParts(): Set<Number> {
    val flattenedSchema = this.flatten()
    val symbols = flattenedSchema.filterIsInstance<Symbol>()
    val numbers = flattenedSchema.filterIsInstance<Number>()
    val parts = numbers.filter { number ->
        symbols.any { symbol ->
            symbol.row in number.expandedRow  && symbol.column in number.expandedColumn
        }
    }
    return parts.toSet()
}

private fun Schema.findGears(): List<Pair<Int, Int>> {
    val flattenedSchema = this.flatten()
    val parts = flattenedSchema.filterIsInstance<Number>()
    val potentialGears = flattenedSchema.filterIsInstance<Symbol>().filter { it.value == '*' }
    return potentialGears.map { symbol ->
        parts.filter { part -> (symbol.row in part.expandedRow) && (symbol.column in part.expandedColumn) }
    }.filter { it.size == 2 }.map { it[0].value to it[1].value }
}

sealed class Element

data class Number(val value: Int, val xRange: IntRange, val row: Int) : Element() {
    val expandedColumn = xRange.first - 1..xRange.last + 1
    val expandedRow = row - 1..row + 1
}

fun Number(number: String, start: Int, end: Int, row: Int): Number = Number(number.toInt(), start..end, row)

data class Symbol(val value: Char, val column: Int, val row: Int) : Element()
