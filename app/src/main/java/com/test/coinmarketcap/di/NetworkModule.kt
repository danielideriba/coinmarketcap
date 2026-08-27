package com.test.coinmarketcap.di

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.test.coinmarketcap.data.remote.ApiClientConstants.API_KEY
import com.test.coinmarketcap.data.remote.ApiClientConstants.HEADER_PARAM_API_KEY
import com.test.coinmarketcap.data.remote.ApiPath.BASE_URL
import com.test.coinmarketcap.data.remote.CryptocurrencyMapApiService
import com.test.coinmarketcap.data.remote.ExchangeInfoApiService
import com.test.coinmarketcap.utils.NetworkConstants.CONNECTION_TIMEOUT
import com.test.coinmarketcap.utils.NetworkConstants.READ_TIMEOUT
import com.test.coinmarketcap.utils.NetworkConstants.WRITE_TIMEOUT
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideHttpInterceptor(): HttpLoggingInterceptor {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.NONE)

        return loggingInterceptor
    }

    @Singleton
    @Provides
    fun provideCache(context: Context): Cache {
        val cacheSize = 10 * 1024 * 1024
        return Cache(context.cacheDir, cacheSize.toLong())
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            .create()
    }

    @Singleton
    @Provides
    fun provideGsonConverterFactory(gson: Gson): GsonConverterFactory =
        GsonConverterFactory.create(gson)

    @Singleton
    @Provides
    fun provideAuthInterceptor(): Interceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader(HEADER_PARAM_API_KEY, API_KEY)
            .build()
        chain.proceed(request)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        loggingInterceptor: HttpLoggingInterceptor,
        authInterceptor: Interceptor,
        cache: Cache
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .cache(cache)
            .connectTimeout(CONNECTION_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT.toLong(), TimeUnit.SECONDS)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(gsonConverterFactory)
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retorfit: Retrofit): CryptocurrencyMapApiService =
        retorfit.create(CryptocurrencyMapApiService::class.java)

    @Singleton
    @Provides
    fun provideExchangeInfoApiService(retrofit: Retrofit): ExchangeInfoApiService =
        retrofit.create(ExchangeInfoApiService::class.java)
}