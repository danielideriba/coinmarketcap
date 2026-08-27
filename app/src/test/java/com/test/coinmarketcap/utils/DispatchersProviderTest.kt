package com.test.coinmarketcap.utils

import kotlinx.coroutines.Dispatchers
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class DispatchersProviderTest {

    private lateinit var provider: DefaultDispatcherProvider

    @Before
    fun setUp() {
        provider = DefaultDispatcherProvider()
    }

    @Test
    fun `default returns Dispatchers Default`() {
        assertEquals(Dispatchers.Default, provider.default())
    }

    @Test
    fun `io returns Dispatchers IO`() {
        assertEquals(Dispatchers.IO, provider.io())
    }

    @Test
    fun `unconfined returns Dispatchers Unconfined`() {
        assertEquals(Dispatchers.Unconfined, provider.unconfined())
    }
}