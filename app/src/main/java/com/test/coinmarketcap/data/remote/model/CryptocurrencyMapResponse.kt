package com.test.coinmarketcap.data.remote.model

import com.google.gson.annotations.SerializedName

data class CryptocurrencyMapResponse(
    val data: List<CryptocurrencyMapItem>
)

data class CryptocurrencyMapItem(
    val id: Int,
    val name: String,
    val slug: String,
    @SerializedName("first_historical_data")
    val firstHistoricalData: String? = null,
    @SerializedName("last_historical_data")
    val lastHistoricalData: String? = null,
    @SerializedName("is_active")
    val isActive: Int = 0,
    @SerializedName("is_listed")
    val isListed: Int? = null,
    @SerializedName("is_redistributable")
    val isRedistributable: Int? = null
)
