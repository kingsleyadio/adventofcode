import java.io.File

fun question1() {
//    val input = File("input.txt").readLines().map { it.toInt() }
//    var answer = 0
//    for (i in 1..input.lastIndex) {
//        if (input[i] > input[i - 1]) answer++
//    }
//    println(answer)
    val answer = File("input.txt").useLines { sequence ->
        sequence.map { it.toInt() }
            .windowed(2)
            .count { (first, second) -> second > first }
    }
    println(answer)
}

fun question2() {
//    val input = File("input.txt").readLines().map { it.toInt() }
//    var answer = 0
//    for (i in 3..input.lastIndex) {
//        if (input[i] > input[i - 3]) answer++
//    }
    val answer = File("input.txt").useLines { sequence ->
        sequence.map { it.toInt() }
            .windowed(4)
            .count { it.last() > it.first() }
    }
    println(answer)
}

question1()
question2()
