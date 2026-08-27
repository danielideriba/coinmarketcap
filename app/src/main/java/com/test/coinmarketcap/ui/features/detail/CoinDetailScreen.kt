package com.test.coinmarketcap.ui.features.detail

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.test.coinmarketcap.ui.viewmodel.CoinDetailViewModel
import com.test.coinmarketcap.utils.UiState
import com.test.coinmarketcap.utils.extensions.formatDate
import com.test.coinmarketcap.utils.extensions.toCurrencyUsd
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun CoinDetailScreen(
    id: Int,
    name: String,
    description: String,
    logo: String,
    website: String,
    makerFee: String,
    takerFee: String,
    dateLaunched: String,
    viewModel: CoinDetailViewModel = hiltViewModel()
) {
    val assetsState by viewModel.assetsState.collectAsStateWithLifecycle()

    LaunchedEffect(id) {
        viewModel.loadAssets(id)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(24.dp))
            Text(text = website, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = id.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            MarkdownText(
                markdown = description,
                modifier = Modifier.padding(16.dp)
            )
            Text(text = logo, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = makerFee, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = takerFee, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Text(text = dateLaunched.formatDate(), fontSize = 24.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(8.dp))
        }

        when (val state = assetsState) {
            is UiState.Success -> {
                items(state.data) { asset ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = asset.currencyName, fontWeight = FontWeight.Medium)
                        Text(text = asset.priceUsd.toCurrencyUsd())
                    }
                }
            }
            else -> {}
        }
    }
}