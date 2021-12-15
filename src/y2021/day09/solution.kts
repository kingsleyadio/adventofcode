import java.io.File

fun part1(input: List<List<Int>>): Int {
    var result = 0
    for (i in input.indices) {
        val inner = input[i]
        for (j in inner.indices) {
            val current = inner[j]
            val adjacents = adjacentPoints(i, j, input).map { (y, x) -> input[y][x] }
            if (adjacents.all { it > current }) result += current + 1
        }
    }
    return result
}

fun part2(input: List<List<Int>>): Int {
    fun expanse(y: Int, x: Int): Int {
        fun evaluate(y: Int, x: Int, lookup: MutableSet<String>): Int {
            lookup.add("$y#$x")
            val value = input[y][x]
            return 1 + adjacentPoints(y, x, input)
                .asSequence()
                .filter { (newY, newX) -> input[newY][newX] in value+1..8 }
                .filterNot { (newY, newX) -> "$newY#$newX" in lookup }
                .sumOf { (newY, newX) -> evaluate(newY, newX, lookup) }
        }
        return evaluate(y, x, hashSetOf())
    }
    val basins = arrayListOf<Int>()
    for (i in input.indices) {
        val inner = input[i]
        for (j in inner.indices) {
            val current = inner[j]
            val adjacents = adjacentPoints(i, j, input).map { (y, x) -> input[y][x] }
            if (adjacents.all { it > current }) basins.add(expanse(i, j))
        }
    }
    return basins.sorted().takeLast(3).fold(1) { acc, n -> acc * n }
}

fun adjacentPoints(y: Int, x: Int, input: List<List<Int>>): List<IntArray> {
    return buildList {
        if (y > 0) add(intArrayOf(y - 1, x))
        if (x < input[0].lastIndex) add(intArrayOf(y, x + 1))
        if (y < input.lastIndex) add(intArrayOf(y + 1, x))
        if (x > 0) add(intArrayOf(y, x - 1))
    }
}

fun main() {
    val input = File("input.txt").useLines { lines ->
        lines.map { line -> line.toCharArray().map { it.digitToInt() } }.toList()
    }
    println(part1(input))
    println(part2(input))
}

main()
