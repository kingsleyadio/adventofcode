package y2021.day16

import java.io.File

fun main() {
    File("input.txt").forEachLine { line ->
        val input = line.map { it.digitToInt(16).toString(2).padStart(4, '0') }.joinToString("")
        val packet = parse(input)
        println(packet.versionSum())
        println(packet.evaluate())
    }
}

sealed interface Packet {
    val version: Int
    fun versionSum(): Int
    fun evaluate(): Long

    class Literal(
        override val version: Int,
        private val value: Long
    ) : Packet {
        override fun versionSum() = version
        override fun evaluate() = value
    }

    class Expression(
        override val version: Int,
        private val operator: (List<Packet>) -> Long,
        private val subPackets: List<Packet>
    ) : Packet {
        override fun versionSum() = version + subPackets.sumOf(Packet::versionSum)
        override fun evaluate() = operator.invoke(subPackets)
    }
}

fun parse(input: String): Packet {
    fun parse(reader: StringReader): Packet {
        val version = reader.readBinary(3)
        when (val typeId = reader.readBinary(3)) {
            4 -> {
                val number = buildString {
                    while (true) {
                        val sub = reader.read(5)
                        append(sub.substring(1))
                        if (sub[0] == '0') break
                    }
                }.toLong(2)
                return Packet.Literal(version, number)
            }
            else -> {
                val subPackets = arrayListOf<Packet>()
                if (reader.readBinary(1) == 0) {
                    val subPacketLength = reader.readBinary(15)
                    val start = reader.index
                    while (reader.index - start < subPacketLength) subPackets.add(parse(reader))
                } else {
                    val subPacketCount = reader.readBinary(11)
                    repeat(subPacketCount) { subPackets.add(parse(reader)) }
                }
                val operator: (List<Packet>) -> Long = { packets ->
                    when (typeId) {
                        0 -> packets.sumOf { it.evaluate() }
                        1 -> packets.fold(1L) { acc, packet -> acc * packet.evaluate() }
                        2 -> packets.minOf { it.evaluate() }
                        3 -> packets.maxOf { it.evaluate() }
                        5 -> if (packets[0].evaluate() > packets[1].evaluate()) 1 else 0
                        6 -> if (packets[0].evaluate() < packets[1].evaluate()) 1 else 0
                        7 -> if (packets[0].evaluate() == packets[1].evaluate()) 1 else 0
                        else -> error("Invalid operator")
                    }
                }
                return Packet.Expression(version, operator, subPackets)
            }
        }
    }
    return parse(StringReader(input))
}

class StringReader(private val string: String) {
    var index = 0
    fun read(size: Int): String {
        val result = string.substring(index, index + size)
        index += size
        return result
    }
}

fun StringReader.readBinary(size: Int): Int = read(size).toInt(2)

main()
