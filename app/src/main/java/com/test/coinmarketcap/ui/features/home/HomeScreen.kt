package com.test.coinmarketcap.ui.features.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.test.coinmarketcap.R
import com.test.coinmarketcap.domain.models.MapCoinsEntity
import com.test.coinmarketcap.ui.common.theme.CoinMarketCapTheme
import com.test.coinmarketcap.ui.common.theme.GrayDark
import com.test.coinmarketcap.ui.common.theme.PersianRed700
import com.test.coinmarketcap.ui.common.theme.White
import com.test.coinmarketcap.ui.components.HeaderList
import com.test.coinmarketcap.ui.components.ListDefaultLoadingContent
import com.test.coinmarketcap.ui.nav.AppScreen
import com.test.coinmarketcap.ui.viewmodel.HomeScreenViewModel
import com.test.coinmarketcap.utils.UiState
import com.test.coinmarketcap.utils.extensions.formatDate
import com.test.coinmarketcap.utils.extensions.toCurrencyUsd

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeScreenViewModel = hiltViewModel()
) {
    val uiMapCoinsState by viewModel.mapState.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .background(color = White)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        LaunchedEffect(Unit) {
            viewModel.mappedCoins()
        }

        MappedCoinsContent(
            state = uiMapCoinsState,
            onCoinClick = { coin ->
                navController.navigate(
                    AppScreen.CoinDetail.createRoute(
                        id = coin.id,
                        name = coin.name,
                        description = coin.description.orEmpty(),
                        logo = coin.logo,
                        website = coin.url,
                        makerFee = coin.makerFee,
                        takerFee = coin.takerFee,
                        dateLaunched = coin.dateLaunched
                    )
                )
            }
        )

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun MappedCoinsContent(
    state: UiState<List<MapCoinsEntity>>,
    onCoinClick: (MapCoinsEntity) -> Unit
) {
    when (state) {
        is UiState.Ready -> {}
        is UiState.Loading -> ListDefaultLoadingContent()
        is UiState.Success -> MappedCoinsItems(data = state.data, onCoinClick = onCoinClick)
        is UiState.Error -> MappedCoinsErrorContent(message = state.message)
    }
}

@Composable
private fun MappedCoinsErrorContent(message: String) {
    Row(
        modifier = Modifier.padding(10.dp)
    ) {
        Text(
            text = message,
            fontSize = 16.sp,
            color = PersianRed700
        )
    }
}

@Composable
private fun MappedCoinsItems(data: List<MapCoinsEntity>, onCoinClick: (MapCoinsEntity) -> Unit) {
    Column(
        modifier = Modifier.padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        HeaderList(text = stringResource(R.string.header_text))
        CoinLazyList(coins = data, onCoinClick = onCoinClick)
    }
}

@Composable
private fun CoinLazyList(coins: List<MapCoinsEntity>, onCoinClick: (MapCoinsEntity) -> Unit) {
    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(
            items = coins,
            key = { it.name }) { coin ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onCoinClick(coin) },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    ExchangeLogo(coin)
                    Column {
                        Text(
                            text = coin.name,
                            fontWeight = FontWeight.SemiBold,
                            color = GrayDark
                        )
                        coin.spotVolumeUsd?.let {
                            Text(
                                text = it.toCurrencyUsd(),
                                fontWeight = FontWeight.SemiBold,
                                color = GrayDark
                            )
                        }
                    }
                    Column(modifier = Modifier.align(Alignment.CenterVertically)) {
                        coin.dateLaunched?.let {
                            Text(text = it.formatDate(), color = GrayDark)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExchangeLogo(coin: MapCoinsEntity) {
    Column {
        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(coin.logo)
                .crossfade(true)
                .build(),
            contentDescription = coin.name,
            placeholder = painterResource(R.drawable.ic_placeholder),
            error = painterResource(R.drawable.ic_placeholder),
            modifier = Modifier.size(64.dp),
            contentScale = ContentScale.Crop
        )
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ScreenPreview() {
    CoinMarketCapTheme {
        HomeScreen(navController = androidx.navigation.compose.rememberNavController())
    }
}