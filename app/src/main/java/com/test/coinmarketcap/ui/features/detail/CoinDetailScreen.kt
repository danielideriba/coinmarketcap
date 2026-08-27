package com.test.coinmarketcap.ui.features.detail

import android.accessibilityservice.GestureDescription
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import com.test.coinmarketcap.R

@Composable
fun CoinDetailScreen(
    id: Int,
    name: String,
    description: String,
    logo: String,
    url: String,
    makerFee: String,
    takerFee: String,
    dateLaunched: String
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = id.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = description, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = logo, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = url, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = makerFee, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = takerFee, fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Text(text = dateLaunched, fontSize = 24.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(8.dp))
    }
}