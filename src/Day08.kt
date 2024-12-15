import kotlin.math.absoluteValue

fun main() {

    fun part1(matrix: Array<CharArray>): Int {
        val antennas = findAntennas(matrix)
        val antinodes = calcAntinodes(antennas, matrix[0].size, matrix.size)
        val antinodeCount = countUniqueAntinodes(antinodes)
        return antinodeCount
    }

    fun part2(matrix: Array<CharArray>): Int {
        val antennas = findAntennas(matrix)
        val antinodes = calcAntinodesInLine(antennas, matrix[0].size, matrix.size)
        val antinodeCount = countUniqueAntinodes(antinodes)
        return antinodeCount
    }



    val testInput = readInputTo2DArray("Day08_test")
    check(part1(testInput) == 14)
    check(part2(testInput) == 34)

    // Read the input from the `src/Day01.txt` file.
    val input = readInputTo2DArray("Day08")
    part1(input).println()
    part2(input).println()
}

fun findAntennas(map: Array<CharArray>): List<Triple<Char, Int, Int>> {
    val antennas = mutableListOf<Triple<Char, Int, Int>>()

    for (y in map.indices) {
        for (x in map[y].indices) {
            val char = map[y][x]
            if (char != '.') {
                antennas.add(Triple(char, x, y))
            }
        }
    }

    return antennas
}

fun calcAntinodes(antennas: List<Triple<Char, Int, Int>>, mapSizeX: Int, mapSizeY: Int): List<Pair<Int, Int>> {
    val antinodePositions = mutableListOf<Pair<Int, Int>>()

    for (i in antennas.indices) {
        for (j in i+1 until antennas.size) {
            val (freq1, x1, y1) = antennas[i]
            val (freq2, x2, y2) = antennas[j]
            println("Checking ($freq1,$x1,$y1) against ($freq2,$x2,$y2)")

            if (freq1 == freq2) {
                val (dx, dy) = Pair(x1 - x2, y1 - y2)
                // Antinodes on either side of the pair
                val midX1 = x1 + dx
                val midY1 = y1 + dy
                val midX2 = x2 - dx
                val midY2 = y2 - dy

                println("Freq matches, add nodes at ($midX1, $midY1) and ($midX2, $midY2)" )

                if (midX1 in 0 until mapSizeX && midY1 in 0 until mapSizeY) {
                    antinodePositions.add(Pair(midX1, midY1))
                }
                if (midX2 in 0 until mapSizeX && midY2 in 0 until mapSizeY) {
                    antinodePositions.add(Pair(midX2, midY2))
                }

            }
        }
    }

    return antinodePositions
}

fun calcAntinodesInLine(antennas: List<Triple<Char, Int, Int>>, mapSizeX: Int, mapSizeY: Int): List<Pair<Int, Int>> {
    val antinodePositions = mutableListOf<Pair<Int, Int>>()

    fun gcd(a: Int, b: Int): Int = if (b == 0) a else gcd(b, a % b)

    for (i in antennas.indices) {
        for (j in i+1 until antennas.size) {
            val (freq1, x1, y1) = antennas[i]
            val (freq2, x2, y2) = antennas[j]
            println("Checking ($freq1,$x1,$y1) against ($freq2,$x2,$y2)")

            if (freq1 == freq2) {
                val (dx, dy) = Pair(x1 - x2, y1 - y2)

                // Normalize the direction vector (dx, dy) to get the step
                val gcd = gcd(dx.absoluteValue, dy.absoluteValue)
                val stepX = dx / gcd
                val stepY = dy / gcd

                // Extend backward from (x1, y1)
                var currentX = x1
                var currentY = y1
                while (currentX in 0 until mapSizeX && currentY in 0 until mapSizeY) {
                    antinodePositions.add(Pair(currentX, currentY))
                    currentX -= stepX
                    currentY -= stepY
                }

                // Extend forward from (x1, y1)
                currentX = x1
                currentY = y1
                while (currentX in 0 until mapSizeX && currentY in 0 until mapSizeY) {
                    antinodePositions.add(Pair(currentX, currentY))
                    currentX += stepX
                    currentY += stepY
                }

            }
        }
    }

    return antinodePositions
}

fun countUniqueAntinodes(antinodes: List<Pair<Int, Int>>): Int {
    return antinodes.toSet().count()
}

