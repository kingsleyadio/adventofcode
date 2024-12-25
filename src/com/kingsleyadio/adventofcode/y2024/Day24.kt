package com.kingsleyadio.adventofcode.y2024

import com.kingsleyadio.adventofcode.util.*
import java.util.*

fun main() {
    readInput(2024, 24).useLines {
        val lines = it.iterator()
        val values = mutableMapOf<Simple, Boolean>()
        for (line in lines) {
            if (line.isEmpty()) break
            val (gate, value) = line.split(": ")
            values[Simple(gate)] = value == "1"
        }
        var zcount = 0
        val circuits = buildMap<Complex, MutableList<Simple>> {
            for (line in lines) {
                val (a, op, b, _, out) = line.split(" ")
                getOrPut(Complex(Simple(a), op, Simple(b))) { mutableListOf() }.add(Simple(out))
                if (out.startsWith("z")) zcount++
            }
        }
        part1(circuits, values, zcount)
        part2(circuits, zcount)
    }
}

private fun part1(circuits: Map<Complex, List<Simple>>, values: MutableMap<Simple, Boolean>, zcount: Int) {
    val combo = circuits.toMutableMap()
    while (combo.isNotEmpty()) {
        val iterator = combo.iterator()
        for ((operation, out) in iterator) {
            val (a, op, b) = operation
            if (a in values && b in values) {
                require(a is Simple && b is Simple)
                val res = when (op) {
                    "OR" -> values.getValue(a) or values.getValue(b)
                    "AND" -> values.getValue(a) and values.getValue(b)
                    else -> values.getValue(a) xor values.getValue(b)
                }
                for (x in out) values[x] = res
                iterator.remove()
            }
        }
    }
    val result = (zcount - 1 downTo 0).fold(0L) { acc, i ->
        val z = Simple("z%02d".format(i))
        acc * 2 + if (values.getValue(z)) 1 else 0
    }
    println(result)
}

private fun part2(circuits: Map<Complex, List<Simple>>, zcount: Int) {
    val combo = circuits.mapValues { (_, v) -> v.single() }
    val reverse: Map<Operand, Complex> = combo.map { (k, v) -> v to k }.toMap()
    fun alias(complex: Complex) = combo[complex] ?: complex
    fun findCulprit(s: Operand, z: Complex): Simple = when {
        s !is Complex -> s as Simple
        s.a == z.a -> findCulprit(s.b, reverse.getValue(z.b))
        s.a == z.b -> findCulprit(s.b, reverse.getValue(z.a))
        s.b == z.b -> findCulprit(s.a, reverse.getValue(z.a))
        else -> findCulprit(s.a, reverse.getValue(z.b))
    }

    val misplaced = mutableListOf<Simple>()
    var c: Operand = Simple("")
    for (index in 0..<zcount) {
        val number = "%02d".format(index)
        val x = Simple("x$number")
        val y = Simple("y$number")
        val z = Simple("z$number")
        var xxy = alias(Complex(x, "XOR", y))
        val xay = alias(Complex(x, "AND", y))
        val s = if (index == 0) xxy else if (index == zcount - 1) c else alias(Complex(xxy, "XOR", c))
        if (s != z) {
            val zr = reverse.getValue(z)
            when {
                xxy == zr.a -> c = zr.b
                xxy == zr.b -> c = zr.a
                c == zr.a -> xxy = zr.b
                c == zr.b -> xxy = zr.a
            }
            misplaced.add(findCulprit(s, zr))
        }
        val xxyac = alias(Complex(xxy, "AND", c))
        c = (if (index == 0) xay else alias(Complex(xay, "OR", xxyac)))
    }
    println(misplaced.sorted().joinToString(","))
}

private sealed interface Operand

private data class Simple(val value: String) : Operand, Comparable<Simple> {
    override fun toString() = value
    override fun compareTo(other: Simple) = value.compareTo(other.value)
}

private data class Complex(val a: Operand, val op: String, val b: Operand) : Operand {
    private val hash = Objects.hash(setOf(a, b), op)
    override fun hashCode() = hash
    override fun equals(other: Any?) = this === other || other is Complex && op == other.op && hash == other.hash
    operator fun contains(o: Operand) = a == o || b == o
    override fun toString() = "($a $op $b)"
}
