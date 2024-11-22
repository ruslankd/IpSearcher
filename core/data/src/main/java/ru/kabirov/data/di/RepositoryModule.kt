package ru.kabirov.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.kabirov.data.impl.IpAddressRepositoryImpl
import ru.kabirov.data.api.IpAddressRepository
import ru.kabirov.data.api.OrganisationRepository
import ru.kabirov.data.impl.OrganisationRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    abstract fun bindIpAddressRepository(
        ipAddressRepositoryImpl: IpAddressRepositoryImpl,
    ): IpAddressRepository

    @Binds
    abstract fun bindOrganisationRepository(
        organisationRepositoryImpl: OrganisationRepositoryImpl,
    ): OrganisationRepository
}