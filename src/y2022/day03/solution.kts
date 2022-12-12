import java.io.File

fun main() {
    part1()
    part2()
}

fun part1() {
    var result = 0
    File("input.txt").forEachLine { line ->
        val first = line.substring(0, line.length / 2)
        val second = line.substring(line.length / 2)

        val array = BooleanArray(52)
        for (char in first) array[valueOfChar(char) - 1] = true
        for (char in second) {
            val value = valueOfChar(char)
            if (array[value - 1]) {
                result += value
                array[value - 1] = false
            }
        }
    }
    println(result)
}

fun part2() {
    var result = 0
    File("input.txt").useLines { lines ->
        lines.windowed(3, 3).forEach { (first, second, third) ->
            val array = IntArray(52)
            for (char in first) array[valueOfChar(char) - 1] = 1
            for (char in second) {
                val value = valueOfChar(char)
                if (array[value - 1] == 1) array[value - 1] = 2
            }
            for (char in third) {
                val value = valueOfChar(char)
                if (array[value - 1] == 2) {
                    result += value
                    array[value - 1] = 0
                }
            }
        }
    }
    println(result)
}


fun valueOfChar(char: Char) = when {
    char.isUpperCase() -> char - 'A' + 27
    else -> char - 'a' + 1
}

main()