package com.test.coinmarketcap.data.datasource

import com.test.coinmarketcap.data.remote.CryptocurrencyMapApiService
import com.test.coinmarketcap.data.remote.ExchangeApiService
import com.test.coinmarketcap.data.remote.model.CryptocurrencyMapResponse
import com.test.coinmarketcap.data.remote.model.ExchangeInfoResponse
import retrofit2.Response
import javax.inject.Inject

class CryptocurrencyDataSourceImpl @Inject constructor(
    private val cryptocurrencyMapApi: CryptocurrencyMapApiService,
    private val exchangeInfoApi: ExchangeApiService
) : CryptocurrencyDataSource {
    override suspend fun mapCoins(limit: Int): Response<CryptocurrencyMapResponse> =
        cryptocurrencyMapApi.getMapCoins(limit)

    override suspend fun exchangeInfo(id: String): Response<ExchangeInfoResponse> =
        exchangeInfoApi.getExchangeInfo(id, aux = "urls,logo,description,date_launched,notice,status")
}