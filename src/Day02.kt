import java.io.File

class Day02 {
    fun calcSafeReports(fileName: String): Int {
        val input = parseFileToNumbers("src/resources/$fileName")
        var counterSafeReports = 0

        outer@ for (row in input) {
            // Check if the first two values are the same, which means it's neither ascending nor descending
            if (row[0] == row[1]) continue@outer

            // Determine if the lists first two values are ascending or descending
            val ascending = row[0] < row[1]
            var prev = row[0]

            for (value in row.drop(1)) {
                if (ascending) {
                    if (value !in listOf(prev + 1, prev + 2, prev + 3)) continue@outer
                } else {
                    if (value !in listOf(prev - 1, prev - 2, prev - 3)) continue@outer
                }
                prev = value
            }
            counterSafeReports++
        }
        return counterSafeReports
    }

    fun parseFileToNumbers(filePath: String): List<String> {
        val result = mutableListOf<String>()
        File(filePath).useLines { lines ->
            lines.forEach { line ->
                result.add(line)
            }
        }
        return result
    }

    fun calcSafeReportsWithDampener(input: List<String>): Int = input.count {
        it.toInts().let { ints ->
            ints.indices.any { idx ->
                ints.toMutableList().apply { removeAt(idx) }.isSafeReport()
            }
        }
    }

    private fun String.toInts(sep: String = " ") = split(sep).map { it.toInt() }

    private fun List<Int>.isSafeReport() = windowed(2).map { (a, b) -> a - b }.let {
        it.all { it in 1..3 } || it.all { it in -3..-1 }
    }
}

fun main() {
    val day02 = Day02()

    // Part One
    val safeReportsCount = day02.calcSafeReports("input_day_2.txt")
    println("number of safe reports: $safeReportsCount")
    // Part Two

    val safeReportsCountWithDampener = day02.calcSafeReportsWithDampener(day02.parseFileToNumbers("src/resources/input_day_2.txt"))
    println("number of safe  reports (with dampener): $safeReportsCountWithDampener")
}