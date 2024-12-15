import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readText

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/resources/$name.txt").readText().trim().lines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

fun readInputTo2DArray(name: String): Array<CharArray> {
    return Path("src/resources/$name.txt")
        .readText()
        .trim()
        .lines()
        .map { it.toCharArray() }
        .toTypedArray()
}

fun readInputTo2DIntList(name: String): List<List<Int>> {
    return Path("src/resources/$name.txt")
        .readText()
        .trim()
        .lines()
        .map { line ->
            line.map { it.digitToInt() }
        }
}

fun readInputToBigIntList(name: String): List<BigInteger> {
    return Path("src/resources/$name.txt")
        .readText()
        .trim()
        .split(" ")
        .filter { it.isNotEmpty() }
        .map { BigInteger(it) }
}

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

/*
* read input to 1D char array
 */
fun readInputToCharArray(name: String): List<String> {
    return Path("src/resources/$name.txt")
        .readText()
        .trim()
        .split("")
        .filter { it.isNotEmpty() } // Remove empty strings
}