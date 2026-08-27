package com.test.coinmarketcap.ui.viewmodel

import app.cash.turbine.test
import com.test.coinmarketcap.domain.models.MapCoinsEntity
import com.test.coinmarketcap.domain.usecase.CryptocurrencyMapUseCase
import com.test.coinmarketcap.utils.UiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class HomeScreenViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: CryptocurrencyMapUseCase
    private lateinit var viewModel: HomeScreenViewModel

    private val fakeCoin = MapCoinsEntity(
        id = 1,
        name = "Binance",
        slug = "binance",
        url = "https://binance.com",
        description = "Top exchange",
        isActive = 1,
        logo = "https://logo.png",
        makerFee = 0.1,
        takerFee = 0.1,
        spotVolumeUsd = 1_000_000.0,
        dateLaunched = "2017-07-14"
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        useCase = mockk()
        viewModel = HomeScreenViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Ready`() {
        assertTrue(viewModel.mapState.value is UiState.Ready)
    }

    @Test
    fun `mappedCoins emits Loading then Success`() = runTest {
        every { useCase.invoke(30) } returns flowOf(UiState.Success(listOf(fakeCoin)))

        viewModel.mapState.test {
            assertEquals(UiState.Ready, awaitItem())

            viewModel.mappedCoins()
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(UiState.Loading, awaitItem())

            val success = awaitItem()
            assertTrue(success is UiState.Success)
            assertEquals(listOf(fakeCoin), (success as UiState.Success).data)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `mappedCoins emits Loading then Error on exception`() = runTest {
        every { useCase.invoke(30) } returns flowOf(UiState.Error("Network error"))

        viewModel.mapState.test {
            assertEquals(UiState.Ready, awaitItem())

            viewModel.mappedCoins()
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(UiState.Loading, awaitItem())

            val error = awaitItem()
            assertTrue(error is UiState.Error)
            assertEquals("Network error", (error as UiState.Error).message)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `setLoading emits Loading state`() = runTest {
        viewModel.mapState.test {
            assertEquals(UiState.Ready, awaitItem())

            viewModel.setLoading()

            assertEquals(UiState.Loading, awaitItem())

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `mappedCoins calls useCase with limit 30`() = runTest {
        every { useCase.invoke(30) } returns flowOf(UiState.Success(emptyList()))

        viewModel.mappedCoins()
        testDispatcher.scheduler.advanceUntilIdle()

        verify(exactly = 1) { useCase.invoke(30) }
    }

    @Test
    fun `Success state contains correct coin data`() = runTest {
        val coins = listOf(fakeCoin, fakeCoin.copy(id = 2, name = "Coinbase"))
        every { useCase.invoke(30) } returns flowOf(UiState.Success(coins))

        viewModel.mapState.test {
            awaitItem() // Ready

            viewModel.mappedCoins()
            testDispatcher.scheduler.advanceUntilIdle()

            awaitItem() // Loading

            val success = awaitItem() as UiState.Success
            assertEquals(2, success.data.size)
            assertEquals("Binance", success.data[0].name)
            assertEquals("Coinbase", success.data[1].name)

            cancelAndIgnoreRemainingEvents()
        }
    }
}