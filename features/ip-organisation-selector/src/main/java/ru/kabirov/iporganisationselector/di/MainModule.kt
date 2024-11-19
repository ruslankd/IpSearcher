package ru.kabirov.iporganisationselector.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kabirov.iporganisationselector.IpAddressValidator

@Module
@InstallIn(SingletonComponent::class)
class MainModule {

    @Provides
    fun provideIpAddressValidator(): IpAddressValidator {
        return IpAddressValidator()
    }
}