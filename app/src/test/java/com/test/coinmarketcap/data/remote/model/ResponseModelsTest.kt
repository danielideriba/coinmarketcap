package com.test.coinmarketcap.data.remote.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class ResponseModelsTest {

    // region CryptocurrencyMapResponse

    @Test
    fun `CryptocurrencyMapResponse holds data list`() {
        val item = CryptocurrencyMapItem(id = 1, name = "Binance", slug = "binance", isActive = 1)
        val response = CryptocurrencyMapResponse(data = listOf(item))
        assertEquals(1, response.data.size)
        assertEquals(item, response.data.first())
    }

    @Test
    fun `CryptocurrencyMapItem defaults optional fields to null`() {
        val item = CryptocurrencyMapItem(id = 1, name = "Binance", slug = "binance", isActive = 1)
        assertNull(item.firstHistoricalData)
        assertNull(item.lastHistoricalData)
        assertNull(item.isListed)
        assertNull(item.isRedistributable)
    }

    @Test
    fun `CryptocurrencyMapItem copy creates independent instance`() {
        val item = CryptocurrencyMapItem(id = 1, name = "Binance", slug = "binance", isActive = 1)
        val copy = item.copy(name = "Coinbase")
        assertEquals("Coinbase", copy.name)
        assertEquals("binance", copy.slug)
    }

    // endregion

    // region ExchangeInfoResponse

    @Test
    fun `ExchangeInfoResponse holds data map`() {
        val info = ExchangeInfo(
            id = 1, name = "Binance", slug = "binance", logo = "logo.png",
            makerFee = 0.1, takerFee = 0.2, description = "Top exchange"
        )
        val response = ExchangeInfoResponse(data = mapOf("1" to info))
        assertEquals(1, response.data.size)
        assertEquals(info, response.data["1"])
    }

    @Test
    fun `ExchangeInfo defaults optional fields to null or empty`() {
        val info = ExchangeInfo(
            id = 1, name = "Binance", slug = "binance", logo = "logo.png",
            makerFee = 0.1, takerFee = 0.2, description = "Top exchange"
        )
        assertNull(info.urls)
        assertNull(info.dateLaunched)
        assertNull(info.spotVolumeUsd)
        assertNull(info.spotVolumeLastUpdated)
        assertTrue(info.countries.isEmpty())
    }

    @Test
    fun `ExchangeUrls defaults to empty website list`() {
        val urls = ExchangeUrls()
        assertTrue(urls.website.isEmpty())
    }

    @Test
    fun `ExchangeUrls holds website list`() {
        val urls = ExchangeUrls(website = listOf("https://binance.com"))
        assertEquals(1, urls.website.size)
        assertEquals("https://binance.com", urls.website.first())
    }

    // endregion

    // region ExchangeAssetsResponse

    @Test
    fun `ExchangeAssetsResponse holds asset list`() {
        val currency = Currency(cryptoId = 1, priceUsd = 60_000.0, symbol = "BTC", name = "Bitcoin")
        val platform = Platform(cryptoId = 2, symbol = "ETH", name = "Ethereum")
        val asset = ExchangeAsset(walletAddress = "0x1", balance = 1.0, platform = platform, currency = currency)
        val response = ExchangeAssetsResponse(data = listOf(asset))
        assertEquals(1, response.data.size)
        assertEquals(asset, response.data.first())
    }

    @Test
    fun `Currency holds all fields correctly`() {
        val currency = Currency(cryptoId = 1, priceUsd = 60_000.0, symbol = "BTC", name = "Bitcoin")
        assertEquals(1, currency.cryptoId)
        assertEquals(60_000.0, currency.priceUsd, 0.0)
        assertEquals("BTC", currency.symbol)
        assertEquals("Bitcoin", currency.name)
    }

    @Test
    fun `Platform holds all fields correctly`() {
        val platform = Platform(cryptoId = 2, symbol = "ETH", name = "Ethereum")
        assertEquals(2, platform.cryptoId)
        assertEquals("ETH", platform.symbol)
        assertEquals("Ethereum", platform.name)
    }

    @Test
    fun `ExchangeAsset holds all fields correctly`() {
        val currency = Currency(cryptoId = 1, priceUsd = 1.0, symbol = "BTC", name = "Bitcoin")
        val platform = Platform(cryptoId = 2, symbol = "ETH", name = "Ethereum")
        val asset = ExchangeAsset(walletAddress = "0xABC", balance = 2.5, platform = platform, currency = currency)
        assertEquals("0xABC", asset.walletAddress)
        assertEquals(2.5, asset.balance, 0.0)
        assertEquals(platform, asset.platform)
        assertEquals(currency, asset.currency)
    }

    // endregion
}
