package com.test.coinmarketcap.data.repository

import android.util.Log
import app.cash.turbine.test
import com.test.coinmarketcap.data.datasource.CryptocurrencyDataSource
import com.test.coinmarketcap.data.remote.model.CoinWithExchangeInfo
import com.test.coinmarketcap.data.remote.model.CryptocurrencyMapItem
import com.test.coinmarketcap.data.remote.model.CryptocurrencyMapResponse
import com.test.coinmarketcap.data.remote.model.Currency
import com.test.coinmarketcap.data.remote.model.ExchangeAsset
import com.test.coinmarketcap.data.remote.model.ExchangeAssetsResponse
import com.test.coinmarketcap.data.remote.model.ExchangeInfo
import com.test.coinmarketcap.data.remote.model.ExchangeInfoResponse
import com.test.coinmarketcap.data.remote.model.ExchangeUrls
import com.test.coinmarketcap.data.remote.model.Platform
import com.test.coinmarketcap.utils.ApiState
import com.test.coinmarketcap.utils.DispatchersProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import retrofit2.Response

@OptIn(ExperimentalCoroutinesApi::class)
class CryptocurrencyRepositoryImplTest {

    private lateinit var dataSource: CryptocurrencyDataSource
    private lateinit var dispatchersProvider: DispatchersProvider
    private lateinit var repository: CryptocurrencyRepositoryImpl

    private val testDispatcher = UnconfinedTestDispatcher()

    private val fakeMapItem = CryptocurrencyMapItem(
        id = 1, name = "Binance", slug = "binance", isActive = 1
    )

    private val fakeExchangeInfo = ExchangeInfo(
        id = 1, name = "Binance", slug = "binance",
        logo = "https://logo.png", makerFee = 0.1, takerFee = 0.2,
        urls = ExchangeUrls(website = listOf("https://binance.com")),
        description = "Top exchange", spotVolumeUsd = 1_000_000.0,
        dateLaunched = "2017-07-14"
    )

    private val fakeAsset = ExchangeAsset(
        walletAddress = "0x123",
        balance = 1.0,
        platform = Platform(cryptoId = 1, symbol = "ETH", name = "Ethereum"),
        currency = Currency(cryptoId = 2, priceUsd = 60_000.0, symbol = "BTC", name = "Bitcoin")
    )

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0

        dataSource = mockk()
        dispatchersProvider = mockk {
            every { io() } returns testDispatcher
        }
        repository = CryptocurrencyRepositoryImpl(dataSource, dispatchersProvider)
    }

    // region mapCoins

    @Test
    fun `mapCoins emits Success with combined data`() = runTest {
        coEvery { dataSource.mapCoins(30) } returns Response.success(
            CryptocurrencyMapResponse(data = listOf(fakeMapItem))
        )
        coEvery { dataSource.exchangeInfo("1") } returns Response.success(
            ExchangeInfoResponse(data = mapOf("1" to fakeExchangeInfo))
        )

        repository.mapCoins(30).test {
            val result = awaitItem() as ApiState.Success
            assertEquals(1, result.data.size)
            val coin = result.data.first()
            assertEquals(1, coin.id)
            assertEquals("Binance", coin.name)
            assertEquals(0.1, coin.makerFee)
            assertEquals(0.2, coin.takerFee)
            assertEquals("https://logo.png", coin.logo)
            awaitComplete()
        }
    }

    @Test
    fun `mapCoins emits Error when mapCoins API fails`() = runTest {
        coEvery { dataSource.mapCoins(30) } returns Response.error(
            500, "error".toResponseBody("text/plain".toMediaType())
        )

        repository.mapCoins(30).test {
            val result = awaitItem()
            assertTrue(result is ApiState.Error)
            awaitComplete()
        }
    }

    @Test
    fun `mapCoins uses empty map when exchangeInfo fails`() = runTest {
        coEvery { dataSource.mapCoins(30) } returns Response.success(
            CryptocurrencyMapResponse(data = listOf(fakeMapItem))
        )
        coEvery { dataSource.exchangeInfo("1") } returns Response.error(
            500, "error".toResponseBody("text/plain".toMediaType())
        )

        repository.mapCoins(30).test {
            val result = awaitItem() as ApiState.Success
            val coin = result.data.first()
            assertEquals("", coin.logo)
            awaitComplete()
        }
    }

    @Test
    fun `mapCoins combines multiple coins correctly`() = runTest {
        val secondItem = CryptocurrencyMapItem(id = 2, name = "Coinbase", slug = "coinbase", isActive = 1)
        val secondExchange = fakeExchangeInfo.copy(id = 2, name = "Coinbase")

        coEvery { dataSource.mapCoins(30) } returns Response.success(
            CryptocurrencyMapResponse(data = listOf(fakeMapItem, secondItem))
        )
        coEvery { dataSource.exchangeInfo("1,2") } returns Response.success(
            ExchangeInfoResponse(data = mapOf("1" to fakeExchangeInfo, "2" to secondExchange))
        )

        repository.mapCoins(30).test {
            val result = awaitItem() as ApiState.Success
            assertEquals(2, result.data.size)
            assertEquals("Binance", result.data[0].name)
            assertEquals("Coinbase", result.data[1].name)
            awaitComplete()
        }
    }

    // endregion

    // region getExchangeAssets

    @Test
    fun `getExchangeAssets emits Success with mapped entities`() = runTest {
        coEvery { dataSource.getAssets("1") } returns Response.success(
            ExchangeAssetsResponse(data = listOf(fakeAsset))
        )

        repository.getExchangeAssets(1).test {
            val result = awaitItem() as ApiState.Success
            assertEquals(1, result.data.size)
            assertEquals("Bitcoin", result.data.first().currencyName)
            assertEquals(60_000.0, result.data.first().priceUsd, 0.0)
            awaitComplete()
        }
    }

    @Test
    fun `getExchangeAssets emits Error when API fails`() = runTest {
        coEvery { dataSource.getAssets("1") } returns Response.error(
            404, "error".toResponseBody("text/plain".toMediaType())
        )

        repository.getExchangeAssets(1).test {
            val result = awaitItem()
            assertTrue(result is ApiState.Error)
            awaitComplete()
        }
    }

    @Test
    fun `getExchangeAssets emits Success with empty list`() = runTest {
        coEvery { dataSource.getAssets("99") } returns Response.success(
            ExchangeAssetsResponse(data = emptyList())
        )

        repository.getExchangeAssets(99).test {
            val result = awaitItem() as ApiState.Success
            assertTrue(result.data.isEmpty())
            awaitComplete()
        }
    }

    // endregion
}
