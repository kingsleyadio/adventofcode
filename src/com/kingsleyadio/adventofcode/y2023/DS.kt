package com.kingsleyadio.adventofcode.y2023

data class Index(val x: Int, val y: Int)
operator fun Index.plus(other: Index) = Index(x + other.x, y + other.y)

data class Path(val to: Index, val cost: Int)
