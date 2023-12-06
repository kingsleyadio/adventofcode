package y2021.day22

import util.readInput

fun part1(commands: List<Command>): Int {
    val bound = -50..50
    val ons = hashSetOf<P3>()
    for ((action, cuboid) in commands) {
        if (cuboid.x !in bound || cuboid.y !in bound || cuboid.z !in bound) continue
        when (action) {
            Action.ON -> cuboid.cubes().forEach { ons.add(it) }
            Action.OFF -> cuboid.cubes().forEach { ons.remove(it) }
        }
    }
    return ons.size
}

fun part2(commands: List<Command>): Long {
    val bound = -50..50
    val within = commands.filter { (_, cuboid) -> cuboid.x in bound && cuboid.y in bound && cuboid.z in bound }
    return within.foldIndexed(0L) { index, acc, (action, cuboid) ->
        println(acc)
        val intersects = commands.subList(0, index).map { cuboid.intersect(it.second) }
        val currentVolume = if (action == Action.ON) cuboid.volume() else 0
        acc - sum(intersects) + currentVolume
    }
}

fun sum(cuboids: List<Cuboid>): Long {
    return when (cuboids.size) {
        0 -> 0L
        1 -> cuboids[0].volume()
        else -> {
            val last = cuboids.last()
            val intersects = cuboids.subList(0, cuboids.lastIndex).map { it.intersect(last) }.filterNot { it.isNone() }
            sum(cuboids.subList(0, cuboids.lastIndex)) - sum(intersects) + last.volume()
        }
    }
}


fun main() {
    val commands = readInput(2021, 22).useLines { lines -> lines.map(::parse).toList() }
    println(part1(commands))
    println(part2(commands))
}

fun parse(line: String): Command {
    val (c, rest) = line.split(" ")
    val (x, y, z) = rest.split(",").map { ch -> ch.substringAfter("=").split("..").map { it.toInt() } }
    val action = if (c == "on") Action.ON else Action.OFF
    val cuboid = Cuboid(x[0]..x[1], y[0]..y[1], z[0]..z[1])
    return Command(action, cuboid)
}

typealias P3 = List<Int>
typealias Command = Pair<Action, Cuboid>


enum class Action { ON, OFF }
data class Cuboid(val x: IntRange, val y: IntRange, val z: IntRange)

fun Cuboid.cubes() = sequence { for (xx in x) for (yy in y) for (zz in z) yield(listOf(xx, yy, zz)) }
fun Cuboid.volume(): Long = x.size.toLong() * y.size * z.size
fun Cuboid.isNone() = x == IntRange.EMPTY && y == IntRange.EMPTY && z == IntRange.EMPTY
fun Cuboid.intersect(other: Cuboid) = Cuboid(x.intersect(other.x), y.intersect(other.y), z.intersect(other.z))

operator fun IntRange.contains(range: IntRange) = range.first in this && range.last in this
val IntRange.size: Int get() = last - first + 1
fun IntRange.intersect(other: IntRange): IntRange {
    return if (this in other) this
    else if (other in this) other
    else if (last < other.first) IntRange.EMPTY
    else if (first > other.first) first..other.last
    else other.first..last
}
