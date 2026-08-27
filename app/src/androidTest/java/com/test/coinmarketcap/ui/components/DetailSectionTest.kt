package com.test.coinmarketcap.ui.components

import androidx.compose.material3.Text
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DetailSectionTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun detailSection_displaysLabelInUppercase() {
        composeRule.setContent {
            DetailSection(label = "website") {
                Text(text = "https://binance.com")
            }
        }

        composeRule.onNodeWithText("WEBSITE").assertIsDisplayed()
    }

    @Test
    fun detailSection_displaysContent() {
        composeRule.setContent {
            DetailSection(label = "Maker Fee") {
                Text(text = "0.100%")
            }
        }

        composeRule.onNodeWithText("0.100%").assertIsDisplayed()
    }

    @Test
    fun detailSection_emptyLabel_doesNotDisplayLabelText() {
        composeRule.setContent {
            DetailSection(label = "") {
                Text(text = "some content")
            }
        }

        composeRule.onNodeWithText("").assertIsNotDisplayed()
        composeRule.onNodeWithText("some content").assertIsDisplayed()
    }

    @Test
    fun detailSection_displaysLabelAndContentTogether() {
        composeRule.setContent {
            DetailSection(label = "Date Launched") {
                Text(text = "14/07/2017")
            }
        }

        composeRule.onNodeWithText("DATE LAUNCHED").assertIsDisplayed()
        composeRule.onNodeWithText("14/07/2017").assertIsDisplayed()
    }
}
