package com.test.coinmarketcap.data.datasource

import com.test.coinmarketcap.data.remote.model.CryptocurrencyMapResponse
import com.test.coinmarketcap.data.remote.model.ExchangeInfoResponse
import retrofit2.Response

interface CryptocurrencyDataSource {
    suspend fun mapCoins(limit: Int): Response<CryptocurrencyMapResponse>
    suspend fun exchangeInfo(id: String): Response<ExchangeInfoResponse>
}