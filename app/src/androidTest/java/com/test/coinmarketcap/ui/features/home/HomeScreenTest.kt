package com.test.coinmarketcap.ui.features.home

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.test.coinmarketcap.data.remote.model.CoinWithExchangeInfo
import com.test.coinmarketcap.data.remote.model.ExchangeUrls
import com.test.coinmarketcap.fake.FakeCryptocurrencyRepository
import com.test.coinmarketcap.ui.MainActivity
import com.test.coinmarketcap.utils.ApiState
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import javax.inject.Inject

@HiltAndroidTest
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Inject
    lateinit var fakeRepository: FakeCryptocurrencyRepository

    private val fakeCoin = CoinWithExchangeInfo(
        id = 270,
        name = "Binance",
        slug = "binance",
        isActive = 1,
        logo = "",
        spotVolumeUsd = 1_000_000.0,
        dateLaunched = null,
        urls = ExchangeUrls(website = listOf("https://binance.com")),
        makerFee = 0.1,
        takerFee = 0.2,
        description = "Top exchange"
    )

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun homeScreen_showsCoinName_onSuccess() {
        fakeRepository.emitCoins(ApiState.Success(listOf(fakeCoin)))

        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithText("Binance").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("Binance").assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsHeader_onSuccess() {
        fakeRepository.emitCoins(ApiState.Success(listOf(fakeCoin)))

        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithText("Coin Market Cap - Coin list").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("Coin Market Cap - Coin list").assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsMultipleCoins_onSuccess() {
        val secondCoin = fakeCoin.copy(id = 2, name = "Coinbase")
        fakeRepository.emitCoins(ApiState.Success(listOf(fakeCoin, secondCoin)))

        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithText("Binance").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("Binance").assertIsDisplayed()
        composeRule.onNodeWithText("Coinbase").assertIsDisplayed()
    }

    @Test
    fun homeScreen_showsErrorMessage_onFailure() {
        fakeRepository.emitCoins(ApiState.Error("Network error"))

        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithText("Network error").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("Network error").assertIsDisplayed()
    }

    @Test
    fun homeScreen_clickCoin_navigatesToDetail() {
        fakeRepository.emitCoins(ApiState.Success(listOf(fakeCoin)))

        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithText("Binance").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("Binance").performClick()

        composeRule.waitUntil(timeoutMillis = 3_000) {
            composeRule.onAllNodesWithText("MAKER FEE").fetchSemanticsNodes().isNotEmpty()
        }

        composeRule.onNodeWithText("MAKER FEE").assertIsDisplayed()
    }
}
