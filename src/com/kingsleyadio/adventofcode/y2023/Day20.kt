package com.kingsleyadio.adventofcode.y2023

import com.kingsleyadio.adventofcode.util.lcm
import com.kingsleyadio.adventofcode.util.readInput

fun main() {
    val (model, inputs) = buildModel()
    part1(model, inputs)
    part2(model, inputs)
}

private fun part1(model: Model, inputs: Inputs) {
    val signals = IntArray(2)
    for (i in 1..1000) simulation(model, inputs) { _, _, pulse -> signals[if (pulse) 1 else 0]++ }
    val result = signals[0] * signals[1]
    println(result)
}

private fun part2(model: Model, inputs: Inputs) {
    model.values.forEach(PulseModule::reset)
    var presses = 0
    val mgName = inputs.getValue("rx").single()
    val mg = model.getValue(mgName) as ConjunctionModule
    val xToMg = hashMapOf<String, Int>()
    while (true) {
        presses++
        simulation(model, inputs) { from, to, pulse -> if (to == mg.name && pulse) xToMg.putIfAbsent(from, presses) }
        if (xToMg.size == inputs.getValue(mg.name).size) break
    }
    val result = xToMg.values.fold(1L) { acc, n -> lcm(acc, n.toLong()) }
    println(result)
}

private inline fun simulation(model: Model, inputs: Inputs, onSignal: (String, String, Boolean) -> Unit) {
    val queue = ArrayDeque<Triple<String, String, Boolean>>()
    queue.addLast(Triple("button", "broadcaster", false))
    while (queue.isNotEmpty()) {
        val (from, to, pulse) = queue.removeFirst()
        onSignal(from, to, pulse)
        val module = model[to] ?: continue
        val moduleInputs = inputs.getOrDefault(module.name, emptySet())
        val newPulse = module.transformPulse(from, pulse, moduleInputs.size) ?: continue
        for (output in module.outputs) queue.addLast(Triple(module.name, output, newPulse))
    }
}

private fun buildModel(isSample: Boolean = false) = readInput(2023, 20, isSample).useLines { lines ->
    val inputs = hashMapOf<String, MutableSet<String>>()
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
        for (output in outputs) inputs.getOrPut(output) { hashSetOf() }.add(module.name)
    }
    model to inputs
}

private typealias Model = Map<String, PulseModule>
private typealias Inputs = Map<String, Set<String>>

private sealed interface PulseModule {
    val name: String
    val outputs: List<String>
    fun transformPulse(from: String, pulse: Boolean, inputSize: Int): Boolean?
    fun reset()
}

private data class BroadcastModule(override val name: String, override val outputs: List<String>) : PulseModule {
    override fun transformPulse(from: String, pulse: Boolean, inputSize: Int) = pulse
    override fun reset() {}
}

private data class FlipFlopModule(override val name: String, override val outputs: List<String>) : PulseModule {
    private var isOn: Boolean = false
    override fun transformPulse(from: String, pulse: Boolean, inputSize: Int): Boolean? {
        if (pulse) return null
        return isOn.not().also { isOn = it }
    }
    override fun reset() = run { isOn = false }
}

private data class ConjunctionModule(override val name: String, override val outputs: List<String>) : PulseModule {
    private val states = hashMapOf<String, Boolean>()
    override fun transformPulse(from: String, pulse: Boolean, inputSize: Int): Boolean {
        states[from] = pulse
        return states.values.count { it } != inputSize
    }
    override fun reset() = states.clear()
}
