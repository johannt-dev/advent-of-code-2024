fun main() {

    fun part1(list: List<String>): Long {
        // Step1: Convert List to hard-drive format (including dots etc)#
        val fileDrive = toFileDrive(list)
        // Step2: sort the drive
        val sortedFileDrive = sortFileDrive(fileDrive)
        // Step3: Calc checksum
        return calcCheckSum(sortedFileDrive)
    }

    fun part2(list: List<String>): Long {
        // Step1: Convert List to hard-drive format (including dots etc)#
        val fileDrive = toFileDrive(list)
        // Step2: sort the drive
        val compactedDrive = compactFileDrive(fileDrive)
        // Step3: Calc checksum
        return calcCheckSum(compactedDrive)
    }

    val testInput = readInputToCharArray("Day09_test")
    check(part1(testInput) == 1928L)
    check(part2(testInput) == 2858L)

    // Read the input from the `src/Day01.txt` file.
    val input = readInputToCharArray("Day09")
    part1(input).println()
    part2(input).println()
}

fun toFileDrive(list: List<String>): List<String> {
    val fileDrive = mutableListOf<String>()
    var id = 0

    list.forEachIndexed { index, value ->
        val count = value.toInt()
        fileDrive.addAll(List(count) { if (index % 2 == 0) id.toString() else "." })
        if (index % 2 == 0) id++
    }
    return fileDrive
}

fun sortFileDrive(fileDrive: List<String>): List<String> {
    val fileDriveSorted = fileDrive.toMutableList()
    var endIndex = fileDrive.lastIndex

    fileDriveSorted.forEachIndexed { index, value ->
        if (value == ".") {
            while (endIndex > index && fileDriveSorted[endIndex] == ".") {
                endIndex-- // Skip trailing dots
            }
            if (endIndex > index) {
                fileDriveSorted[index] = fileDriveSorted[endIndex] // Move the valid element
                fileDriveSorted[endIndex] = "." // Mark the moved position as dot
                endIndex-- // Update endIndex
            }
        }
    }

    return fileDriveSorted
}

fun compactFileDrive(fileDrive: List<String>): List<String> {
    val mutableDrive = fileDrive.toMutableList()

    val files = mutableDrive.filter { it != "." }.map { it.toInt() }.distinct().sortedDescending()

    for (file in files) {
        val fileIndices = mutableDrive.withIndex().filter { it.value != "." && it.value.toInt() == file }.map { it.index }
        val fileLength = fileIndices.size

        fun calculateFreeSpans(): MutableList<Pair<Int, Int>> {
            val freeSpans = mutableListOf<Pair<Int, Int>>() // List of (start, end) of free spans
            var start = -1
            for (i in mutableDrive.indices) {
                if (mutableDrive[i] == ".") {
                    if (start == -1) start = i
                } else if (start != -1) {
                    freeSpans.add(start to i - 1)
                    start = -1
                }
            }
            if (start != -1) freeSpans.add(start to mutableDrive.lastIndex)
            return freeSpans
        }

        val freeSpans = calculateFreeSpans()

        val targetSpan = freeSpans.firstOrNull { (start, end) -> end - start + 1 >= fileLength && start < fileIndices.first() }

        if (targetSpan != null) {
            val (start, end) = targetSpan
            for (i in start until start + fileLength) {
                mutableDrive[i] = file.toString()
            }

            // clear old file position
            for (i in fileIndices) mutableDrive[i] = "."
        }
    }
    return mutableDrive
}

fun calcCheckSum(fileDrive: List<String>): Long {
    return fileDrive.withIndex()
        .filter { it.value != "." }
        .sumOf { it.index.toLong() * it.value.toLong() }
}