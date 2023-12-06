package y2021.day04

import java.io.File

fun solution(numbers: List<Int>, boards: MutableList<Board>, exitFirst: Boolean): Int {
    for (number in numbers) {
        val iterator = boards.iterator()
        for (board in iterator) {
            board.asSequence().flatten().filter { it.number == number }.forEach { it.isMarked = true }
            val (win, unmarkedSum) = checkWin(board)
            if (win) {
                iterator.remove()
                if (exitFirst || boards.isEmpty()) return number * unmarkedSum
            }
        }
    }
    return 0
}

class Cell(val number: Int, var isMarked: Boolean)
typealias Board = List<List<Cell>>

fun main() {
    val (numbers, boards) = readInput()
    println(solution(numbers, boards, exitFirst = true))
    // Continue part 2 with the same boards
    println(solution(numbers, boards, exitFirst = false))
}

fun readInput(): Pair<List<Int>, MutableList<Board>> = File("input.txt").useLines { lines ->
    val iterator = lines.iterator()
    val numbers = iterator.next().split(",").map { it.toInt() }

    val boards = mutableListOf<Board>()
    while (iterator.hasNext()) {
        iterator.next()
        val board = buildList {
            repeat(5) {
                val line = iterator.next().trim().split(" +".toRegex()).map { Cell(it.toInt(), false) }
                add(line)
            }
        }
        boards.add(board)
    }

    numbers to boards
}

fun checkWin(board: Board): Pair<Boolean, Int> {
    var win = false
    for (index in 0..4) {
        // Check rows and columns
        if (board[index].all { it.isMarked } || board.all { it[index].isMarked }) {
            win = true
            break
        }
    }
    return win to when {
        win -> board.asSequence().flatten().filterNot { it.isMarked }.sumOf { it.number }
        else -> 0
    }
}
