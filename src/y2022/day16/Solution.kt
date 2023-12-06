package y2022.day16

import util.readInput
import java.util.*

fun main() {
//    nav(arrayOf("A", "B", "C"))
    val input = parseInput()
    part1priority(input)
//    part1a(input)
//    part2(input)
}

fun part1priority(input: Map<String, ValveInfo>) {
    val queue = PriorityQueue<Navi> { a, b -> b.value - a.value }
    queue.add(Navi("AA", "AA",0, 0))
    val naviMap = hashMapOf<String, Navi>()
    var timeLeft = 30
    while (queue.isNotEmpty()) {
        if (naviMap.size == input.size) break
        val (from, current, distanceHere, value) = queue.poll()
        println("$from => $current")
        val valveInfo = input.getValue(current)
        val fromNavi = naviMap[from]
        //if (fromNavi != null && fromNavi.next == current && value <= fromNavi.value) continue
        naviMap[from] = Navi(from, current, distanceHere, value)
//        timeLeft -= distanceHere
//        if (current !in path) {
//            timeLeft--
//        }
//        if (current in path) continue
//        if (timeLeft <= 0) break
        for (next in valveInfo.exits) {
            val nextValve = input.getValue(next)
            val distance = shortestDistance(input, current, next).value
            val navValue = (timeLeft - distance - 1) * nextValve.flowRate
            //if (current in naviMap && naviMap.getValue(current).next == next && navValue <= naviMap.getValue(current).value) continue
            queue.add(Navi(current, next, distance, navValue))
        }
    }
    var current = "AA"
    var maxValue = 0
    var time = 30
    while(current in naviMap) {
        val next = naviMap.remove(current)!!
        print("${next.next} ")
        maxValue += (time - next.distance - 1) * input.getValue(next.next).flowRate
        time -= next.distance + 1
        current = next.next
    }
    println(maxValue)
}

data class Navi(val from: String, val next: String, val distance: Int, val value: Int)

fun part1v(input: Map<String, ValveInfo>) {
    var valueSum = 0
    var current = "AA"
    val valvePriority = input.values.sortedByDescending { it.flowRate }
    var timeLeft = 30
    var visitIndex = 0
    val visited = mutableSetOf<String>()
    while (timeLeft > 0 && visited.size < input.size) {
        println("Time: $timeLeft")
        while (valvePriority[visitIndex].id in visited || valvePriority[visitIndex].id == current) visitIndex++
        val potentialNext = valvePriority[visitIndex]
        val (distanceToNext, pathToNext) = shortestDistance(input, current, potentialNext.id)
        val valueOfNext = potentialNext.flowRate * (timeLeft - distanceToNext - 1)
        val valueOfCurrent = input.getValue(current).flowRate * (timeLeft - 1)
        println("Current: $current, Next: ${potentialNext.id}")
        println("D to N: $distanceToNext, P to N: $pathToNext, V of N: $valueOfNext, V of C: $valueOfCurrent")
        if (current !in visited && valueOfCurrent >= 0 && input.getValue(current).flowRate > 0) { // open current
            println("Opening $current")
            visited.add(current)
            valueSum += valueOfCurrent
            timeLeft--
            println("Time: $timeLeft")
        }
        if (potentialNext.flowRate == 0) break // Exhausted all openable valves
        // go towards the next priority
        current = pathToNext.getOrElse(1) { potentialNext.id }
        println("Moving to $current")
        timeLeft--
//        timeLeft -= distanceToNext
//        current = potentialNext.id
//        visitIndex++
    }
    println(visited)
    println(valueSum)
    println("--------------------------------")
}

fun part1ca(input: Map<String, ValveInfo>) {
    val distanceMap = hashMapOf<String, Int>()
    for ((k, _) in input) {
        for ((kk, _) in input) {
            if (k != kk) distanceMap["$k-$kk"] = shortestDistance(input, k, kk).value
        }
    }

    val cache = hashMapOf<String, Int>()
    var cacheHit = 0; var cacheMiss = 0
    fun flowValue(initial: String, path: Array<String>, index: Int, timeLeft: Int): Int {
        if (index == path.size || timeLeft <= 0) return 0
        val current = path[index]
        val previous = path.getOrNull(index - 1) ?: initial
        val distance = distanceMap.getValue("$previous-$current")
        val value = (timeLeft - distance - 1) * input.getValue(current).flowRate
        
        val restKey = path.drop(index).joinToString("-") + "-${timeLeft - distance - 1}"
        if (restKey in cache) cacheHit++
        val restValue = cache.getOrPut(restKey) {
            cacheMiss++
            flowValue(initial, path, index + 1, timeLeft - distance - 1)
        }
        return value + restValue
    }

    val nodes = input.keys.filter { input.getValue(it).flowRate != 0 }.toTypedArray()
    var max = 0
    traverse(nodes, 0) { path -> max = maxOf(max, flowValue("AA", path, 0, 30)) }
    println("Cache hit: $cacheHit, Cache miss: $cacheMiss")
    println(max)
}

