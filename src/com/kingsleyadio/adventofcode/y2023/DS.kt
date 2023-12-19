package com.kingsleyadio.adventofcode.y2023

data class Index(val x: Int, val y: Int)
operator fun Index.plus(other: Index) = Index(x + other.x, y + other.y)

data class Path(val to: Index, val cost: Int)


operator fun IntRange.contains(range: IntRange) = range.first in this && range.last in this
val IntRange.size: Int get() = last - first + 1
fun IntRange.intersect(other: IntRange): IntRange {
    return if (this in other) this
    else if (other in this) other
    else if (last < other.first) IntRange.EMPTY
    else if (first > other.first) first..other.last
    else other.first..last
}
