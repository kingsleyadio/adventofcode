package y2023.day05

import java.io.File

fun main() {
    val (seeds, cultivator) = File("input.txt").useLines { sequence ->
        val lines = sequence.iterator()
        val seeds = lines.next().substringAfter(": ").split(" ").map { it.toLong() }
        lines.next()
        lines.next()
        val seedToSoil = lines.generateMap()
        lines.next()
        val soilToFertilizer = lines.generateMap()
        lines.next()
        val fertilizerToWater = lines.generateMap()
        lines.next()
        val waterToLight = lines.generateMap()
        lines.next()
        val lightToTemperature = lines.generateMap()
        lines.next()
        val temperatureToHumidity = lines.generateMap()
        lines.next()
        val humidityToLocation = lines.generateMap()

        seeds to Cultivator(
            seedToSoil,
            soilToFertilizer,
            fertilizerToWater,
            waterToLight,
            lightToTemperature,
            temperatureToHumidity,
            humidityToLocation
        )
    }
    part1(seeds, cultivator)
    part2(seeds, cultivator)
}

fun part1(seeds: List<Long>, cultivator: Cultivator) {
    val lowestLocation = seeds.minOf(cultivator::calculateSeedLocation)
    println(lowestLocation)
}

fun part2(spec: List<Long>, cultivator: Cultivator) {
    // Using asSequence here to avoid OOM
    val seeds = spec.asSequence().chunked(2).flatMap { (start, size) -> start..<start+size }
    val lowestLocation = seeds.minOf(cultivator::calculateSeedLocation)
    println(lowestLocation)
}

class Cultivator(
    private val seedToSoil: Map<LongRange, Long>,
    private val soilToFertilizer: Map<LongRange, Long>,
    private val fertilizerToWater: Map<LongRange, Long>,
    private val waterToLight: Map<LongRange, Long>,
    private val lightToTemperature: Map<LongRange, Long>,
    private val temperatureToHumidity: Map<LongRange, Long>,
    private val humidityToLocation: Map<LongRange, Long>,
) {
    fun calculateSeedLocation(seed: Long): Long {
        val soil = seedToSoil.getValue(seed)
        val fertilizer = soilToFertilizer.getValue(soil)
        val water = fertilizerToWater.getValue(fertilizer)
        val light = waterToLight.getValue(water)
        val temperature = lightToTemperature.getValue(light)
        val humidity = temperatureToHumidity.getValue(temperature)
        return humidityToLocation.getValue(humidity)
    }

    private fun Map<LongRange, Long>.getValue(key: Long): Long {
        for ((range, v) in this) {
            if (key in range) {
                val diff = key - range.first
                return v + diff
            }
        }
        return key
    }
}

fun Iterator<String>.generateMap(): Map<LongRange, Long> = buildMap {
    while (hasNext()) {
        val line = next()
        if (line.isEmpty()) break
        val (dest, src, range) = line.split(" ").map { it.toLong() }
        put(src..<src + range, dest)
    }
}

main()
