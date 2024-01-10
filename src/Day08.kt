fun main() {
    fun part1(input: List<String>): Int {
        val instructions = input[0]
        val nodes = input.drop(2).map { Node(it) }.associateBy { it.name }

        var currentNode = nodes["AAA"]!!
        val goalNodeName = "ZZZ"
        var stepCounter = 0

        while (currentNode.name != goalNodeName) {
            val instruction = instructions[stepCounter % instructions.length]
            currentNode = when (instruction) {
                'L' -> nodes[currentNode.leftName]!!
                'R' -> nodes[currentNode.rightName]!!
                else -> throw Exception("Unknown instruction: $instruction")
            }
            stepCounter++
        }
        return stepCounter
    }

    fun part2(input: List<String>): Long {

        fun findLoopLength(nodes: Map<String, Node>, instructions: String, startNodeName: String): Long {
            var currentNodeName = startNodeName
            var stepCounter = 0L
            while (currentNodeName[2] != 'Z') {
                val instruction = instructions[(stepCounter % instructions.length.toLong()).toInt()]

                val currentNode = nodes[currentNodeName]!!
                currentNodeName = when (instruction) {
                    'L' -> currentNode.leftName
                    'R' -> currentNode.rightName
                    else -> throw Exception("Unknown instruction: $instruction")
                }
                stepCounter++
            }
            return stepCounter
        }

        fun findLCM(a: Long, b: Long): Long {
            val larger = if (a > b) a else b
            val maxLcm = a * b
            var lcm = larger
            while (lcm <= maxLcm) {
                if (lcm % a == 0L && lcm % b == 0L) {
                    return lcm
                }
                lcm += larger
            }

            return maxLcm
        }

        fun findLCMOfListOfNumbers(numbers: List<Long>): Long {
            var result = numbers[0]
            for (i in 1 until numbers.size) {
                result = findLCM(result, numbers[i])
            }
            return result
        }

        val instructions = input[0]
        val nodes = input.drop(2).map { Node(it) }.associateBy { it.name }

        val startNodes = nodes.keys.filter { it[2] == 'A' }
        val loopLengths = startNodes.map { findLoopLength(nodes, instructions, it) }

        val lcm = findLCMOfListOfNumbers(loopLengths)
        return lcm
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("Day08_test1")
    val testInput2 = readInput("Day08_test2")
    val input = readInput("Day08")

    check(part1(testInput1) == 6)
    println(part1(input))

    check(part2(testInput2) == 6L)
    part2(input).println()
}

class Node(input: String) {
    val name: String = input.substringBefore(" = ")
    val leftName: String = input.substringAfter(" = (").substringBefore(",")
    val rightName: String = input.substringAfter(", ").substringBefore(")")

    override fun toString(): String {
        return "Node(name='$name', leftName='$leftName', rightName='$rightName')"
    }
}
