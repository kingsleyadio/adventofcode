import java.io.File

fun part1(graph: Map<String, List<String>>, start: String, end: String): Int {
    var count = 0
    fun travel(current: String, visited: MutableSet<String>) {
        if (current == end) { count++; return }
        for (dest in graph.getValue(current)) {
            if (dest !in visited) {
                if (dest == dest.lowercase()) visited.add(dest)
                travel(dest, visited)
                visited.remove(dest)
            }
        }
    }
    travel(start, hashSetOf(start))
    return count
}

fun part2(graph: Map<String, List<String>>, start: String, end: String): Int {
    var count = 0
    var secondVisit: String? = null
    fun travel(current: String, visited: MutableSet<String>) {
        if (current == end) { count++; return }
        for (dest in graph.getValue(current)) {
            if (dest !in visited || secondVisit == null) {
                if (dest == dest.lowercase()) {
                    if (dest == start) continue
                    else if (dest !in visited) visited.add(dest)
                    else secondVisit = dest
                }
                travel(dest, visited)
                if (secondVisit == dest) secondVisit = null
                else visited.remove(dest)
            }
        }
    }
    travel(start, hashSetOf(start))
    return count
}

fun main() {
    val graph = buildMap<String, MutableList<String>> {
        File("input.txt").forEachLine { line ->
            val (current, dest) = line.split("-")
            getOrPut(current) { arrayListOf() }.add(dest)
            getOrPut(dest) { arrayListOf() }.add(current)
        }
    }
    println(part1(graph, "start", "end"))
    println(part2(graph, "start", "end"))
}

main()
