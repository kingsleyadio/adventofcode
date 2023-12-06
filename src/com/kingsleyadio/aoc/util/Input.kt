package util

import java.io.BufferedReader

fun readInput(year: Int, day: Int): BufferedReader {
    val resource = "/$year/day${"%02d".format(day)}.txt"
    return readResource(resource)
}

fun readResource(resource: String): BufferedReader {
    val url = Resource.javaClass.getResource(resource) ?: error("Resource '$resource' not found")
    return url.openStream().bufferedReader()
}

private object Resource
