fun main() {

    fun part1(matrix: Array<CharArray>): Int {
        val matrixAfterSimulation = simulate(matrix.map { it.clone() }.toTypedArray())
        return countX(matrixAfterSimulation)
    }

    fun part2(matrix: Array<CharArray>): Int {
        return countObstructionPositions(matrix.map { it.clone() }.toTypedArray())
    }

    val testInput = readInputTo2DArray("Day06_test")
    check(part1(testInput) == 41)
    check(part2(testInput) == 6)

    // Read the input from the `src/Day01.txt` file.
    val input = readInputTo2DArray("Day06")
    part1(input).println()
    part2(input).println()
}

enum class Direction {
    UP, LEFT, RIGHT, DOWN;

    companion object {
        fun fromChar(char: Char): Direction? {
            return when (char) {
                '^' -> UP
                'v' -> DOWN
                '<' -> LEFT
                '>' -> RIGHT
                else -> null
            }
        }
    }
}

private fun findPositionAndDirection(matrix: Array<CharArray>): Pair<Pair<Int, Int>, Direction>? {
    for (y in matrix.indices) {
        for (x in matrix[y].indices) {
            val char = matrix[y][x]
            if (Direction.fromChar(char) != null) {
                return Pair(Pair(x, y), Direction.fromChar(char)!!)
            }
        }
    }

    return null
}

fun simulate(matrix: Array<CharArray>): Array<CharArray> {
    val positionAndDirection = findPositionAndDirection(matrix)
    var (pos, direction) = positionAndDirection!!
    val nRows = matrix.size
    val nCols = matrix[0].size

    // Mark the starting position
    matrix[pos.second][pos.first] = 'X'

    while (true) {
        val nextPos = move(pos, direction)

        // Check if the next position is within bounds
        if (nextPos.second < 0 || nextPos.first < 0 || nextPos.second >= nRows || nextPos.first >= nCols) {
            break // Reached the outer boundary
        }

        // Check if the next position is a blocking element
        if (matrix[nextPos.second][nextPos.first] == '#') {
            direction = turnRight(direction) // Turn right 90 degrees
        } else {
            // Move to the next position
            pos = nextPos
            matrix[pos.second][pos.first] = 'X'
        }
    }

    return matrix
}

fun countX(matrix: Array<CharArray>): Int {
    return matrix.sumOf { row -> row.count { it == 'X' } }
}

fun move(position: Pair<Int, Int>, direction: Direction): Pair<Int, Int> {
    return when (direction) {
        Direction.UP -> position.copy(second = position.second - 1)
        Direction.DOWN -> position.copy(second = position.second + 1)
        Direction.LEFT -> position.copy(first = position.first - 1)
        Direction.RIGHT -> position.copy(first = position.first + 1)
    }
}

fun turnRight(direction: Direction): Direction {
    return when (direction) {
        Direction.UP -> Direction.RIGHT
        Direction.RIGHT -> Direction.DOWN
        Direction.DOWN -> Direction.LEFT
        Direction.LEFT -> Direction.UP
    }
}

fun countObstructionPositions(matrix: Array<CharArray>): Int {
    val guardInfo = findPositionAndDirection(matrix)
        ?: throw IllegalArgumentException("No guard position found.")
    val startPos = guardInfo.first
    val startDir = guardInfo.second

    val nRows = matrix.size
    val nCols = matrix[0].size
    val validObstructionPositions = mutableSetOf<Pair<Int, Int>>()

    // Simulates the guard's path and checks for a loop considering position and direction
    fun simulateGuardPath(matrix: Array<CharArray>, startPos: Pair<Int, Int>, startDir: Direction): Boolean {
        val visited = mutableSetOf<Triple<Int, Int, Direction>>() // Track visited positions with direction
        var position = startPos
        var direction = startDir

        while (true) {
            val currentState = Triple(position.first, position.second, direction)
            if (visited.contains(currentState)) return true // Loop detected
            visited.add(currentState)

            val nextPos = move(position, direction)
            if (nextPos.first < 0 || nextPos.second < 0 || nextPos.second >= nRows || nextPos.first >= nCols) {
                return false // Out of bounds, no loop
            }
            if (matrix[nextPos.second][nextPos.first] == '#') {
                direction = turnRight(direction) // Turn right
            } else {
                position = nextPos // Move forward
            }
        }
    }

    for (y in matrix.indices) {
        for (x in matrix[y].indices) {
            if (matrix[y][x] == '.' && (x to y) != startPos) {
                // Temporarily place an obstruction
                matrix[y][x] = '#'

                // Check if it creates a loop
                if (simulateGuardPath(matrix, startPos, startDir)) {
                    validObstructionPositions.add(x to y)
                }

                // Remove the obstruction
                matrix[y][x] = '.'
            }
        }
    }

    return validObstructionPositions.size
}