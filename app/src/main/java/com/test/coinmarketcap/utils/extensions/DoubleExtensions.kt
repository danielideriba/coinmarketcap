package com.test.coinmarketcap.utils.extensions

import java.text.NumberFormat
import java.util.Locale

fun Double.toCurrencyUsd(): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale.US)
    return formatter.format(this)
}

fun Double.toFeePercentage(): String = "%.3f%%".format(this)