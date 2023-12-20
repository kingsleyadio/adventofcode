package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.lcm
import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    part1(buildModel())
    part2(buildModel())
}

private fun part1(model: Map<String, PulseModule>) {
    val signals = IntArray(2)
    for (i in 1..1000) simulation(model) { _, _, pulse -> signals[if (pulse) 1 else 0]++ }
    val result = signals[0] * signals[1]
    println(result)
}

private fun part2(model: Map<String, PulseModule>) {
    var buttonPresses = 0
    val xToMg = hashMapOf<String, Int>()
    val mg = model.getValue("mg") as ConjunctionModule
    while (true) {
        buttonPresses++
        simulation(model) { from, to, pulse -> if (to == mg.name && pulse) xToMg.putIfAbsent(from, buttonPresses) }
        if (xToMg.size == mg.inputCount) break
    }
    val result = xToMg.values.fold(1L) { acc, n -> lcm(acc, n.toLong()) }
    println(result)
}

private inline fun simulation(model: Map<String, PulseModule>, onSignal: (String, String, Boolean) -> Unit) {
    val queue = ArrayDeque<Triple<String, String, Boolean>>()
    queue.addLast(Triple("button", "broadcaster", false))
    while (queue.isNotEmpty()) {
        val (from, to, pulse) = queue.removeFirst()
        onSignal(from, to, pulse)
        when (val module = model[to] ?: continue) {
            is BroadcastModule -> for (output in module.outputs) queue.addLast(Triple(module.name, output, pulse))
            is FlipFlopModule -> if (pulse.not()) {
                module.isOn = !module.isOn
                for (output in module.outputs) queue.addLast(Triple(module.name, output, module.isOn))
            }
            is ConjunctionModule -> {
                module.states[from] = pulse
                val newPulse = module.states.values.count { it } != module.inputCount
                for (output in module.outputs) queue.addLast(Triple(module.name, output, newPulse))
            }
        }
    }
}

private fun buildModel(isSample: Boolean = false) = readInput(2023, 20, isSample).useLines { lines ->
    val outputFrequency = hashMapOf<String, Int>()
    val model = hashMapOf<String, PulseModule>()
    for (line in lines) {
        val (name, other) = line.split(" -> ")
        val outputs = other.split(", ")
        val module = when {
            name.startsWith("%") -> FlipFlopModule(name.drop(1), outputs)
            name.startsWith("&") -> ConjunctionModule(name.drop(1), outputs)
            else -> BroadcastModule(name, outputs)
        }
        model[module.name] = module
        for (output in outputs) outputFrequency[output] = outputFrequency.getOrDefault(output, 0) + 1
    }
    for ((key, value) in outputFrequency) {
        val module = model[key] ?: continue
        if (module is ConjunctionModule) module.inputCount = value
    }
    model
}


sealed interface PulseModule {
    val name: String
    val outputs: List<String>
}

data class BroadcastModule(override val name: String, override val outputs: List<String>) : PulseModule
data class FlipFlopModule(override val name: String, override val outputs: List<String>) : PulseModule {
    var isOn: Boolean = false
}
data class ConjunctionModule(override val name: String, override val outputs: List<String>) : PulseModule {
    var inputCount = 0
    val states = hashMapOf<String, Boolean>()
}
