package com.guaracode.evofit.core.common

import kotlin.random.Random

fun randomUuid(): String {
    val random = Random.Default
    val hexChars = "0123456789abcdef"
    fun hex(count: Int): String {
        val sb = StringBuilder(count)
        repeat(count) {
            sb.append(hexChars[random.nextInt(16)])
        }
        return sb.toString()
    }
    val variantChar = hexChars[8 + random.nextInt(4)]
    return "${hex(8)}-${hex(4)}-4${hex(3)}-$variantChar${hex(3)}-${hex(12)}"
}
