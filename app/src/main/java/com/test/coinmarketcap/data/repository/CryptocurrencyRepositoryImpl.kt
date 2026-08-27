package com.test.coinmarketcap.data.repository

import android.util.Log
import com.test.coinmarketcap.data.datasource.CryptocurrencyDataSource
import com.test.coinmarketcap.data.remote.model.CoinWithExchangeInfo
import com.test.coinmarketcap.domain.repository.CryptocurrencyRepository
import com.test.coinmarketcap.utils.ApiState
import com.test.coinmarketcap.utils.DispatchersProvider
import com.test.coinmarketcap.utils.safeApiCall
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class CryptocurrencyRepositoryImpl @Inject constructor(
    private val cryptocurrencyDataSource: CryptocurrencyDataSource,
    private val dispatchersProvider: DispatchersProvider
) : CryptocurrencyRepository {

    override fun mapCoins(limit: Int): Flow<ApiState<List<CoinWithExchangeInfo>>> = flow {
        val mapResult = safeApiCall { cryptocurrencyDataSource.mapCoins(limit) }

        if (mapResult is ApiState.Error) {
            emit(mapResult)
            return@flow
        }

        val coins = (mapResult as ApiState.Success).data.data
        val coinsIds = coins.joinToString(",") { it.id.toString() }

        Log.i("TAG", "----IDS---$coinsIds")

        val exchangeResult = safeApiCall { cryptocurrencyDataSource.exchangeInfo(coinsIds) }
        val exchangeData = if (exchangeResult is ApiState.Success) {
            Log.i("TAG", "----MAP---${exchangeResult.data.data}")
            exchangeResult.data.data
        } else {
            Log.i("TAG", "----MAP---EMPTY")
            emptyMap()
        }

        val combined = coins.map { coin ->
            val exchangeItem = exchangeData[coin.id.toString()]
            CoinWithExchangeInfo(
                id = coin.id,
                name = coin.name,
                slug = coin.slug,
                isActive = coin.isActive,
                logo = exchangeItem?.logo.orEmpty(),
                spotVolumeUsd = exchangeItem?.spotVolumeUsd,
                dateLaunched = exchangeItem?.dateLaunched
            )
        }

        emit(ApiState.Success(combined))
    }.flowOn(dispatchersProvider.io())
}
