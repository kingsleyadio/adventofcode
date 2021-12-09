import java.io.File

fun solution(input: List<Int>, timeLeft: Int): Long {
    val cache = hashMapOf<String, Long>()
    fun memo(life: Int, timeLeft: Int, func: (Int, Int) -> Long): Long {
        val key = "$life-$timeLeft"
        return cache.getOrPut(key) { func(life, timeLeft) }
    }

    fun headCount(life: Int, timeLeft: Int): Long {
        if (timeLeft <= life) return 1
        val newTimeLeft = timeLeft - life - 1
        return memo(6, newTimeLeft, ::headCount) + memo(8, newTimeLeft, ::headCount)
    }

    return input.fold(0) { acc, life -> acc + memo(life, timeLeft, ::headCount) }
}

fun main() {
    val input = File("input.txt").useLines { lines ->
        lines.first().split(",").map { it.toInt() }
    }
    println(solution(input, 80))
    println(solution(input, 256))
}

main()
