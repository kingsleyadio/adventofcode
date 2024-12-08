package com.kingsleyadio.adventofcode.y2024

data class Index(val x: Int, val y: Int)
operator fun Index.plus(other: Index) = Index(x + other.x, y + other.y)
operator fun Index.minus(other: Index) = Index(x - other.x, y - other.y)

data class Rect(val x: IntRange, val y: IntRange)
operator fun Rect.contains(other: Index) = other.y in y && other.x in x
