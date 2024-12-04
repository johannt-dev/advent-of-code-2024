import java.util.regex.Matcher
import java.util.regex.Pattern

fun extractVerticalWindows(grid: Array<CharArray>, windowSize: Int): List<List<Char>> {
    val windows = mutableListOf<List<Char>>()
    for (col in 0 until grid[0].size) {
        for (row in 0..(grid.size - windowSize)) {
            windows.add((0 until windowSize).map { grid[row + it][col] })
        }
    }
    return windows
}

fun extractHorizontalWindows(grid: Array<CharArray>, windowSize: Int): List<List<Char>> {
    val windows = mutableListOf<List<Char>>()
    for (row in 0 until grid.size) {
        for (col in 0..(grid[row].size - windowSize)) {
            windows.add((0 until windowSize).map { grid[row][col + it] })
        }
    }
    return windows
}

fun extractDiagonalLeftTopToRightBottomWindows(grid: Array<CharArray>, windowSize: Int): List<List<Char>> {
    val windows = mutableListOf<List<Char>>()
    for (row in 0..(grid.size - windowSize)) {
        for (col in 0..(grid[0].size - windowSize)) {
            windows.add((0 until windowSize).map { grid[row + it][col + it] })
        }
    }
    return windows
}

fun extractDiagonalLeftBottomToRightTopWindows(grid: Array<CharArray>, windowSize: Int): List<List<Char>> {
    val windows = mutableListOf<List<Char>>()
    for (row in (windowSize - 1) until grid.size) {
        for (col in 0..(grid[0].size - windowSize)) {
            windows.add((0 until windowSize).map { grid[row - it][col + it] })
        }
    }
    return windows
}

fun extractXYGrids(grid: Array<CharArray>, x: Int, y: Int): MutableList<List<List<Char>>> {
    val grids = mutableListOf<List<List<Char>>>()
    val rows = grid.size
    val cols = grid[0].size

    for (startRow in 0..(rows - x)) {
        for (startCol in 0..(cols - y)) {
            val subGrid = (startRow until startRow + x).map { row ->
                (startCol until startCol + y).map { col ->
                    grid[row][col]
                }
            }
            grids.add(subGrid)
        }
    }

    return grids
}

fun isValidXMasPattern(window: List<Char>): Boolean {
    val pattern = Regex("(XMAS|SAMX)")
    val match = pattern.find(window.joinToString(""))
    return match != null
}

fun isValidMasMasPattern(grid: List<List<Char>>): Boolean {
    val pattern = Regex("(M.M.A.S.S|S.S.A.M.M|S.M.A.S.M|M.S.A.M.S)")
    val match = pattern.find(grid.flatten().joinToString(""))
    return match != null
}

fun main() {
    /*
    Some samples on how XMAS can look:
    ..X...
    .SAMX.
    .A..A.
    XMAS.S
    .X....
     */
    fun part1(input: Array<CharArray>): Int {
        val windowSize = 4
        val allWindows = extractVerticalWindows(input, windowSize) + extractHorizontalWindows(input, windowSize) + extractDiagonalLeftTopToRightBottomWindows(input, windowSize) + extractDiagonalLeftBottomToRightTopWindows(input, windowSize)
        return allWindows.count { window ->
            isValidXMasPattern(window)
        }
    }


    fun part2(input: Array<CharArray>): Int {
        val x = 3
        val y = 3
        val allGrids = extractXYGrids(input, x, y)
        return allGrids.count { grid ->
            isValidMasMasPattern(grid)
        }
    }

    val testInput = readInputTo2DArray("Day04_test")
    check(part1(testInput) == 18)
    check(part2(testInput) == 9)

    // Read the input from the `src/Day01.txt` file.
    val input = readInputTo2DArray("Day04")
    part1(input).println()
    part2(input).println()
}