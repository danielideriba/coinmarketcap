package com.test.coinmarketcap.data.remote

import com.test.coinmarketcap.data.remote.model.ExchangeInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ExchangeInfoApiService {
    @GET("/v1/exchange/info")
    suspend fun getExchangeInfo(
        @Query("id") id: String,
        @Query("aux") aux: String,
    ): Response<ExchangeInfoResponse>
}
