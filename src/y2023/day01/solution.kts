import java.io.File

fun main() {
    part1()
    part2()
}

fun part1() {
    var sum = 0
    File("input.txt").forEachLine { line ->
        val first = line.first { it.isDigit() }.digitToInt()
        val last = line.last { it.isDigit() }.digitToInt()
        sum += first * 10 + last
    }
    println(sum)
}

fun part2() {
    var sum = 0
    val numbers = listOf("zero", "one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    File("input.txt").forEachLine { line ->
        val first = run {
            val numIndex = line.indexOfFirst { it.isDigit() }
            val numNumber = line[numIndex].digitToInt()
            val view = line.subSequence(0..numIndex)
            var wordNumber = 0
            var wordIndex = numIndex
            numbers.forEachIndexed { index, num -> 
                val lineIndex = view.indexOf(num)
                if (lineIndex in 0..<wordIndex) {
                    wordNumber = index
                    wordIndex = lineIndex
                }
            }
            if (wordIndex < numIndex) wordNumber else numNumber
        }
        val last = run {
            val numIndex = line.indexOfLast { it.isDigit() }
            val numNumber = line[numIndex].digitToInt()
            val view = line.substring(numIndex+1)
            var wordNumber = 0
            var wordIndex = -1
            numbers.forEachIndexed { index, num ->
                val lineIndex = view.lastIndexOf(num)
                if (lineIndex > wordIndex) {
                    wordNumber = index
                    wordIndex = lineIndex
                }
            }
            if (wordIndex + numIndex + 1 > numIndex) wordNumber else numNumber
        }
        sum += first * 10 + last
    }
    println(sum)
}

main()
