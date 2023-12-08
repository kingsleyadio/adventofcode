package com.kingsleyadio.adventofcode.util

tailrec fun gcd(a: Long, b: Long): Long = if (b == 0L) a else gcd(b, a % b)

fun lcm(a: Long, b: Long): Long = a / gcd(a, b) * b
