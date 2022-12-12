import java.io.File

fun main() {
    part1()
    part2()
}

fun part1() {
    var max = 0
    var sum = 0
    File("input.txt").forEachLine { line ->
        if (line.isEmpty()) {
            max = maxOf(max, sum)
            sum = 0
        } else sum += line.toInt()
    }
    println(max)
}

fun part2() {
    val top = IntArray(3)
    fun place(number: Int) {
        if (number > top[0]) {
            top[2] = top[1]
            top[1] = top[0]
            top[0] = number
        } else if (number > top[1]) {
            top[2] = top[1]
            top[1] = number
        } else if (number > top[2]) {
            top[2] = number
        }
    }
    var sum = 0
    File("input.txt").forEachLine { line ->
        if (line.isEmpty()) {
            place(sum)
            sum = 0
        } else sum += line.toInt()
    }
    println(top.sum())
    // Could also be as simple as sorted().takeLast(3)
}

main()