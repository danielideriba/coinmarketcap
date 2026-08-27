package com.test.coinmarketcap.ui.features.detail

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.AsyncImage
import com.test.coinmarketcap.R
import com.test.coinmarketcap.ui.components.DetailSection
import com.test.coinmarketcap.ui.components.ListDefaultLoadingContent
import com.test.coinmarketcap.ui.viewmodel.CoinDetailViewModel
import com.test.coinmarketcap.utils.UiState
import com.test.coinmarketcap.utils.extensions.formatDate
import com.test.coinmarketcap.utils.extensions.toCurrencyUsd
import com.test.coinmarketcap.utils.extensions.toFeePercentage
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun CoinDetailScreen(
    id: Int,
    name: String,
    description: String,
    logo: String,
    website: String,
    makerFee: Double,
    takerFee: Double,
    dateLaunched: String,
    onNavigateBack: () -> Unit = {},
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
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
                    .padding(top = 16.dp)
            ) {
                IconButton(onClick = onNavigateBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = stringResource(R.string.back_bottom)
                    )
                }
                AsyncImage(
                    model = logo,
                    contentDescription = stringResource(R.string.image_content_description, name),
                    modifier = Modifier
                        .padding(5.dp)
                        .size(56.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    contentScale = ContentScale.Crop
                )
                Column {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                    Text(
                        text = "ID: $id",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        item {
            HorizontalDivider(modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp))
        }
        item {
            DetailSection(
                label = stringResource(R.string.details_date_launched),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = dateLaunched.formatDate(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        item {
            DetailSection(
                label = stringResource(R.string.details_maker_fee),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = makerFee.toFeePercentage(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        item {
            DetailSection(
                label = stringResource(R.string.details_taker_fee),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                Text(
                    text = takerFee.toFeePercentage(),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
        item {
            val uriHandler = LocalUriHandler.current

            DetailSection(
                label = stringResource(R.string.details_website),
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp)
            ) {
                if (!website.isEmpty()) {
                    Text(
                        text = website,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.primary,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { uriHandler.openUri(website) }
                    )
                } else {
                    Text(
                        text = stringResource(R.string.content_error),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            val uriHandler = LocalUriHandler.current
            var expanded by remember { mutableStateOf(false) }

            DetailSection(
                label = "",
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 16.dp)
            ) {
                if (!description.isEmpty()) {
                    val lines = description.lines()
                    val preview = lines.take(4).joinToString("\n")
                    val hasMore = lines.size > 4

                    MarkdownText(
                        markdown = if (expanded) description else preview,
                        style = MaterialTheme.typography.bodyMedium,
                        onLinkClicked = { url -> uriHandler.openUri(url) }
                    )

                    if (hasMore) {
                        Button(
                            onClick = { expanded = !expanded },
                            modifier = Modifier.padding(top = 8.dp)
                        ) {
                            Text(
                                if (expanded) {
                                    stringResource(R.string.see_more_button_hide)
                                } else {
                                    stringResource(R.string.see_more_button_expand)
                                }
                            )
                        }
                    }
                } else {
                    Text(
                        text = stringResource(R.string.content_error_no_description),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        when (val state = assetsState) {
            is UiState.Success -> {
                items(state.data) { asset ->
                    DetailSection(
                        label = stringResource(R.string.exchange_coin_list),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
                    ) {
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
            }

            is UiState.Error -> {
                item {
                    DetailSection(
                        label = stringResource(R.string.exchange_coin_list),
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.content_error_no_description),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            is UiState.Loading -> {
                item {
                    ListDefaultLoadingContent(10)
                }
            }
            else -> {}
        }
    }
}