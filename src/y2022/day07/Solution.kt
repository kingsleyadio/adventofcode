package y2022.day07

import java.io.File

fun main() {
    val fs = buildFs()
    part1(fs)
    part2(fs)
}

fun part1(root: Fs) {
    var partialSum = 0
    root.sumWithATwist(100_000, true) { partialSum += it }
    println(partialSum)
}

fun part2(root: Fs) {
    val usedSpace = root.size
    val availableSpace = 70_000_000 - usedSpace
    val neededSpace = 30_000_000 - availableSpace

    var toDelete = Int.MAX_VALUE
    root.sumWithATwist(neededSpace, false) { toDelete = minOf(toDelete, it) }
    println(toDelete)
}

fun Fs.sumWithATwist(limit: Int, checkBelow: Boolean, onHit: (Int) -> Unit): Int {
    return when (this) {
        is Fs.Directory -> {
            val sum = children().sumOf { it.sumWithATwist(limit, checkBelow, onHit) }
            if (checkBelow) {
                if (sum <= limit) onHit(sum)
            } else {
                if (sum >= limit) onHit(sum)
            }
            sum
        }

        is Fs.File -> size
    }
}


private fun buildFs(): Fs {
    val root = Fs.Directory("/", null)
    var current: Fs.Directory = root
    File("input.txt").forEachLine { line ->
        val splits = line.split(" ")
        if (splits[0] == "$") { // Command
            if (splits[1] == "cd") current = when (val destination = splits[2]) {
                "/" -> root
                ".." -> current.parent!!
                else -> current.child(destination) as Fs.Directory
            }
        } else if (splits[0] == "dir") { // Directory listing
            current.addChild(Fs.Directory(splits[1], current))
        } else { // File listing
            current.addChild(Fs.File(splits[1], current, splits[0].toInt()))
        }
    }
    return root
}

sealed interface Fs {
    val name: String
    val parent: Directory?
    val size: Int

    class Directory(
        override val name: String,
        override val parent: Directory?,
    ) : Fs {
        private val children = hashMapOf<String, Fs>()

        fun addChild(child: Fs) = children.put(child.name, child)
        fun child(name: String) = children.getValue(name)
        fun children() = children.values
        override val size get() = children.values.sumOf { it.size }
    }

    class File(
        override val name: String,
        override val parent: Directory?,
        override val size: Int
    ) : Fs
}
