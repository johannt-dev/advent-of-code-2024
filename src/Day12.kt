import java.math.BigInteger
import kotlin.time.times

fun main() {
    fun part1(map: Array<CharArray>): Int {
        return calculateFencingCost(map)
    }

    fun part2(map: Array<CharArray>): Int {
        return calculateFencingCostWithSides(map)
    }

    val testInput = readInputTo2DArray("Day12_test")
    check(part1(testInput) == 1930)
    check(part2(testInput) == 1206)

    val input = readInputTo2DArray("Day12")
    part1(input).println()
    part2(input).println()
}

// this could be refactored by using the additional functions for part2... might do that in the future :-)
fun calculateFencingCost(map: Array<CharArray>): Int {
    val rows = map.size
    val cols = map[0].size
    val visited = Array(rows) { BooleanArray(cols) }

    // Directions for moving up/down/left/right
    val directions = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))

    fun isWithinBounds(x: Int, y: Int): Boolean = x in 0 until rows && y in 0 until cols

    fun floodFill1(x: Int, y: Int, plantType: Char): Pair<Int, Int> {
        val queue = ArrayDeque<Pair<Int, Int>>()
        queue.add(x to y)
        visited[x][y] = true

        var area = 0
        var perimeter = 0

        while (queue.isNotEmpty()) {
            val (curX, curY) = queue.removeFirst()
            area++

            for ((dx, dy) in directions) {
                val newX = curX + dx
                val newY = curY + dy

                if (isWithinBounds(newX, newY)) {
                    if (map[newX][newY] == plantType && !visited[newX][newY]) {
                        visited[newX][newY] = true
                        queue.add(newX to newY)
                    } else if (map[newX][newY] != plantType) {
                        perimeter++
                    }
                } else {
                    perimeter++
                }
            }
        }

        return area to perimeter
    }

    var totalCost = 0

    // Traverse the grid and process each unvisited cell
    for (i in 0 until rows) {
        for (j in 0 until cols) {
            if (!visited[i][j]) {
                val plantType = map[i][j]
                val (area, perimeter) = floodFill1(i, j, plantType)
                totalCost += area * perimeter
            }
        }
    }

    return totalCost
}

fun calculateFencingCostWithSides(map: Array<CharArray>): Int {
    val rows = map.size
    val cols = map[0].size
    val visited = Array(rows) { BooleanArray(cols) }

    // triple (id area, x, y)
    val fences = mutableSetOf<Triple<Pair<Int, String>, Int, Int>>()
    val areas = mutableMapOf<Int, Int>()
    var id = 0

    // Directions for moving up/down/left/right
    val directions = listOf(Pair(-1, 0), Pair(1, 0), Pair(0, -1), Pair(0, 1))

    fun isWithinBounds(x: Int, y: Int): Boolean = x in 0 until rows && y in 0 until cols

    fun floodFill2(x: Int, y: Int, plantType: Char) {
        val queue = ArrayDeque<Pair<Int, Int>>()
        queue.add(x to y)
        visited[x][y] = true

        var area = 0

        while (queue.isNotEmpty()) {
            val (curX, curY) = queue.removeFirst()
            area++

            for (direction in directions.indices) {
                val newX = curX + directions.get(direction).first
                val newY = curY + directions.get(direction).second
                val d = when(direction) {
                    0 -> "VL"
                    1 -> "VR"
                    2 -> "HD"
                    3 -> "HU"
                    else -> "X"
                }

                if (isWithinBounds(newX, newY)) {
                    if (map[newX][newY] == plantType && !visited[newX][newY]) {
                        visited[newX][newY] = true
                        queue.add(newX to newY)
                    } else if (map[newX][newY] != plantType) {
                        fences.add(Triple(Pair(id, d), curX, curY))
                    }
                } else {
                    fences.add(Triple(Pair(id, d), curX, curY))
                }
            }
        }

        areas[id] = area
        id++
    }

    // Traverse the grid and process each unvisited cell
    for (i in 0 until rows) {
        for (j in 0 until cols) {
            if (!visited[i][j]) {
                val plantType = map[i][j]
                floodFill2(i, j, plantType)
            }
        }
    }

    var totalCost = 0
    areas.forEach { area ->
        println("${area.key}: ${area.value} area. Fences [${findFencesForArea(area.key, fences).size}]: ${findFencesForArea(area.key, fences).joinToString(", ")}" +
        "- straight lines: ${countStraightLines(findFencesForArea(area.key, fences))}")
        val countLines = countStraightLines(findFencesForArea(area.key, fences))
        totalCost += area.value * countLines
    }
    println("Total cost: $totalCost")
    return totalCost
}

fun findFencesForArea(id: Int, fences: Set<Triple<Pair<Int, String>, Int, Int>>): List<Triple<String, Int, Int>> {
    val fencesForArea = mutableListOf<Triple<String, Int, Int>>()
    fences.forEach { fence ->
        if (fence.first.first == id) fencesForArea.add(Triple(fence.first.second, fence.second, fence.third))
    }
    return fencesForArea
}

fun countStraightLines(fences: List<Triple<String, Int, Int>>): Int {
    // Separate fences into groups based on their type
    val verticalLeft = mutableSetOf<Pair<Int, Int>>() // x-coord for VL
    val verticalRight = mutableSetOf<Pair<Int, Int>>() // x-coord for VR
    val horizontalDown = mutableSetOf<Pair<Int, Int>>() // y-coord for HD
    val horizontalUp = mutableSetOf<Pair<Int, Int>>() // y-coord for HU

    for (fence in fences) {
        when (fence.first) {
            "VL" -> verticalLeft.add(Pair(fence.second, fence.third))
            "VR" -> verticalRight.add(Pair(fence.second, fence.third))
            "HD" -> horizontalDown.add(Pair(fence.second, fence.third))
            "HU" -> horizontalUp.add(Pair(fence.second, fence.third))
        }
    }

    // Function to compress fences in the same line
    fun compressFences(fences: Set<Pair<Int, Int>>, isVertical: Boolean): Int {
        val grouped = mutableMapOf<Int, MutableList<Int>>()

        for (fence in fences) {
            val key = if (isVertical) fence.first else fence.second
            val value = if (isVertical) fence.second else fence.first
            grouped.computeIfAbsent(key) { mutableListOf() }.add(value)
        }

        // Count unique lines by compressing contiguous values
        return grouped.values.sumOf { values ->
            values.sorted().zipWithNext().count { (a, b) -> b - a > 1 } + 1
        }
    }

    // Compress vertical and horizontal fences
    val verticalLeftCount = compressFences(verticalLeft, isVertical = true)
    val verticalRightCount = compressFences(verticalRight, isVertical = true)
    val horizontalDownCount = compressFences(horizontalDown, isVertical = false)
    val horizontalUpCount = compressFences(horizontalUp, isVertical = false)

    // Total compressed fence occurrences
    return verticalLeftCount + verticalRightCount + horizontalDownCount + horizontalUpCount
}