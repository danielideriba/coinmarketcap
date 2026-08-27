package com.test.coinmarketcap.fake

import com.test.coinmarketcap.data.remote.model.CoinWithExchangeInfo
import com.test.coinmarketcap.domain.models.ExchangeAssetEntity
import com.test.coinmarketcap.domain.repository.CryptocurrencyRepository
import com.test.coinmarketcap.utils.ApiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FakeCryptocurrencyRepository @Inject constructor() : CryptocurrencyRepository {

    private val coinsFlow = MutableSharedFlow<ApiState<List<CoinWithExchangeInfo>>>(replay = 1)
    private val assetsFlow = MutableSharedFlow<ApiState<List<ExchangeAssetEntity>>>(replay = 1)

    fun emitCoins(state: ApiState<List<CoinWithExchangeInfo>>) {
        coinsFlow.tryEmit(state)
    }

    fun emitAssets(state: ApiState<List<ExchangeAssetEntity>>) {
        assetsFlow.tryEmit(state)
    }

    override fun mapCoins(limit: Int): Flow<ApiState<List<CoinWithExchangeInfo>>> = coinsFlow

    override fun getExchangeAssets(id: Int): Flow<ApiState<List<ExchangeAssetEntity>>> = assetsFlow
}
