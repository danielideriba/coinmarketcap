package com.test.coinmarketcap.ui.components

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithTag
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertCountEquals
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.test.onRoot
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ListDefaultLoadingContentTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun listDefaultLoadingContent_isDisplayed() {
        composeRule.setContent {
            ListDefaultLoadingContent()
        }

        composeRule.onRoot().assertIsDisplayed()
    }

    @Test
    fun listDefaultLoadingContent_withZeroElements_isDisplayed() {
        composeRule.setContent {
            ListDefaultLoadingContent(elements = 0)
        }

        composeRule.onRoot().assertIsDisplayed()
    }

    @Test
    fun listDefaultLoadingContent_withCustomElements_isDisplayed() {
        composeRule.setContent {
            ListDefaultLoadingContent(elements = 3)
        }

        composeRule.onRoot().assertIsDisplayed()
    }

    @Test
    fun listDefaultLoadingContent_defaultElements_isDisplayed() {
        composeRule.setContent {
            ListDefaultLoadingContent(elements = 10)
        }

        composeRule.onRoot().assertIsDisplayed()
    }
}
