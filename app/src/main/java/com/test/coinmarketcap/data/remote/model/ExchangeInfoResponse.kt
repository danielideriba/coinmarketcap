package com.test.coinmarketcap.data.remote.model

import com.google.gson.annotations.SerializedName

data class ExchangeInfoResponse(
    val data: Map<String, ExchangeInfo>
)

data class ExchangeInfo(
    val id: Int,
    val name: String,
    val slug: String,
    val logo: String?,
    val description: String?,
    val urls: ExchangeUrls? = null,
    val countries: List<String> = emptyList(),
    @SerializedName("date_launched")
    val dateLaunched: String? = null,
    @SerializedName("spot_volume_usd")
    val spotVolumeUsd: Double? = null,
    @SerializedName("spot_volume_last_updated")
    val spotVolumeLastUpdated: String? = null,
    @SerializedName("maker_fee")
    val makerFee: Double? = null,
    @SerializedName("taker_fee")
    val takerFee: Double? = null
)

data class ExchangeUrls(
    val website: List<String> = emptyList()
)
