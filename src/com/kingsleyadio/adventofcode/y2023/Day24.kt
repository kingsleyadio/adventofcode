package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.gcd
import com.kingsleyadio.adventofcode.util.readInput
import java.math.BigDecimal
import java.math.BigInteger
import kotlin.math.abs
import kotlin.math.sqrt

fun main() {
    val isSample = false
    val input = readInput(2023, 24, isSample).useLines { lines ->
        buildList {
            for (line in lines) {
                val (p, v) = line.split(" @ ")
                val (px, py, pz) = p.split(", ").map { it.trim().toBigDecimal() }
                val (vx, vy, vz) = v.split(", ").map { it.trim().toBigDecimal() }
                add(Vec3(px, py, pz) to Vec3(vx, vy, vz))
            }
        }
    }
    part1(input, isSample)
    part2e(input)
}

@Suppress("SameParameterValue")
private fun part1(input: List<Pair<Vec3, Vec3>>, isSample: Boolean) {
    val range = when {
        isSample -> 7.toBigDecimal()..27.toBigDecimal()
        else -> 200_000_000_000_000.toBigDecimal()..400_000_000_000_000.toBigDecimal()
    }
    val lines = input.map { (p, v) ->
        val p1 = Point(p.x, p.y)
        val p2 = Point(p.x + v.x, p.y + v.y)
        Line(p1, p2)
    }
    var counter = 0
    for (i in 0..<lines.lastIndex) {
        val line = lines[i]
        for (j in i + 1..lines.lastIndex) {
            val other = lines[j]
            val (cx, cy) = findIntersection(line, other) ?: continue
            if (cx in range && cy in range && line.hasFuture(cx, cy) && other.hasFuture(cx, cy)) counter++
        }
    }
    println(counter)
}

// credits: https://github.com/elizarov/AdventOfCode2023/blob/main/src/Day24_2.kt
private fun part2e(input: List<Pair<Vec3, Vec3>>) {
    data class Range(val pi: LongRange, val v: Long)

    fun checkTimes(coord: String, ps: List<Long>, vs: List<Long>): Long {
        val n = ps.size
        check(vs.size == n)

        val minV = -1000L
        val maxV = 1000L
        val minP = 0L
        val maxP = 1_000_000_000_000_000L
        check(minV < vs.min() && maxV > vs.max())
        check(minP < ps.min() && maxP > ps.max())
        val vss = vs.zip(ps).groupBy { it.first }.mapValues { e -> e.value.map { it.second }.toSet() }
        val rs = ArrayList<Range>()

        tailrec fun gcd(x: BigInteger, y: BigInteger): BigInteger = if (y == BigInteger.ZERO) x else gcd(y, x % y)
        fun lcm(x: BigInteger, y: BigInteger) = x * y / gcd(x, y)
        fun BigInteger.floorDiv(d: BigInteger): BigInteger =
            if (this >= BigInteger.ZERO) divide(d) else -(-this + d - BigInteger.ONE).divide(d)

        fun Long.floorDiv(d: BigInteger): BigInteger = toBigInteger().floorDiv(d)
        fun Long.mod(d: BigInteger): BigInteger = toBigInteger().mod(d)
        fun modRoundUp(x: Long, m: BigInteger, r: BigInteger): BigInteger =
            (x.floorDiv(m) + if (x.mod(m) <= r) BigInteger.ZERO else BigInteger.ONE) * m + r

        fun modRoundDn(x: Long, m: BigInteger, r: BigInteger): BigInteger =
            (x.floorDiv(m) - if (x.mod(m) >= r) BigInteger.ZERO else BigInteger.ONE) * m + r

        vloop@ for (v in minV..maxV) {
            val p1 = vs.withIndex().filter { v < it.value }.maxOfOrNull { ps[it.index] } ?: minP
            val p2 = vs.withIndex().filter { v > it.value }.minOfOrNull { ps[it.index] } ?: maxP
            if (p1 > p2) continue
            var pmod = BigInteger.ONE
            var prem = BigInteger.ZERO
            var p1r = p1
            var p2r = p2
            for (i in 0..<n) {
                val pi = ps[i]
                val vi = vs[i]
                if (v == vi) {
                    val p0 = vss[v]?.singleOrNull() ?: continue@vloop
                    if (p0 !in p1r..p2r) continue@vloop
                    p1r = p0
                    p2r = p0
                    continue
                }
                // t_meet = (p - pi) / (vi - v)
                val d = abs(vi - v).toBigInteger()
                val r = pi.mod(d)
                val pmod2 = lcm(pmod, d)
                var prem2 = prem
                while (prem2 < pmod2) {
                    if (prem2.remainder(d) == r) break
                    prem2 += pmod
                    if (prem2 >= pmod2) continue@vloop
                    if (prem2 > p2r.toBigInteger()) continue@vloop
                }
                pmod = pmod2
                prem = prem2
                val p1n = modRoundUp(p1r, pmod, prem)
                val p2n = modRoundDn(p2r, pmod, prem)
                if (p1n > p2n) continue@vloop
                check(p1n >= p1r.toBigInteger())
                check(p2n <= p2r.toBigInteger())
                p1r = p1n.toLong()
                p2r = p2n.toLong()
            }
            println("$coord -> $p1r .. $p2r, v = $v")
            rs += Range(p1r..p2r, v)
        }
        println("$coord: ${rs.size} ranges")
        return rs.single().pi.first
    }

    val x = checkTimes("x", input.map { (p, _) -> p.x.toLong() }, input.map { (_, v) -> v.x.toLong() })
    val y = checkTimes("y", input.map { (p, _) -> p.y.toLong() }, input.map { (_, v) -> v.y.toLong() })
    val z = checkTimes("z", input.map { (p, _) -> p.z.toLong() }, input.map { (_, v) -> v.z.toLong() })

    println(x + y + z)
}

