package com.test.coinmarketcap.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.coinmarketcap.domain.models.ExchangeAssetEntity
import com.test.coinmarketcap.domain.usecase.ExchangeAssetsUseCase
import com.test.coinmarketcap.utils.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoinDetailViewModel @Inject constructor(
    private val exchangeAssetsUseCase: ExchangeAssetsUseCase
) : ViewModel() {

    private val _assetsState = MutableStateFlow<UiState<List<ExchangeAssetEntity>>>(UiState.Ready)
    val assetsState: StateFlow<UiState<List<ExchangeAssetEntity>>> = _assetsState.asStateFlow()

    fun loadAssets(id: Int) {
        viewModelScope.launch {
            exchangeAssetsUseCase.invoke(id)
                .onStart { _assetsState.value = UiState.Loading }
                .catch { error -> _assetsState.value = UiState.Error(error.localizedMessage.orEmpty()) }
                .collect { result ->
                    _assetsState.value = result
                }
        }
    }
}