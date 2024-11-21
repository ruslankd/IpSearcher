package ru.kabirov.searcherbyip.di

import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kabirov.IpAddressRepositoryImpl
import ru.kabirov.data.IpAddressRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object IpAddressModule {

}
