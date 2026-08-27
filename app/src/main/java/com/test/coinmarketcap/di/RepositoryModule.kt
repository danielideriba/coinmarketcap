package com.test.coinmarketcap.di

import com.test.coinmarketcap.data.datasource.CryptocurrencyDataSource
import com.test.coinmarketcap.data.datasource.CryptocurrencyDataSourceImpl
import com.test.coinmarketcap.data.repository.CryptocurrencyRepositoryImpl
import com.test.coinmarketcap.domain.repository.CryptocurrencyRepository
import com.test.coinmarketcap.utils.DefaultDispatcherProvider
import com.test.coinmarketcap.utils.DispatchersProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Singleton
    @Binds
    abstract fun provideShortenerDataSource(impl: CryptocurrencyDataSourceImpl): CryptocurrencyDataSource

    @Singleton
    @Binds
    abstract fun provideShortenerRepository(impl: CryptocurrencyRepositoryImpl): CryptocurrencyRepository

    @Singleton
    @Binds
    abstract fun provideDispatchersProvider(impl: DefaultDispatcherProvider): DispatchersProvider
}