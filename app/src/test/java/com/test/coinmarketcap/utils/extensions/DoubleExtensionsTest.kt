package com.test.coinmarketcap.utils.extensions

import org.junit.Assert.assertEquals
import org.junit.Test

class DoubleExtensionsTest {

    // region toCurrencyUsd

    @Test
    fun `toCurrencyUsd formats integer value correctly`() {
        assertEquals("$1,000.00", 1000.0.toCurrencyUsd())
    }

    @Test
    fun `toCurrencyUsd formats value with cents correctly`() {
        assertEquals("$60,000.50", 60000.50.toCurrencyUsd())
    }

    @Test
    fun `toCurrencyUsd formats zero correctly`() {
        assertEquals("$0.00", 0.0.toCurrencyUsd())
    }

    @Test
    fun `toCurrencyUsd formats small decimal value correctly`() {
        assertEquals("$0.99", 0.99.toCurrencyUsd())
    }

    @Test
    fun `toCurrencyUsd formats large value with thousand separators`() {
        assertEquals("$1,000,000.00", 1_000_000.0.toCurrencyUsd())
    }

    @Test
    fun `toCurrencyUsd formats negative value correctly`() {
        assertEquals("-$500.00", (-500.0).toCurrencyUsd())
    }

    // endregion

    // region toFeePercentage

    @Test
    fun `toFeePercentage formats value with 3 decimal places`() {
        assertEquals("0.100%", 0.1.toFeePercentage())
    }

    @Test
    fun `toFeePercentage formats zero correctly`() {
        assertEquals("0.000%", 0.0.toFeePercentage())
    }

    @Test
    fun `toFeePercentage formats value already with 3 decimals`() {
        assertEquals("0.250%", 0.25.toFeePercentage())
    }

    @Test
    fun `toFeePercentage rounds to 3 decimal places`() {
        assertEquals("0.100%", 0.1004.toFeePercentage())
    }

    @Test
    fun `toFeePercentage formats integer fee correctly`() {
        assertEquals("1.000%", 1.0.toFeePercentage())
    }

    // endregion
}