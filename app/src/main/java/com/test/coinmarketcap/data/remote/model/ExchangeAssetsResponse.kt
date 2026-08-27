package com.test.coinmarketcap.data.remote.model

import com.google.gson.annotations.SerializedName

data class ExchangeAssetsResponse(
    val data: Map<String, ExchangeAsset>
)

data class ExchangeAsset(
    @SerializedName("wallet_address")
    val walletAddress: String,
    val balance: Double,
    val platform: Platform,
    val currency: Currency
)

data class Platform(
    @SerializedName("crypto_id")
    val cryptoId: Int,
    val symbol: String,
    val name: String
)

data class Currency(
    @SerializedName("crypto_id")
    val cryptoId: Int,
    @SerializedName("price_usd")
    val priceUsd: Double,
    val symbol: String,
    val name: String
)
