package y2022.day13

import java.io.File

fun main() {
    val input = parseInput()
    part1(input)
    part2(input)
}

fun part1(input: List<*>) {
    val result = input
        .asSequence()
        .windowed(2, 2)
        .withIndex()
        .sumOf { (index, list) ->
            val (left, right) = list
            if (checkIsRightOrder(left, right) == Result.PASS) index + 1 else 0
        }
    println(result)
}

fun part2(input: List<*>) {
    val modified = input.toMutableList()
    val dp1 = listOf(listOf(2))
    val dp2 = listOf(listOf(6))
    modified.add(dp1)
    modified.add(dp2)

    modified.sortWith { a, b ->
        when (checkIsRightOrder(a, b)) {
            Result.PASS -> -1
            Result.FAIL -> 1
            else -> 0
        }
    }
    val result = (modified.indexOf(dp1) + 1) * (modified.indexOf(dp2) + 1)
    println(result)
}

fun parseInput(): List<*> = buildList {
    File("input.txt").forEachLine { if (it.isNotEmpty()) add(parseLine(it)) }
}

fun parseLine(line: String): List<*> {
    val stack = ArrayDeque<MutableList<Any>>()
    val sb = StringBuilder()
    for (index in 0 until line.lastIndex) {
        when (val char = line[index]) {
            '[' -> stack.addLast(mutableListOf())
            ']' -> {
                if (sb.isNotEmpty()) {
                    stack.last().add(sb.toString().toInt())
                    sb.clear()
                }
                val list = stack.removeLast()
                stack.last().add(list)
            }

            ',' -> if (sb.isNotEmpty()) {
                stack.last().add(sb.toString().toInt())
                sb.clear()
            }

            else -> sb.append(char)
        }
    }
    if (sb.isNotEmpty()) stack.last().add(sb.toString().toInt())
    return stack.last()
}

fun checkIsRightOrder(left: Any?, right: Any?): Result {
    return if (left is List<*> && right is List<*>) {
        for (index in left.indices) {
            if (right.lastIndex < index) return Result.FAIL
            val tempResult = checkIsRightOrder(left[index], right[index])
            if (tempResult != Result.UNDEFINED) return tempResult
        }
        if (right.size > left.size) Result.PASS else Result.UNDEFINED
    } else if (left is Int && right is Int) when {
        left < right -> Result.PASS
        left > right -> Result.FAIL
        else -> Result.UNDEFINED
    } else if (left is Int) checkIsRightOrder(listOf(left), right)
    else checkIsRightOrder(left, listOf(right))
}

enum class Result {
    PASS, FAIL, UNDEFINED
}

main()
