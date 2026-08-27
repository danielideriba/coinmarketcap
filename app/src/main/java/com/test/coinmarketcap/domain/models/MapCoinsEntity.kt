package com.test.coinmarketcap.domain.models

data class MapCoinsEntity(
    val id: Int,
    val name: String,
    val slug: String,
    val url: String,
    val description: String? = null,
    val isActive: Int,
    val logo: String,
    val makerFee: String? = null,
    val takerFee: String? = null,
    val spotVolumeUsd: Double?,
    val dateLaunched: String?
)