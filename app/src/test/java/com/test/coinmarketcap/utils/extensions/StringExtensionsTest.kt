package com.test.coinmarketcap.utils.extensions

import org.junit.Assert.assertEquals
import org.junit.Test

class StringExtensionsTest {

    // region formatDate

    @Test
    fun `formatDate converts valid ISO datetime to dd-MM-yyyy`() {
        assertEquals("14/07/2017", "2017-07-14T00:00:00.000Z".formatDate())
    }

    @Test
    fun `formatDate converts datetime with non-zero time component`() {
        assertEquals("01/01/2020", "2020-01-01T23:59:59.999Z".formatDate())
    }

    @Test
    fun `formatDate returns original string when format is invalid`() {
        val invalid = "not-a-date"
        assertEquals(invalid, invalid.formatDate())
    }

    @Test
    fun `formatDate returns original string when date is empty`() {
        assertEquals("", "".formatDate())
    }

    @Test
    fun `formatDate returns original string for partial date`() {
        val partial = "2021-06-15"
        assertEquals(partial, partial.formatDate())
    }

    @Test
    fun `formatDate returns original string for wrong separator`() {
        val wrongFormat = "15-06-2021T00:00:00.000Z"
        assertEquals(wrongFormat, wrongFormat.formatDate())
    }

    // endregion
}