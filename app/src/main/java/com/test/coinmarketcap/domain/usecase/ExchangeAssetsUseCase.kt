package com.test.coinmarketcap.domain.usecase

import com.test.coinmarketcap.domain.models.ExchangeAssetEntity
import com.test.coinmarketcap.domain.repository.CryptocurrencyRepository
import com.test.coinmarketcap.utils.ApiState
import com.test.coinmarketcap.utils.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExchangeAssetsUseCase @Inject constructor(
    private val cryptocurrencyRepository: CryptocurrencyRepository
) {
    fun invoke(id: Int): Flow<UiState<List<ExchangeAssetEntity>>> {
        return cryptocurrencyRepository.getExchangeAssets(id).map { result ->
            when (result) {
                is ApiState.Success -> UiState.Success(result.data)
                is ApiState.Error -> UiState.Error(result.message)
            }
        }
    }
}