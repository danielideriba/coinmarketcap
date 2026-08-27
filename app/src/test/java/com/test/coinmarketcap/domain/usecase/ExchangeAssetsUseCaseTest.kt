package com.test.coinmarketcap.domain.usecase

import app.cash.turbine.test
import com.test.coinmarketcap.domain.models.ExchangeAssetEntity
import com.test.coinmarketcap.domain.repository.CryptocurrencyRepository
import com.test.coinmarketcap.utils.ApiState
import com.test.coinmarketcap.utils.UiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ExchangeAssetsUseCaseTest {

    private lateinit var repository: CryptocurrencyRepository
    private lateinit var useCase: ExchangeAssetsUseCase

    private val fakeAssets = listOf(
        ExchangeAssetEntity(currencyName = "Bitcoin", priceUsd = 60_000.0),
        ExchangeAssetEntity(currencyName = "Ethereum", priceUsd = 3_000.0)
    )

    @Before
    fun setUp() {
        repository = mockk()
        useCase = ExchangeAssetsUseCase(repository)
    }

    @Test
    fun `invoke maps ApiState Success to UiState Success`() = runTest {
        every { repository.getExchangeAssets(1) } returns flowOf(ApiState.Success(fakeAssets))

        useCase.invoke(1).test {
            val result = awaitItem()
            assertTrue(result is UiState.Success)
            assertEquals(fakeAssets, (result as UiState.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `invoke maps ApiState Error to UiState Error`() = runTest {
        every { repository.getExchangeAssets(1) } returns flowOf(ApiState.Error("Server error"))

        useCase.invoke(1).test {
            val result = awaitItem()
            assertTrue(result is UiState.Error)
            assertEquals("Server error", (result as UiState.Error).message)
            awaitComplete()
        }
    }

    @Test
    fun `invoke calls repository with correct id`() = runTest {
        every { repository.getExchangeAssets(42) } returns flowOf(ApiState.Success(emptyList()))

        useCase.invoke(42).test {
            awaitItem()
            awaitComplete()
        }

        verify(exactly = 1) { repository.getExchangeAssets(42) }
    }

    @Test
    fun `invoke returns empty list when ApiState Success has no assets`() = runTest {
        every { repository.getExchangeAssets(1) } returns flowOf(ApiState.Success(emptyList()))

        useCase.invoke(1).test {
            val result = awaitItem() as UiState.Success
            assertTrue(result.data.isEmpty())
            awaitComplete()
        }
    }

    @Test
    fun `invoke preserves asset data correctly`() = runTest {
        every { repository.getExchangeAssets(1) } returns flowOf(ApiState.Success(fakeAssets))

        useCase.invoke(1).test {
            val result = awaitItem() as UiState.Success
            assertEquals(2, result.data.size)
            assertEquals("Bitcoin", result.data[0].currencyName)
            assertEquals(60_000.0, result.data[0].priceUsd, 0.0)
            assertEquals("Ethereum", result.data[1].currencyName)
            assertEquals(3_000.0, result.data[1].priceUsd, 0.0)
            awaitComplete()
        }
    }
}