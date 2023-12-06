package y2022.day11

import util.readInput

fun main() {
    solution(loadMonkeys(), 20, true)
    solution(loadMonkeys(), 10_000, false)
}

fun solution(monkeys: List<Monkey>, rounds: Int, divisibleBy3: Boolean) {
    val monkeyOps = IntArray(monkeys.size)
    val limit = monkeys.fold(1) { acc, m -> acc * m.testDivisor } // needed for part 2
    repeat(rounds) {
        for (index in monkeys.indices) {
            monkeys[index].inspect(divisibleBy3, limit) { receiver, item ->
                monkeys[receiver].receive(item)
                monkeyOps[index]++
            }
        }
    }
    val monkeyBusiness = monkeyOps.sortedDescending().take(2).fold(1L) { acc, m -> acc * m }
    println(monkeyBusiness)
}

fun loadMonkeys(): List<Monkey> {
    val monkeys = mutableListOf<Monkey>()
    readInput(2022, 11).useLines { lineSequence ->
        val lines = lineSequence.iterator()
        while (lines.hasNext()) {
            lines.next()
            val startingItems = lines.next().substringAfterLast(": ").split(", ").map { it.toLong() }
            val op = lines.next().substringAfterLast("= ")
            val testDivisor = lines.next().substringAfterLast(" ").toInt()
            val trueMonkey = lines.next().substringAfterLast(" ").toInt()
            val falseMonkey = lines.next().substringAfterLast(" ").toInt()
            if (lines.hasNext()) lines.next()
            val newMonkey = Monkey(startingItems, op, testDivisor, trueMonkey, falseMonkey)
            monkeys.add(newMonkey)
        }
    }
    return monkeys
}

class Monkey(
    startingItems: List<Long>,
    opString: String,
    val testDivisor: Int,
    private val trueMonkey: Int,
    private val falseMonkey: Int,
) {

    private val itemsInHand = startingItems.toMutableList()
    private val inspector = { item: Long ->
        val op = opString.split(" ")
        val left = if (op[0] == "old") item else op[0].toLong()
        val right = if (op[2] == "old") item else op[2].toLong()
        when (op[1]) {
            "+" -> left + right
            "*" -> left * right
            else -> 0
        }
    }

    fun receive(item: Long) = itemsInHand.add(item)

    fun inspect(divisibleBy3: Boolean, mod: Int, onThrow: (monkey: Int, item: Long) -> Unit) {
        itemsInHand.forEach { item ->
            val inspection = inspector(item)
            val worryLevel = if (divisibleBy3) inspection / 3 else inspection % mod
            if (worryLevel % testDivisor == 0L) onThrow(trueMonkey, worryLevel)
            else onThrow(falseMonkey, worryLevel)
        }
        itemsInHand.clear()
    }
}
