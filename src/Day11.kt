import java.math.BigInteger

fun main() {
    fun part1(stones: List<BigInteger>): Long {
        var stonesAfterStep = stones
        for (i in 0..24) {
            println("Step $i: ${stonesAfterStep.size}")
            stonesAfterStep = applyRules(stonesAfterStep)
        }
        println("Final size: ${stonesAfterStep.size}")
        return stonesAfterStep.size.toLong()
    }

    fun part2Optimized(stones: List<BigInteger>): BigInteger {
        var stoneCounts = stones.groupingBy { it }.eachCount()
            .mapValues { (_, count) -> BigInteger.valueOf(count.toLong()) }
            .toMutableMap()

        for (i in 0..74) {
            stoneCounts = applyRulesOptimized(stoneCounts)
            println("Step $i: Total Stones = ${stoneCounts.values.sumOf { it }}")
        }

        return stoneCounts.values.sumOf { it }
    }


    /*
    This results in a OOM :)

    fun part2(stones: List<BigInteger>): Long {
        var stonesAfterStep = stones
        for (i in 0..74) {
            println("Step $i: ${stonesAfterStep.size}")
            stonesAfterStep = applyRules(stonesAfterStep)
        }
        println("Final size: ${stonesAfterStep.size}")
        return stonesAfterStep.size.toLong()
    }
    */

    val testInput = readInputToBigIntList("Day11_test")
    check(part1(testInput) == 55312L)

    val input = readInputToBigIntList("Day11")
    part1(input).println()
    //part2(input).println()
    part2Optimized(input).println()
}

// ruleset task 1
/*
 *  Rule1: n == 0 -> n = 1
 *  Rule2: n.digitCount%2 == 0 -> n1 = n.take(digitCount/2), n2 = n.takeRight(digitCount/2)
 *  Rule3: else: n = n * 2024
 */
fun applyRules(stones: List<BigInteger>): List<BigInteger> {
    val stonesAfterStep = mutableListOf<BigInteger>()
    stones.forEach { stone ->
        when {
            stone == BigInteger.ZERO -> stonesAfterStep.add(BigInteger.ONE) // Rule 1
            stone.length() % 2 == 0 -> { // Rule 2
                val half = stone.length() / 2
                stonesAfterStep.add(stone.takeLeft(half))
                stonesAfterStep.add(stone.takeRight(half))
            }
            else -> stonesAfterStep.add(stone * BigInteger("2024")) // Rule 3
        }
    }
    return stonesAfterStep
}

fun applyRulesOptimized(stoneCounts: Map<BigInteger, BigInteger>): MutableMap<BigInteger, BigInteger> {
    val newCounts = mutableMapOf<BigInteger, BigInteger>()

    stoneCounts.forEach { (stone, count) ->
        when {
            stone == BigInteger.ZERO -> {
                newCounts[BigInteger.ONE] = newCounts.getOrDefault(BigInteger.ONE, BigInteger.ZERO) + count
            }
            stone.length() % 2 == 0 -> {
                val half = stone.length() / 2
                val left = stone.takeLeft(half)
                val right = stone.takeRight(half)
                newCounts[left] = newCounts.getOrDefault(left, BigInteger.ZERO) + count
                newCounts[right] = newCounts.getOrDefault(right, BigInteger.ZERO) + count
            }
            else -> {
                val multiplied = stone * BigInteger("2024")
                newCounts[multiplied] = newCounts.getOrDefault(multiplied, BigInteger.ZERO) + count
            }
        }
    }

    return newCounts
}


fun BigInteger.length(): Int = this.toString().length

fun BigInteger.takeLeft(n: Int): BigInteger {
    val str = this.toString()
    return if (n <= str.length) BigInteger(str.take(n)) else this
}

fun BigInteger.takeRight(n: Int): BigInteger {
    val str = this.toString()
    return if (n <= str.length) BigInteger(str.takeLast(n)) else this
}

