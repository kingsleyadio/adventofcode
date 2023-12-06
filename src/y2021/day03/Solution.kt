package y2021.day03

import util.readInput

fun main() {
    part1()
    part2()
}

fun part1() {
    var size = 0
    var array = IntArray(0)
    readInput(2021, 3).forEachLine { line ->
        size++
        if (array.isEmpty()) array = IntArray(line.length) // adjust array size
        array.indices.forEach { i -> if (line[i] == '1') array[i]++ }
    }
    val halfSize = size shr 1
    val gamma = array.foldIndexed(0) { index, acc, item ->
        val add = if (item > halfSize) 1 shl (array.lastIndex - index) else 0
        acc + add
    }
    val epsilon = gamma xor ((1 shl array.size) - 1)

    println(gamma * epsilon)
}

fun part2() {
    readInput(2021, 3).useLines { lines ->
        val list = lines.map { it.map(Char::digitToInt) }.toList()
        val o2 = reduce(list, mostSignificant = true)
        val co2 = reduce(list, mostSignificant = false)
        println(o2 * co2)
    }
}

fun reduce(binaryList: List<List<Int>>, mostSignificant: Boolean): Int {
    var result = binaryList
    var bitPosition = 0
    while (result.size > 1) { // _guaranteed_ to not overflow
        val (ones, zeros) = result.partition { it[bitPosition] == 1 }
        val halfSize = (result.size + 1) shr 1
        result = when {
            mostSignificant -> if (ones.size >= halfSize) ones else zeros
            else -> if (ones.size < halfSize) ones else zeros
        }
        bitPosition++
    }
    // return result.single().joinToString("").toInt(2)
    val winner = result.single()
    return winner.foldIndexed(0) { index, acc, item ->
        val add = if (item == 1) 1 shl (winner.lastIndex - index) else 0
        acc + add
    }
}
