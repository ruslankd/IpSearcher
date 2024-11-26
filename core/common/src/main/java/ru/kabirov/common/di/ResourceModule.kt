package ru.kabirov.common.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.kabirov.common.resource.AndroidResourceManager
import ru.kabirov.common.resource.ResourceManager

@Module
@InstallIn(SingletonComponent::class)
object ResourceModule {
    @Provides
    fun provideResourceManager(@ApplicationContext context: Context): ResourceManager {
        return AndroidResourceManager(context)
    }
}