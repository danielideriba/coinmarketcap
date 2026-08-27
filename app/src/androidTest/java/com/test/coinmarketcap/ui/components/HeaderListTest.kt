package com.test.coinmarketcap.ui.components

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class HeaderListTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun headerList_displaysText() {
        composeRule.setContent {
            HeaderList(text = "Coin Market Cap - Coin list")
        }

        composeRule.onNodeWithText("Coin Market Cap - Coin list").assertIsDisplayed()
    }

    @Test
    fun headerList_displaysLongText() {
        val longText = "A".repeat(100)
        composeRule.setContent {
            HeaderList(text = longText)
        }

        composeRule.onNodeWithText(longText).assertIsDisplayed()
    }
}
