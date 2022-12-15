package y2022.day08

import java.io.File

fun main() {
    val grid = buildGrid()
    part1(grid)
    part2(grid)
}

fun part1(grid: List<List<Int>>) {
    // We could also use a boolean array as a shadow grid and do the counting explicitly
    val visibleCells = hashSetOf<String>()
    for (i in 1 until grid.lastIndex) {
        val row = grid[i]
        var maxLeft = -1
        for (left in row.indices) {
            if (row[left] > maxLeft) {
                visibleCells.add("$i-$left")
                maxLeft = row[left]
            }
        }
        var maxRight = -1
        for (right in row.lastIndex downTo 0) {
            if (row[right] > maxRight) {
                visibleCells.add("$i-$right")
                maxRight = row[right]
            }
        }
    }
    for (i in 1 until grid[0].lastIndex) {
        var maxTop = -1
        for (top in 0..grid.lastIndex) {
            if (grid[top][i] > maxTop) {
                visibleCells.add("$top-$i")
                maxTop = grid[top][i]
            }
        }
        var maxBottom = -1
        for (bottom in grid.lastIndex downTo 0) {
            if (grid[bottom][i] > maxBottom) {
                visibleCells.add("$bottom-$i")
                maxBottom = grid[bottom][i]
            }
        }
    }
    val visibleCount = visibleCells.size + 4
    println(visibleCount)
}

fun part2(grid: List<List<Int>>) {
    // We could do this more elegantly and with less code using a bruteforce approach
    // However, this approach is supposed to be slightly more optimal than the bruteforce

    val lefts = Array(grid.size) { IntArray(grid[0].size) { 0 } }
    val tops = Array(grid.size) { IntArray(grid[0].size) { 0 } }
    val rights = Array(grid.size) { IntArray(grid[0].size) { 0 } }
    val bottoms = Array(grid.size) { IntArray(grid[0].size) { 0 } }
    for (i in 1 until grid.lastIndex) {
        val row = grid[i]
        var maxIndex = 0
        for (left in 1 until row.lastIndex) {
            lefts[i][left] = when {
                row[left] > row[maxIndex] -> left.also { maxIndex = left }
                row[left] == row[maxIndex] -> (left - maxIndex).also { maxIndex = left }
                else -> {
                    var jumpIndex = left - 1;
                    var value = 1
                    while (row[left] > row[jumpIndex]) {
                        value += lefts[i][jumpIndex]
                        jumpIndex -= lefts[i][jumpIndex]
                    }
                    value
                }
            }
        }
        maxIndex = row.lastIndex
        for (right in row.lastIndex - 1 downTo 1) {
            rights[i][right] = when {
                row[right] > row[maxIndex] -> (row.lastIndex - right).also { maxIndex = right }
                row[right] == row[maxIndex] -> (maxIndex - right).also { maxIndex = right }
                else -> {
                    var jumpIndex = right + 1;
                    var value = 1
                    while (row[right] > row[jumpIndex]) {
                        value += rights[i][jumpIndex]
                        jumpIndex += rights[i][jumpIndex]
                    }
                    value
                }
            }
        }
    }

    for (i in 1 until grid[0].lastIndex) {
        var maxIndex = 0
        for (top in 1 until grid.lastIndex) {
            tops[top][i] = when {
                grid[top][i] > grid[maxIndex][i] -> top.also { maxIndex = top }
                grid[top][i] == grid[maxIndex][i] -> (top - maxIndex).also { maxIndex = top }
                else -> {
                    var jumpIndex = top - 1;
                    var value = 1
                    while (grid[top][i] > grid[jumpIndex][i]) {
                        value += tops[jumpIndex][i]
                        jumpIndex -= tops[jumpIndex][i]
                    }
                    value
                }
            }
        }
        maxIndex = grid.lastIndex
        for (bottom in grid.lastIndex - 1 downTo 1) {
            bottoms[bottom][i] = when {
                grid[bottom][i] > grid[maxIndex][i] -> (grid.lastIndex - bottom).also { maxIndex = bottom }
                grid[bottom][i] == grid[maxIndex][i] -> (maxIndex - bottom).also { maxIndex = bottom }
                else -> {
                    var jumpIndex = bottom + 1;
                    var value = 1
                    while (grid[bottom][i] > grid[jumpIndex][i]) {
                        value += bottoms[jumpIndex][i]
                        jumpIndex += bottoms[jumpIndex][i]
                    }
                    value
                }
            }
        }
    }
    var result = 0
    for (i in grid.indices) {
        for (j in grid[i].indices) {
            result = maxOf(result, lefts[i][j] * rights[i][j] * tops[i][j] * bottoms[i][j])
        }
    }
    println(result)
}

fun buildGrid(): List<List<Int>> = buildList {
    File("input.txt").forEachLine { add(it.map(Char::digitToInt)) }
}

main()
