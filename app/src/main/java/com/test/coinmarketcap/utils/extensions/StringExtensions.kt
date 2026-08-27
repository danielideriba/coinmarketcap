package com.test.coinmarketcap.utils.extensions

fun String.formatDate(): String {
    return try {
        val input = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
        val output = java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")
        val date = java.time.LocalDate.parse(this, input)
        date.format(output)
    } catch (_: Exception) {
        this
    }
}