package ru.kabirov.ipsearcher

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.kabirov.database.IpSearcherDatabase
import ru.kabirov.ripeapi.RipeApi
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideRipeApi(): RipeApi {
        return RipeApi(baseUrl = "https://rest.db.ripe.net/")
    }

    @Provides
    @Singleton
    fun provideIpSearcherDatabase(
        @ApplicationContext context: Context
    ): IpSearcherDatabase {
        return IpSearcherDatabase(context)
    }
}