package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val (workflows, ratings) = buildModel()
    part1(workflows, ratings)
    part2(workflows)
}

private fun part1(workflows: Map<String, Workflow>, ratings: List<PartRating>) {
    val result = ratings.filter { rating ->
        var result: WorkflowResult = WorkflowResult.Next("in")
        while (result is WorkflowResult.Next) result = workflows.getValue(result.workflow).evaluate(rating)
        result is WorkflowResult.Accepted
    }.sumOf(PartRating::score)
    println(result)
}

private fun part2(workflows: Map<String, Workflow>) {
    var sum = 0L
    val queue = ArrayDeque<Pair<String, Map<String, IntRange>>>()
    queue.addLast("in" to mapOf("x" to 1..4000, "m" to 1..4000, "a" to 1..4000, "s" to 1..4000))
    while (queue.isNotEmpty()) {
        val (name, ranges) = queue.removeFirst()
        var current = ranges
        val workflow = workflows.getValue(name)
        for (condition in workflow.conditions) {
            if (condition.rating in current) {
                val currentRange = current.getValue(condition.rating)
                val conditionRange = condition.range
                val intersection = currentRange.intersect(conditionRange)
                val accepted = current + Pair(condition.rating, intersection)
                when (condition.result) {
                    is WorkflowResult.Accepted -> sum += accepted.values.fold(1L) { acc, n -> acc * n.size }
                    is WorkflowResult.Next -> queue.addLast(condition.result.workflow to accepted)
                    else -> {}
                }
                val leftover = when (intersection.last) {
                    currentRange.last -> currentRange.first..<intersection.first
                    else -> intersection.last + 1..currentRange.last
                }
                current = current + Pair(condition.rating, leftover)
            } else when (condition.result) {
                is WorkflowResult.Accepted -> sum += current.values.fold(1L) { acc, n -> acc * n.size }
                is WorkflowResult.Next -> queue.addLast(condition.result.workflow to current)
                else -> {}
            }
        }
    }
    println(sum)
}

private fun buildModel(isSample: Boolean = false): Pair<Map<String, Workflow>, List<PartRating>> {
    readInput(2023, 19, isSample).useLines { sequence ->
        val lines = sequence.iterator()
        val workflows = buildMap {
            while (true) {
                val line = lines.next()
                val workflow = if (line.isNotEmpty()) line.toWorkflow() else break
                put(workflow.name, workflow)
            }
        }
        val ratings = buildList {
            while (lines.hasNext()) add(lines.next().toPartRating())
        }
        return workflows to ratings
    }
}

private fun String.toWorkflow(): Workflow {
    val name = substringBefore('{')
    val conditionRegex = "([xmas])([><])(\\d+):(\\w+)".toRegex()
    val conditions = substringAfter('{').dropLast(1).split(",").map { c ->
        val match = conditionRegex.matchEntire(c)
        val (part, condition, value, next) = match?.groupValues?.drop(1) ?: listOf("", "", "", c)
        val range = when (condition) {
            ">" -> value.toInt() + 1..4000
            "<" -> 1..<value.toInt()
            else -> 1..4000
        }
        val result = when (next) {
            "A" -> WorkflowResult.Accepted
            "R" -> WorkflowResult.Rejected
            else -> WorkflowResult.Next(next)
        }
        Workflow.Condition(part, range, result)
    }
    return Workflow(name, conditions)
}

private fun String.toPartRating(): PartRating {
    val ratings = substring(1, lastIndex).split(",").associate {
        val (k, v) = it.split("=")
        k to v.toInt()
    }
    return PartRating(ratings)
}

private class PartRating(val values: Map<String, Int>) {
    val score get() = values.values.sum()
}

private class Workflow(val name: String, val conditions: List<Condition>) {
    fun evaluate(rating: PartRating): WorkflowResult {
        return conditions.firstNotNullOf { condition ->
            val partValue = rating.values.getOrDefault(condition.rating, 1)
            condition.result.takeIf { partValue in condition.range }
        }
    }

    data class Condition(val rating: String, val range: IntRange, val result: WorkflowResult)
}

private sealed interface WorkflowResult {
    data object Accepted : WorkflowResult
    data object Rejected : WorkflowResult
    data class Next(val workflow: String) : WorkflowResult
}
