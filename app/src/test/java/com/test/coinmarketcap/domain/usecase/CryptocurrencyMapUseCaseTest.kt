package com.test.coinmarketcap.domain.usecase

import app.cash.turbine.test
import com.test.coinmarketcap.data.remote.model.CoinWithExchangeInfo
import com.test.coinmarketcap.data.remote.model.ExchangeUrls
import com.test.coinmarketcap.domain.repository.CryptocurrencyRepository
import com.test.coinmarketcap.utils.ApiState
import com.test.coinmarketcap.utils.UiState
import android.util.Log
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class CryptocurrencyMapUseCaseTest {

    private lateinit var repository: CryptocurrencyRepository
    private lateinit var useCase: CryptocurrencyMapUseCase

    private val fakeCoin = CoinWithExchangeInfo(
        id = 1,
        name = "Binance",
        slug = "binance",
        makerFee = 0.1,
        takerFee = 0.2,
        urls = ExchangeUrls(website = listOf("https://binance.com")),
        isActive = 1,
        logo = "https://logo.png",
        spotVolumeUsd = 1_000_000.0,
        dateLaunched = "2017-07-14",
        description = "Top exchange"
    )

    @Before
    fun setUp() {
        mockkStatic(Log::class)
        every { Log.i(any(), any()) } returns 0
        repository = mockk()
        useCase = CryptocurrencyMapUseCase(repository)
    }

    @Test
    fun `invoke maps ApiState Success to UiState Success`() = runTest {
        every { repository.mapCoins(30) } returns flowOf(ApiState.Success(listOf(fakeCoin)))

        useCase.invoke(30).test {
            val result = awaitItem()
            assertTrue(result is UiState.Success)
            awaitComplete()
        }
    }

    @Test
    fun `invoke maps coin fields to MapCoinsEntity correctly`() = runTest {
        every { repository.mapCoins(30) } returns flowOf(ApiState.Success(listOf(fakeCoin)))

        useCase.invoke(30).test {
            val result = awaitItem() as UiState.Success
            val entity = result.data.first()

            assertEquals(1, entity.id)
            assertEquals("Binance", entity.name)
            assertEquals("binance", entity.slug)
            assertEquals("https://binance.com", entity.url)
            assertEquals(0.1, entity.makerFee)
            assertEquals(0.2, entity.takerFee)
            assertEquals(1, entity.isActive)
            assertEquals("https://logo.png", entity.logo)
            assertEquals(1_000_000.0, entity.spotVolumeUsd)
            assertEquals("2017-07-14", entity.dateLaunched)
            assertEquals("Top exchange", entity.description)

            awaitComplete()
        }
    }

    @Test
    fun `invoke maps ApiState Error to UiState Error`() = runTest {
        every { repository.mapCoins(30) } returns flowOf(ApiState.Error("Network error"))

        useCase.invoke(30).test {
            val result = awaitItem()
            assertTrue(result is UiState.Error)
            assertEquals("Network error", (result as UiState.Error).message)
            awaitComplete()
        }
    }

    @Test
    fun `invoke maps not found error to friendly message`() = runTest {
        every { repository.mapCoins(30) } returns flowOf(ApiState.Error("not found"))

        useCase.invoke(30).test {
            val result = awaitItem() as UiState.Error
            assertEquals(
                "Resources not found. Please check the spelling and try again.",
                result.message
            )
            awaitComplete()
        }
    }

    @Test
    fun `invoke calls repository with correct limit`() = runTest {
        every { repository.mapCoins(10) } returns flowOf(ApiState.Success(emptyList()))

        useCase.invoke(10).test {
            awaitItem()
            awaitComplete()
        }

        verify(exactly = 1) { repository.mapCoins(10) }
    }

    @Test
    fun `invoke maps list with multiple coins correctly`() = runTest {
        val secondCoin = fakeCoin.copy(
            id = 2,
            name = "Coinbase",
            slug = "coinbase",
            urls = ExchangeUrls(website = listOf("https://coinbase.com"))
        )
        every { repository.mapCoins(30) } returns flowOf(ApiState.Success(listOf(fakeCoin, secondCoin)))

        useCase.invoke(30).test {
            val result = awaitItem() as UiState.Success
            assertEquals(2, result.data.size)
            assertEquals("Binance", result.data[0].name)
            assertEquals("Coinbase", result.data[1].name)
            awaitComplete()
        }
    }

    @Test
    fun `invoke maps nullable fields correctly`() = runTest {
        val coinWithNulls = fakeCoin.copy(
            makerFee = null,
            takerFee = null,
            description = null,
            spotVolumeUsd = null,
            dateLaunched = null
        )
        every { repository.mapCoins(30) } returns flowOf(ApiState.Success(listOf(coinWithNulls)))

        useCase.invoke(30).test {
            val result = awaitItem() as UiState.Success
            val entity = result.data.first()

            assertNull(entity.makerFee)
            assertNull(entity.takerFee)
            assertNull(entity.description)
            assertNull(entity.spotVolumeUsd)
            assertNull(entity.dateLaunched)

            awaitComplete()
        }
    }

    @Test
    fun `invoke returns empty list when ApiState Success has no coins`() = runTest {
        every { repository.mapCoins(30) } returns flowOf(ApiState.Success(emptyList()))

        useCase.invoke(30).test {
            val result = awaitItem() as UiState.Success
            assertTrue(result.data.isEmpty())
            awaitComplete()
        }
    }
}