import java.io.File

class Day01 {

    // Function to calculate the total distance (Part One)
    fun calculateTotalDistance(fileName: String): Int {
        val inputLines = File("src/resources/$fileName").readLines()

        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()

        // Parse the input
        for (line in inputLines) {
            val parts = line.split("\\s+".toRegex()).map { it.toInt() }
            if (parts.size == 2) {
                leftList.add(parts[0])
                rightList.add(parts[1])
            }
        }

        // Sort both lists
        leftList.sort()
        rightList.sort()

        // Calculate total distance
        return leftList.indices.sumBy { index ->
            kotlin.math.abs(leftList[index] - rightList[index])
        }
    }

    // Function to calculate the similarity score (Part Two)
    fun calculateSimilarityScore(fileName: String): Int {
        val inputLines = File("src/resources/$fileName").readLines()

        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()

        // Parse the input
        for (line in inputLines) {
            val parts = line.split("\\s+".toRegex()).map { it.toInt() }
            if (parts.size == 2) {
                leftList.add(parts[0])
                rightList.add(parts[1])
            }
        }

        // Count occurrences of each number in the right list
        val rightFrequency = rightList.groupingBy { it }.eachCount()

        // Calculate similarity score
        return leftList.sumBy { leftNumber ->
            val occurrences = rightFrequency.getOrDefault(leftNumber, 0)
            leftNumber * occurrences
        }
    }
}

fun main() {
    val day01 = Day01()

    // Part One
    val totalDistance = day01.calculateTotalDistance("input_day_1.txt")
    println("Total distance: $totalDistance")

    // Part Two
    val similarityScore = day01.calculateSimilarityScore("input_day_1.txt")
    println("Similarity score: $similarityScore")
}
