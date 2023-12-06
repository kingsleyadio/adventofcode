package com.kingsleyadio.adventofcode.util

import java.io.BufferedReader

fun readInput(year: Int, day: Int, sample: Boolean = false): BufferedReader {
    val dayId = "%02d".format(day)
    val suffix = if (sample) "-sample" else ""
    val resource = "/$year/day$dayId$suffix.txt"
    return readResource(resource)
}

fun readResource(resource: String): BufferedReader {
    val url = Resource.javaClass.getResource(resource) ?: error("Resource '$resource' not found")
    return url.openStream().bufferedReader()
}

private object Resource
