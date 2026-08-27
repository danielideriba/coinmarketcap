package com.test.coinmarketcap.ui.features.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.test.coinmarketcap.ui.common.theme.CoinMarketCapTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CoinDetailActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val name = intent.getStringExtra(EXTRA_NAME).orEmpty()
        val symbol = intent.getStringExtra(EXTRA_SYMBOL).orEmpty()
        val rank = intent.getIntExtra(EXTRA_RANK, 0)
        val isActive = intent.getIntExtra(EXTRA_IS_ACTIVE, 0)

        setContent {
            CoinMarketCapTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Surface(modifier = Modifier.padding(innerPadding)) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {
                            Spacer(modifier = Modifier.height(24.dp))
                            Text(text = name, fontSize = 24.sp, fontWeight = FontWeight.Bold)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Symbol: $symbol", fontSize = 16.sp)
                            Text(text = "Rank: $rank", fontSize = 16.sp)
                            Text(text = "Active: ${if (isActive == 1) "Yes" else "No"}", fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val EXTRA_NAME = "extra_name"
        private const val EXTRA_SYMBOL = "extra_symbol"
        private const val EXTRA_RANK = "extra_rank"
        private const val EXTRA_IS_ACTIVE = "extra_is_active"

        fun newIntent(context: Context, name: String, symbol: String, rank: Int, isActive: Int): Intent =
            Intent(context, CoinDetailActivity::class.java).apply {
                putExtra(EXTRA_NAME, name)
                putExtra(EXTRA_SYMBOL, symbol)
                putExtra(EXTRA_RANK, rank)
                putExtra(EXTRA_IS_ACTIVE, isActive)
            }
    }
}