fun main() {
    fun part1(map: List<List<Int>>): Int {
        val trailheads = findTrailheads(map)

        val scoresPerTrailhead = mutableListOf<Pair<Int, Set<Pair<Int, Int>>>>()

        // Now find all unique scores for each trailhead
        trailheads.forEachIndexed { index, (ir, ic) ->
            val visited = mutableSetOf<Pair<Int, Int>>() // Scoped to each trailhead
            val terminalPositions = canHike(ir to ic, 0, map, visited)
            scoresPerTrailhead.add(index to terminalPositions)
        }
        return calcTotalPathes(scoresPerTrailhead)
    }



    fun part2(map: List<List<Int>>): Int {
        val trailheads = findTrailheads(map)
        var totalRating = 0

        trailheads.forEach { trailhead ->
            val visited = mutableSetOf<Pair<Int, Int>>()
            val distinctTrails = canHikeDistinctTrails(trailhead, 0, map, visited, emptyList())
            totalRating += distinctTrails.size
            println("Trailhead $trailhead: Rating = ${distinctTrails.size}")
        }

        return totalRating
    }

    val testInput = readInputTo2DIntList("Day10_test")
    check(part1(testInput) == 36)
    check(part2(testInput) == 81)

    val input = readInputTo2DIntList("Day10")
    part1(input).println()
    part2(input).println()
}

fun findTrailheads(map: List<List<Int>>): List<Pair<Int, Int>> {
    val trailheads = mutableListOf<Pair<Int, Int>>()
    map.forEachIndexed { ir, row ->
        row.forEachIndexed { ic, height ->
            if (height == 0) {
                trailheads.add(ir to ic)
            }
        }
    }
    return trailheads
}

fun canHike(
    position: Pair<Int, Int>,
    height: Int,
    map: List<List<Int>>,
    visited: MutableSet<Pair<Int, Int>>
): Set<Pair<Int, Int>> {
    val (row, col) = position
    val directions = listOf(
        Pair(-1, 0), // up
        Pair(1, 0),  // down
        Pair(0, -1), // left
        Pair(0, 1)   // right
    )

    if (height == 9) {
        return setOf(position)
    }

    visited.add(position)

    val results = mutableSetOf<Pair<Int, Int>>()
    for ((dr, dc) in directions) {
        val newRow = row + dr
        val newCol = col + dc
        val newPosition = newRow to newCol

        if (newRow in map.indices && newCol in map[newRow].indices && newPosition !in visited) {
            val neighborHeight = map[newRow][newCol]
            if (neighborHeight == height + 1) {
                results += canHike(newPosition, neighborHeight, map, visited)
            }
        }
    }

    return if (results.isNotEmpty()) results else setOf()
}



fun calcTotalPathes(pathes: List<Pair<Int, Set<Pair<Int, Int>>>>): Int {
    return pathes.sumOf { (_, terminalPositions) ->
        terminalPositions.size
    }
}

fun canHikeDistinctTrails(
    position: Pair<Int, Int>,
    height: Int,
    map: List<List<Int>>,
    visited: MutableSet<Pair<Int, Int>>,
    currentTrail: List<Pair<Int, Int>>
): Set<List<Pair<Int, Int>>> {
    val (row, col) = position
    val directions = listOf(
        Pair(-1, 0), // up
        Pair(1, 0),  // down
        Pair(0, -1), // left
        Pair(0, 1)   // right
    )

    if (height == 9) {
        return setOf(currentTrail + position)
    }

    visited.add(position)

    val results = mutableSetOf<List<Pair<Int, Int>>>()
    for ((dr, dc) in directions) {
        val newRow = row + dr
        val newCol = col + dc
        val newPosition = newRow to newCol

        if (newRow in map.indices && newCol in map[newRow].indices && newPosition !in visited) {
            val neighborHeight = map[newRow][newCol]
            if (neighborHeight == height + 1) {
                results += canHikeDistinctTrails(newPosition, neighborHeight, map, visited, currentTrail + position)
            }
        }
    }

    visited.remove(position)

    return results
}