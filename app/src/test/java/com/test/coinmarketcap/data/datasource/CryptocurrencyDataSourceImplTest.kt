package com.test.coinmarketcap.data.datasource

import com.test.coinmarketcap.data.remote.CryptocurrencyMapApiService
import com.test.coinmarketcap.data.remote.ExchangeApiService
import com.test.coinmarketcap.data.remote.model.CryptocurrencyMapItem
import com.test.coinmarketcap.data.remote.model.CryptocurrencyMapResponse
import com.test.coinmarketcap.data.remote.model.ExchangeAssetsResponse
import com.test.coinmarketcap.data.remote.model.ExchangeInfoResponse
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import retrofit2.Response

class CryptocurrencyDataSourceImplTest {

    private lateinit var mapApiService: CryptocurrencyMapApiService
    private lateinit var exchangeApiService: ExchangeApiService
    private lateinit var dataSource: CryptocurrencyDataSourceImpl

    @Before
    fun setUp() {
        mapApiService = mockk()
        exchangeApiService = mockk()
        dataSource = CryptocurrencyDataSourceImpl(mapApiService, exchangeApiService)
    }

    @Test
    fun `mapCoins delegates to cryptocurrencyMapApi`() = runTest {
        val fakeResponse = Response.success(
            CryptocurrencyMapResponse(
                data = listOf(CryptocurrencyMapItem(id = 1, name = "Binance", slug = "binance", isActive = 1))
            )
        )
        coEvery { mapApiService.getMapCoins(30) } returns fakeResponse

        val result = dataSource.mapCoins(30)

        assertEquals(fakeResponse, result)
        coVerify(exactly = 1) { mapApiService.getMapCoins(30) }
    }

    @Test
    fun `exchangeInfo delegates to exchangeInfoApi with correct aux params`() = runTest {
        val fakeResponse = Response.success(ExchangeInfoResponse(data = emptyMap()))
        coEvery {
            exchangeApiService.getExchangeInfo("1", aux = "urls,logo,description,date_launched,notice,status")
        } returns fakeResponse

        val result = dataSource.exchangeInfo("1")

        assertEquals(fakeResponse, result)
        coVerify(exactly = 1) {
            exchangeApiService.getExchangeInfo("1", aux = "urls,logo,description,date_launched,notice,status")
        }
    }

    @Test
    fun `getAssets delegates to exchangeInfoApi`() = runTest {
        val fakeResponse = Response.success(ExchangeAssetsResponse(data = emptyList()))
        coEvery { exchangeApiService.getAssets("1") } returns fakeResponse

        val result = dataSource.getAssets("1")

        assertEquals(fakeResponse, result)
        coVerify(exactly = 1) { exchangeApiService.getAssets("1") }
    }
}
