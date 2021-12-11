import java.io.File

fun part1(): Int {
    File("input.txt").useLines { lines ->
        val knownLengths = listOf(2, 3, 4, 7)
        return lines
            .map { it.substringAfterLast("| ") }
            .map { it.split(" ") }
            .sumOf { it.count { digit -> digit.length in knownLengths } }
    }
}

fun part2(): Int {
    var result = 0
    File("input.txt").forEachLine { line ->
        val (numberString, puzzleString) = line.split(" | ")
        val numbers = numberString.split(" ").map { it.toSet() }
        val decodedList = arrayOfNulls<Set<Char>>(10)
        val decodedMap = hashMapOf<Set<Char>, Int>()
        val other = arrayListOf<Set<Char>>()
        for (number in numbers) {
            val value = when (number.size) {
                7 -> 8
                4 -> 4
                3 -> 7
                2 -> 1
                else -> { other.add(number); continue }
            }
            decodedMap[number] = value
            decodedList[value] = number
        }
        for (number in other) {
            val value = when (number.size) {
                5 -> when {
                    number.intersect(decodedList[4]!!).size == 2 -> 2
                    number.containsAll(decodedList[1]!!) -> 3
                    else -> 5
                }
                else -> when {
                    number.containsAll(decodedList[4]!!) -> 9
                    !number.containsAll(decodedList[1]!!) -> 6
                    else -> 0
                }
            }
            decodedMap[number] = value
            decodedList[value] = number
        }

        result += puzzleString.split(" ").map { it.toSet() }.fold(0) { acc, s -> acc * 10 + decodedMap.getValue(s) }
    }
    return result
}

println(part1())
println(part2())
