package com.test.coinmarketcap.ui.features.detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
    dateLaunched: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = website, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = id.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        MarkdownText(
            markdown = description ?: "",
            modifier = Modifier.padding(16.dp)
        )
        Text(text = logo, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = makerFee, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = takerFee, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = dateLaunched, fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))
    }
}