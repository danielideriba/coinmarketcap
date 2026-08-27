package com.test.coinmarketcap.domain.usecase

import android.util.Log
import com.test.coinmarketcap.domain.models.MapCoinsEntity
import com.test.coinmarketcap.domain.repository.CryptocurrencyRepository
import com.test.coinmarketcap.utils.ApiState
import com.test.coinmarketcap.utils.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CryptocurrencyMapUseCase @Inject constructor(
    private val cryptocurrencyRepository: CryptocurrencyRepository
) {
    fun invoke(limit: Int): Flow<UiState<List<MapCoinsEntity>>> {
        return cryptocurrencyRepository.mapCoins(limit).map { result ->
            when (result) {
                is ApiState.Success -> UiState.Success(
                    result.data.map { item ->
                        MapCoinsEntity(
                            id = item.id,
                            name = item.name,
                            url = item.urls!!.website[0],
                            makerFee = item.makerFee,
                            takerFee = item.takerFee,
                            description = item.description,
                            slug = item.slug,
                            isActive = item.isActive,
                            logo = item.logo,
                            spotVolumeUsd = item.spotVolumeUsd,
                            dateLaunched = item.dateLaunched
                        )
                    }
                )
                is ApiState.Error -> {
                    val errorMessage = when {
                        result.message.contains(
                            "not found",
                            ignoreCase = true
                        ) -> "Resources not found. Please check the spelling and try again."

                        else -> result.message
                    }
                    UiState.Error(errorMessage)
                }
            }
        }
    }
}