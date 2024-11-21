package ru.kabirov.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kabirov.IpAddressRepositoryImpl
import ru.kabirov.data.IpAddressRepository

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindIpAddressRepository(
        ipAddressRepositoryImpl: IpAddressRepositoryImpl
    ): IpAddressRepository
}