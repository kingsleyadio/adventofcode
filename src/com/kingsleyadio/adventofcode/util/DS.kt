package com.kingsleyadio.adventofcode.util

import kotlin.math.abs

data class Index(val x: Int, val y: Int)

operator fun Index.plus(other: Index) = Index(x + other.x, y + other.y)
operator fun Index.minus(other: Index) = Index(x - other.x, y - other.y)
operator fun Index.times(multiplier: Int) = Index(x * multiplier, y * multiplier)

fun Index.manhattanDistanceTo(other: Index) = abs(x - other.x) + abs(y - other.y)

data class Rect(val x: IntRange, val y: IntRange)

operator fun Rect.contains(other: Index) = other.y in y && other.x in x

object Directions {
    @JvmField val N = Index(0, -1)
    @JvmField val S = Index(0, 1)
    @JvmField val E = Index(1, 0)
    @JvmField val W = Index(-1, 0)
    @JvmField val NE = Index(1, -1)
    @JvmField val NW = Index(-1, -1)
    @JvmField val SE = Index(1, 1)
    @JvmField val SW = Index(-1, 1)

    @JvmField val Cardinal = listOf(N, E, S, W)
    @JvmField val InterCardinal = listOf(NE, SE, SW, NW)
    @JvmField val Compass = Cardinal + InterCardinal
}
