package com.test.coinmarketcap.domain.repository

import com.test.coinmarketcap.data.remote.model.CoinWithExchangeInfo
import com.test.coinmarketcap.utils.ApiState
import kotlinx.coroutines.flow.Flow

interface CryptocurrencyRepository {
    fun mapCoins(limit: Int): Flow<ApiState<List<CoinWithExchangeInfo>>>
}