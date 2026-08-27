package com.test.coinmarketcap.data.remote.model

data class CoinWithExchangeInfo(
    val id: Int,
    val name: String,
    val slug: String,
    val urls: ExchangeUrls? = null,
    val firstHistoricalData: String? = null,
    val lastHistoricalData: String? = null,
    val isActive: Int,
    val isListed: Int? = null,
    val isRedistributable: Int? = null,
    val logo: String,
    val spotVolumeUsd: Double?,
    val dateLaunched: String?,
    val description: String? = null,
    val makerFee: Int? = null,
    val takerFee: Int? = null,
)