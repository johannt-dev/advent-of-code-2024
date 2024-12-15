
fun main() {
    fun part1(input: List<String>): Long {
        val clawMachines = input.toClawMachines()
        var totalPrize = 0L
        var totalTokensSpent = 0L
        clawMachines.forEach { clawMachine ->
            val (tokens, winnable) = calculateMinimumCostBruteForce(clawMachine, maxSteps = 100)
            if(winnable) {
                totalTokensSpent += tokens
                totalPrize ++
            }
        }
        return totalTokensSpent
    }

    fun part2(input: List<String>): Long {
        val clawMachines = input.toClawMachines()
        var totalPrize = 0L
        var totalTokensSpent = 0L
        clawMachines.forEach { clawMachine ->
            val (tokens, winnable) = calculateMinimumCostMathematical(clawMachine, 10000000000000L)
            println("Machine: $tokens, $winnable")
            if(winnable) {
                totalTokensSpent += tokens
                totalPrize++
            }
        }
        return totalTokensSpent
    }

    val testInput = readInput("Day13_test")
    check(part1(testInput) == 480L)

    val input = readInput("Day13")
    part1(input).println()
    part2(input).println()
}

// some data classes to make it more readable
data class Prize(val x: Int, val y: Int)
data class Button(val dx: Int, val dy: Int)
data class ClawMachine(val prize: Prize, val a: Button, val b: Button)


fun List<String>.toClawMachines(): List<ClawMachine> {
    val clawMachines = mutableListOf<ClawMachine>()

    var index = 0
    while (index < this.size) {
        if (this[index].isBlank()) {
            index++
            continue
        }

        // Extract Button A data
        val buttonARegex = "Button A: X\\+([\\d]+), Y\\+([\\d]+)".toRegex()
        val buttonAData = buttonARegex.find(this[index++])?.destructured
            ?: throw IllegalArgumentException("Invalid Button A format at line ${index - 1}")
        val buttonA = Button(dx = buttonAData.component1().toInt(), dy = buttonAData.component2().toInt())

        // Extract Button B data
        val buttonBRegex = "Button B: X\\+([\\d]+), Y\\+([\\d]+)".toRegex()
        val buttonBData = buttonBRegex.find(this[index++])?.destructured
            ?: throw IllegalArgumentException("Invalid Button B format at line ${index - 1}")
        val buttonB = Button(dx = buttonBData.component1().toInt(), dy = buttonBData.component2().toInt())

        // Extract Prize data
        val prizeRegex = "Prize: X=([\\d]+), Y=([\\d]+)".toRegex()
        val prizeData = prizeRegex.find(this[index++])?.destructured
            ?: throw IllegalArgumentException("Invalid Prize format at line ${index - 1}")
        val prize = Prize(x = prizeData.component1().toInt(), y = prizeData.component2().toInt())

        // Create ClawMachine object and add to list
        clawMachines.add(ClawMachine(prize = prize, a = buttonA, b = buttonB))
    }

    return clawMachines
}

fun calculateMinimumCostBruteForce(machine: ClawMachine, maxSteps: Long = 100): Pair<Long, Boolean> {
    var minCost = Long.MAX_VALUE
    var canWin = false

    outer@ for(pressA in 0..maxSteps) {
        for(pressB in 0..maxSteps) {
            val x = pressA * machine.a.dx + pressB * machine.b.dx
            val y = pressA * machine.a.dy + pressB * machine.b.dy

            if (x.toInt() == machine.prize.x && y.toInt() == machine.prize.y) {
                val cost = pressA * 3 + pressB
                minCost = minOf(minCost, cost)
                canWin = true
                continue@outer
            }
        }
    }
    return Pair(minCost, canWin)
}

fun calculateMinimumCostMathematical(machine: ClawMachine, addition: Long): Pair<Long, Boolean> {
    val prizeX = machine.prize.x + addition
    val prizeY = machine.prize.y + addition

    // Integer math for calculating b and a
    val denominator = machine.a.dx * machine.b.dy - machine.a.dy * machine.b.dx
    if (denominator == 0) return Pair(Long.MAX_VALUE, false) // No solution possible if determinant is zero

    val b = (machine.a.dx * prizeY - machine.a.dy * prizeX) / denominator
    if ((machine.a.dx * prizeY - machine.a.dy * prizeX) % denominator != 0L) {
        return Pair(Long.MAX_VALUE, false) // Ensure integer solution for b
    }

    val a = (prizeY - machine.b.dy * b) / machine.a.dy
    if ((prizeY - machine.b.dy * b) % machine.a.dy != 0L) {
        return Pair(Long.MAX_VALUE, false) // Ensure integer solution for a
    }

    // Validate solution
    val solvable = a * machine.a.dx + b * machine.b.dx == prizeX
    return if (solvable) Pair(a * 3 + b, true) else Pair(Long.MAX_VALUE, false)
}