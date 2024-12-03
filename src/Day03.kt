import java.util.regex.Matcher
import java.util.regex.Pattern

fun main() {
    fun part1(input: List<String>): Int {
        val mulPattern = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")

        return input.sumOf { line ->
            mulPattern.findAll(line).sumOf {
                it.groupValues[1].toInt() * it.groupValues[2].toInt()
            }
        }
    }

    fun part2(input: List<String>): Int {
        val mulPattern = Regex("""mul\((\d{1,3}),(\d{1,3})\)""")
        val replacePattern = Regex("""don't\(\).*?(do\(\)|${'$'})""")

        return input.joinToString(separator = "").replace(replacePattern, "").let { line ->
            mulPattern.findAll(line).sumOf { it.groupValues[1].toInt() * it.groupValues[2].toInt() }
        }
    }

    val testInput = readInput("Day03_test")
    check(part1(testInput) == 161)
    check(part2(testInput) == 48)

    // Read the input from the `src/Day01.txt` file.
    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}