fun part1c(input: Map<String, ValveInfo>) {
    val distanceMap = hashMapOf<String, Int>()
    for ((k, _) in input) {
        for ((kk, _) in input) {
            if (k != kk) distanceMap["$k-$kk"] = shortestDistance(input, k, kk).value
        }
    }
    val nodes = input.keys.filter { input.getValue(it).flowRate != 0 }.toTypedArray()
    var max = 0
    traverse(nodes, 0) { path ->
        var current = "AA"
        var timeLeft = 30
        var value = 0
        for (i in path.indices) {
            val n = path[i]
            val distanceToN = distanceMap.getValue("$current-$n")
            val valueToN = (timeLeft - distanceToN - 1) * input.getValue(n).flowRate
            if (valueToN < 0) return@traverse
            value += valueToN
            timeLeft -= distanceToN + 1
            current = n
        }
        max = maxOf(max, value)
    }
    println(max)
}

fun part1(input: Map<String, ValveInfo>) {
    var valueSum = 0
    var current = "AA"
    var timeLeft = 30
    val visited = hashSetOf<String>()
    fun findNext(): Triple<ValveInfo?, Cost, Int> {
        var valve: ValveInfo? = null
        var bestCost = Cost(1_000, emptyList())
        var bestValue = 0
        for ((_, v) in input) {
            if (v.flowRate == 0 || v.id in visited || v.id == current) continue
            val cost = shortestDistance(input, current, v.id)
            val value = v.flowRate * (timeLeft - cost.value - 1)
            println("Value for $current to ${v.id}: $value")
            if (value >= bestValue) {
                bestValue = value
                bestCost = cost
                valve = v
            }
        }
        return Triple(valve, bestCost, bestValue)
    }
    while (timeLeft > 0 && visited.size < input.size) {
        println("Time: $timeLeft")
        val (potentialNext, cost, valueOfNext) = findNext()
        val (distanceToNext, pathToNext) = cost
        val valueOfCurrent = input.getValue(current).flowRate * (timeLeft - 1)
        println("Current: $current, Next: ${potentialNext?.id}")
        println("D to N: $distanceToNext, P to N: $pathToNext, V of N: $valueOfNext, V of C: $valueOfCurrent")
        if (current !in visited && valueOfCurrent >= 0 && input.getValue(current).flowRate > 0) { // open current
            println("Opening $current")
            visited.add(current)
            valueSum += valueOfCurrent
            timeLeft--
            println("Time: $timeLeft")
        }
        // go towards the next priority
        potentialNext ?: break
        current = pathToNext.getOrElse(1) { potentialNext.id }
        println("Moving to $current")
        timeLeft--
//        timeLeft -= distanceToNext
//        current = potentialNext.id
//        visitIndex++
    }
    println(visited)
    println(valueSum)
}

fun part2(input: Map<String, ValveInfo>) {

}

fun shortestDistance(graph: Map<String, ValveInfo>, start: String, end: String): Cost {
    val costs = hashMapOf<String, Cost>()
    val queue = PriorityQueue<Path> { a, b -> a.cost - b.cost }
    queue.add(Path(start, 0, emptyList()))
    while (queue.isNotEmpty()) {
        val (valve, cost, from) = queue.poll()
        if ((costs[valve]?.value ?: -1) in 0..cost) continue
        costs[valve] = Cost(cost, from)
        for (next in graph.getValue(valve).exits) {
            if ((costs[next]?.value ?: -1) >= 0) continue // already fixed cost to this valve
            queue.add(Path(next, cost + 1, from + valve))
        }
    }
    return costs.getValue(end)
}

fun parseInput(): Map<String, ValveInfo> = buildMap {
    val pattern = "Valve ([A-Z]+) has flow rate=([0-9]+); tunnels? leads? to valves? (.*)".toRegex()
    readInput(2022, 16).forEachLine { line ->
        val (valve, rate, ends) = pattern.matchEntire(line)!!.groupValues.drop(1)
        val exits = ends.split(", ")
        put(valve, ValveInfo(valve, rate.toInt(), exits))
    }
}

data class ValveInfo(val id: String, val flowRate: Int, val exits: List<String>)
data class Cost(val value: Int, val path: List<String>)
data class Path(val to: String, val cost: Int, val from: List<String>)


fun traverse(nums: Array<String>, start: Int, onPath: (Array<String>) -> Unit) {
    if (start == nums.size) return onPath(nums)
    for (i in start..nums.lastIndex) {
        nums.swap(start, i)
        traverse(nums, start + 1, onPath)
        nums.swap(start, i)
    }
}

fun <T> Array<T>.swap(src: Int, dst: Int) {
    val temp = get(src)
    set(src, get(dst))
    set(dst, temp)
}
