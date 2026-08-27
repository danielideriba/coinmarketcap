package com.test.coinmarketcap.ui.viewmodel

import app.cash.turbine.test
import com.test.coinmarketcap.domain.models.ExchangeAssetEntity
import com.test.coinmarketcap.domain.usecase.ExchangeAssetsUseCase
import com.test.coinmarketcap.utils.UiState
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
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
class CoinDetailViewModelTest {

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var useCase: ExchangeAssetsUseCase
    private lateinit var viewModel: CoinDetailViewModel

    private val fakeAssets = listOf(
        ExchangeAssetEntity(currencyName = "Bitcoin", priceUsd = 60_000.0),
        ExchangeAssetEntity(currencyName = "Ethereum", priceUsd = 3_000.0)
    )

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        useCase = mockk()
        viewModel = CoinDetailViewModel(useCase)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state is Ready`() {
        assertTrue(viewModel.assetsState.value is UiState.Ready)
    }

    @Test
    fun `loadAssets emits Loading then Success`() = runTest {
        every { useCase.invoke(1) } returns flowOf(UiState.Success(fakeAssets))

        viewModel.assetsState.test {
            assertEquals(UiState.Ready, awaitItem())

            viewModel.loadAssets(1)
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(UiState.Loading, awaitItem())

            val success = awaitItem()
            assertTrue(success is UiState.Success)
            assertEquals(fakeAssets, (success as UiState.Success).data)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadAssets emits Loading then Error when flow emits Error`() = runTest {
        every { useCase.invoke(1) } returns flowOf(UiState.Error("Not found"))

        viewModel.assetsState.test {
            assertEquals(UiState.Ready, awaitItem())

            viewModel.loadAssets(1)
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(UiState.Loading, awaitItem())

            val error = awaitItem()
            assertTrue(error is UiState.Error)
            assertEquals("Not found", (error as UiState.Error).message)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadAssets emits Error when flow throws exception`() = runTest {
        every { useCase.invoke(1) } returns flow { throw RuntimeException("Timeout") }

        viewModel.assetsState.test {
            assertEquals(UiState.Ready, awaitItem())

            viewModel.loadAssets(1)
            testDispatcher.scheduler.advanceUntilIdle()

            assertEquals(UiState.Loading, awaitItem())

            val error = awaitItem()
            assertTrue(error is UiState.Error)
            assertEquals("Timeout", (error as UiState.Error).message)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadAssets calls useCase with correct id`() = runTest {
        every { useCase.invoke(42) } returns flowOf(UiState.Success(emptyList()))

        viewModel.loadAssets(42)
        testDispatcher.scheduler.advanceUntilIdle()

        verify(exactly = 1) { useCase.invoke(42) }
    }

    @Test
    fun `Success state contains correct assets data`() = runTest {
        every { useCase.invoke(1) } returns flowOf(UiState.Success(fakeAssets))

        viewModel.assetsState.test {
            awaitItem() // Ready

            viewModel.loadAssets(1)
            testDispatcher.scheduler.advanceUntilIdle()

            awaitItem() // Loading

            val success = awaitItem() as UiState.Success
            assertEquals(2, success.data.size)
            assertEquals("Bitcoin", success.data[0].currencyName)
            assertEquals(60_000.0, success.data[0].priceUsd, 0.0)
            assertEquals("Ethereum", success.data[1].currencyName)
            assertEquals(3_000.0, success.data[1].priceUsd, 0.0)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `loadAssets with empty list emits Success with empty data`() = runTest {
        every { useCase.invoke(99) } returns flowOf(UiState.Success(emptyList()))

        viewModel.assetsState.test {
            awaitItem() // Ready

            viewModel.loadAssets(99)
            testDispatcher.scheduler.advanceUntilIdle()

            awaitItem() // Loading

            val success = awaitItem() as UiState.Success
            assertTrue(success.data.isEmpty())

            cancelAndIgnoreRemainingEvents()
        }
    }
}