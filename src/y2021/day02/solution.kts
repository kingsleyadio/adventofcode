import java.io.File

fun part1() {
    var x = 0
    var y = 0
    File("input.txt").forEachLine {
        val  (command, arg) = it.split(" ", limit = 2)
        when (command) {
            "forward" -> x += arg.toInt()
            "up" -> y -= arg.toInt()
            "down" -> y += arg.toInt()
        }
    }
    println(x * y)
}

fun part2() {
    var x = 0
    var y = 0
    var aim = 0
    File("input.txt").forEachLine {
        val  (command, arg) = it.split(" ", limit = 2)
        val input = arg.toInt()
        when (command) {
            "up" -> aim -= input
            "down" -> aim += input
            "forward" -> { x += input; y += input * aim }
        }
    }
    println(x * y)
}

part1()
part2()
