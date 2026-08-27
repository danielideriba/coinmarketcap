package com.test.coinmarketcap.data.remote

import com.test.coinmarketcap.data.remote.model.CryptocurrencyMapResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface CryptocurrencyMapApiService {
    @GET("/v1/exchange/map")
    suspend fun getMapCoins(
        @Query("limit") limit: Int
    ): Response<CryptocurrencyMapResponse>
}