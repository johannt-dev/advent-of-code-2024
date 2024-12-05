import kotlin.io.path.Path
import kotlin.io.path.readText

fun main() {

    fun part1(rules: List<Pair<Int, Int>>, updates: List<List<Int>>): Int {
        return updates.sumOf { update ->
            // If all rules are satisfied, return the middle index value; otherwise, return 0
            if (allRulesSatisfiedForUpdate(rules, update)) update[update.size / 2] else 0
        }
    }


    fun part2(rules: List<Pair<Int, Int>>, updates: List<List<Int>>): Int {
        return updates.sumOf { update ->
            val ordered = reorderUpdate(rules, update)
            if(allRulesSatisfiedForUpdate(rules, update)) 0 else ordered[ordered.size/2]
        }
    }

    val testInput = readInputToSections("Day05_test")
    if (testInput != null) {
        check(part1(testInput.first, testInput.second) == 143)
    }
    if (testInput != null) {
        check(part2(testInput.first, testInput.second) == 123)
    }

    // Read the input from the `src/Day01.txt` file.
    val input = readInputToSections("Day05")
    if (input != null) {
        part1(input.first, input.second).println()
    }
    if (input != null) {
        part2(input.first, input.second).println()
    }
}

fun reorderUpdate(rules: List<Pair<Int, Int>>, update: List<Int>): List<Int> {
    // Create a map of precedence based on the rules
    val precedenceMap = mutableMapOf<Int, MutableSet<Int>>()
    for ((x, y) in rules) {
        precedenceMap.computeIfAbsent(x) { mutableSetOf() }.add(y)
    }

    // Perform topological sort to order the elements
    val result = mutableListOf<Int>()
    val visited = mutableSetOf<Int>()
    val inStack = mutableSetOf<Int>()

    fun dfs(node: Int) {
        if (node in visited) return
        if (node in inStack) throw IllegalArgumentException("Cycle detected in rules")

        inStack.add(node)
        precedenceMap[node]?.forEach { neighbor ->
            if (neighbor in update) dfs(neighbor)
        }
        inStack.remove(node)
        visited.add(node)
        result.add(node)
    }

    // Start DFS for all elements in the update
    update.forEach { element ->
        if (element !in visited) dfs(element)
    }

    // Reverse result to get the correct order
    result.reverse()
    return result
}

fun allRulesSatisfiedForUpdate(rules: List<Pair<Int, Int>>, update: List<Int>): Boolean {
    val positions = update.withIndex().associate { it.value to it.index }
    return rules.all { (x, y) ->
        val posX = positions[x]
        val posY = positions[y]
        if (posX == null || posY == null) return@all true
        posX < posY
    }
}

fun readInputToSections(path: String): Pair<List<Pair<Int, Int>>, List<List<Int>>>? {
    val input = Path("src/resources/$path.txt").readText().trim()
    val sections = input.split("\n\n")
    if(sections.size != 2) {
        println("invalid format")
        return null
    }

    // first section
    val pairs = sections[0].lines().map { line ->
        val (first, second) = line.split("|").map { it.toInt() }
        first to second
    }

    // second section
    val lists = sections[1].lines().map { line ->
        line.split(",").map { it.toInt() }
    }

    return Pair(pairs, lists)
}