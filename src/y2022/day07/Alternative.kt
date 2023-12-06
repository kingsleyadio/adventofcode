package y2022.day07
import java.io.File

fun main() {
    val fsMap = buildFs()
    part1(fsMap)
    part2(fsMap)
}

fun part1(fs: Map<String, Int>) {
    val result = fs.values.filter { it <= 100_000 }.sum()
    println(result)
}

fun part2(fs: Map<String, Int>) {
    val usedSpace = fs.getValue("")
    val availableSpace = 70_000_000 - usedSpace
    val neededSpace = 30_000_000 - availableSpace

    var toDelete = Int.MAX_VALUE
    for ((_, value) in fs) {
        if (value in neededSpace..toDelete) toDelete = value
    }
    println(toDelete)
}

private fun buildFs(): Map<String, Int> {
    val map = hashMapOf("" to 0)
    var current = ""
    File("input.txt").forEachLine { line ->
        val split = line.split(" ")
        if (split[0] == "$") {
            if (split[1] == "cd") {
                current = when (split[2]) {
                    "/" -> ""
                    ".." -> current.substringBeforeLast("/").also { map[it] = map.getValue(it) + map.getValue(current) }
                    else -> current + "/" + split[2]
                }
            }
        } else if (split[0] == "dir") {
            map.getOrPut(current + "/" + split[1]) { 0 }
        } else {
            map[current] = map.getValue(current) + split[0].toInt()
        }
    }
    while (current != "") {
        current = current.substringBeforeLast("/").also { map[it] = map.getValue(it) + map.getValue(current) }
    }

    return map
}
