package com.kingsleyadio.adventofcode.y2024

private fun part2(program: List<Int>) {
    val padding = 10
    val a = IntArray(program.size * 3 + padding)
    val x = BooleanArray(program.size * 3 + padding)
    fun possibilities(index: Int): List<Int> {
        return (0..7).map {
            val first = if (x[index]) a[index] * 4 else it and 4
            val second = if (x[index + 1]) a[index + 1] * 2 else it and 2
            val third = if (x[index + 2]) a[index + 2] else it and 1
            first + second + third
        }.distinct()
    }

    fun simulate(bi: Int): Boolean {
        // This works with specific puzzle inputs. Logic for display must be adjusted accordingly
        if (bi < padding) return true
        val programIndex = program.lastIndex - (bi - padding) / 3
        for (aa in possibilities(bi)) {
            // Sample
            // if (aa == program[programIndex] && simulate(bi - 3)) {
            //     aa.toBin8().copyInto(a, bi-3)
            //     return true
            // }
            // My puzzle
            val b = aa xor 6
            val ci = bi - b
            for (c in possibilities(ci)) {
                val bb = b xor c xor 4
                c.toBin8().copyInto(a, ci)
                val temp = x.sliceArray(ci..ci + 2)
                x.fill(true, ci, ci + 3)
                if (bb == program[programIndex] && simulate(bi - 3)) {
                    aa.toBin8().copyInto(a, bi)
                    x.fill(true, bi, bi + 3)
                    return true
                }
                temp.copyInto(x, ci)
            }
        }
        return false
    }

    require(simulate(a.lastIndex - 2))
    val result = a.sliceArray(padding..a.lastIndex).toLong8()
    println(result)
//    println(run(longArrayOf(result, 0, 0), program).joinToString(","))
}

private fun IntArray.toLong8() = fold(0L) { acc, n -> acc * 2 + n }
private fun Int.toBin8() = toString(2).padStart(3, '0').map(Char::digitToInt).toIntArray()
