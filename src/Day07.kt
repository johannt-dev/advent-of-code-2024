fun main() {

    fun part1(input: List<String>): Long {
        return input.sumOf { line ->
            val equation = parseEquation(line)
            if(isSolvable(equation.first, equation.second)) equation.first else 0L
        }
    }

    fun part2(input: List<String>): Long {
        return input.sumOf { line ->
            val equation = parseEquation(line)
            if(isSolvableWithConcat(equation.first, equation.second)) equation.first else 0L
        }
    }



    val testInput = readInput("Day07_test")
    check(part1(testInput) == 3749L)
    check(part2(testInput) == 11387L)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}

fun parseEquation(equation: String): Pair<Long, List<Long>> {
    val parts = equation.split(": ")
    val target = parts[0].toLong()
    val numbers = parts[1].split(" ").map { it.toLong() }
    return Pair(target, numbers)
}

fun isSolvable(target: Long, numbers: List<Long>, currentVal: Long = numbers[0], currentIndex: Int = 1): Boolean {
    if (currentIndex == numbers.size) return currentVal == target //check if target reached

    val nextValue = numbers[currentIndex]
    return isSolvable(target, numbers, currentVal + nextValue, currentIndex + 1) || isSolvable(target, numbers, currentVal * nextValue, currentIndex + 1)
}

fun isSolvableWithConcat(target: Long, numbers: List<Long>, currentVal: Long = numbers[0], currentIndex: Int = 1): Boolean {
    if (currentIndex == numbers.size) return currentVal == target //check if target reached
    val nextValue = numbers[currentIndex]
    return isSolvableWithConcat(target, numbers, currentVal + nextValue, currentIndex + 1) || isSolvableWithConcat(target, numbers, currentVal * nextValue, currentIndex + 1) || isSolvableWithConcat(target, numbers, (currentVal.toString() + nextValue.toString()).toLong(), currentIndex + 1)
}