package com.test.coinmarketcap.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.test.coinmarketcap.domain.models.MapCoinsEntity
import com.test.coinmarketcap.domain.usecase.CryptocurrencyMapUseCase
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
class HomeScreenViewModel @Inject constructor(
    private val cryptocurrencyMapUseCase: CryptocurrencyMapUseCase
) : ViewModel() {
    private val _mapState = MutableStateFlow<UiState<List<MapCoinsEntity>>>(UiState.Ready)
    val mapState: StateFlow<UiState<List<MapCoinsEntity>>> = _mapState.asStateFlow()

    fun setLoading() {
        _mapState.value = UiState.Loading
    }

    fun mappedCoins() {
        viewModelScope.launch {
            cryptocurrencyMapUseCase.invoke(30)
                .onStart { _mapState.value = UiState.Loading }
                .catch { error -> _mapState.value = UiState.Error("${error.localizedMessage}") }
                .collect { result ->
                    _mapState.value = result
                }
        }
    }
}