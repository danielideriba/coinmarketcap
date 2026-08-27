package com.test.coinmarketcap.di

import com.test.coinmarketcap.domain.repository.CryptocurrencyRepository
import com.test.coinmarketcap.fake.FakeCryptocurrencyRepository
import com.test.coinmarketcap.utils.DefaultDispatcherProvider
import com.test.coinmarketcap.utils.DispatchersProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import javax.inject.Singleton

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [RepositoryModule::class]
)
abstract class TestRepositoryModule {

    @Singleton
    @Binds
    abstract fun provideRepository(impl: FakeCryptocurrencyRepository): CryptocurrencyRepository

    @Singleton
    @Binds
    abstract fun provideDispatchersProvider(impl: DefaultDispatcherProvider): DispatchersProvider
}