private fun part2(input: List<Pair<Vec3, Vec3>>) {
    // 12 18 19 20 20

    // 20 18 20 19 12
    // 21 15 12  9  6
    //  1  3  4  5  6


    // 19, 13, 30 @ -2,  1, -2
    // x = -2n+19 => 2n = 19-x => n = (19-x)/2
    // y = n+13 => n = y-13 => y = (19-x)/2 + 13
    // 18, 19, 22 @ -1, -1, -2
    val minx = input.minOf { (p, v) -> p.x }
    val miny = input.minOf { (p, v) -> p.y }
    val minz = input.minOf { (p, v) -> p.z }
    val maxx = input.maxOf { (p, v) -> p.x }
    val maxy = input.maxOf { (p, v) -> p.y }
    val maxz = input.maxOf { (p, v) -> p.z }
    println("${minx..maxx}, ${miny..maxy}, ${minz..maxz}")
    println("${maxx - minx}, ${maxy - miny}, ${maxz - minz}")

    val mvx = input.minOf { (_, v) -> v.x }
    val mvy = input.minOf { (_, v) -> v.y }
    val mvz = input.minOf { (_, v) -> v.z }
    val maxvx = input.maxOf { (_, v) -> v.x }
    val maxvy = input.maxOf { (_, v) -> v.y }
    val maxvz = input.maxOf { (_, v) -> v.z }
    println()
    println("${mvx..maxvx}, ${mvy..maxvy}, ${mvz..maxvz}")
    println("${maxvx - mvx}, ${maxvy - mvy}, ${maxvz - mvz}")

    println()
    val velocities = input.map { (_, v) -> sqrt((v.x * v.x + v.y * v.y + v.z * v.z).toDouble()) }
    println(velocities)
    println("Size: ${velocities.size}")
    val uniques = velocities.distinct()
    println("Distinct: ${uniques.size}")
    println(uniques.sorted())
    input.flatMap { (p, _) -> listOf(p.y) }.map { it.toLong() }.reduce { acc, n -> gcd(acc, n) }.also(::println)
}

private fun findIntersection(c: Line, d: Line): Point? {
    val denominator = (c.a.x - c.b.x) * (d.a.y - d.b.y) - (c.a.y - c.b.y) * (d.a.x - d.b.x)
    if (denominator == BigDecimal.ZERO) return null // never intersect
    val nx = (c.a.x * c.b.y - c.a.y * c.b.x) * (d.a.x - d.b.x) - (d.a.x * d.b.y - d.a.y * d.b.x) * (c.a.x - c.b.x)
    val ny = (c.a.x * c.b.y - c.a.y * c.b.x) * (d.a.y - d.b.y) - (d.a.x * d.b.y - d.a.y * d.b.x) * (c.a.y - c.b.y)
    return Point(nx / denominator, ny / denominator)
}

private data class Vec3(val x: BigDecimal, val y: BigDecimal, val z: BigDecimal)
private data class Point(val x: BigDecimal, val y: BigDecimal)
private data class Line(val a: Point, val b: Point)

private fun Line.hasFuture(x: BigDecimal, y: BigDecimal): Boolean {
    return (x - a.x).signum() == (b.x - a.x).signum() && (y - a.y).signum() == (b.y - a.y).signum()
}
