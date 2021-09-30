package com.example.bloggers.base.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton


@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class UnconfinedDispatcher

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class DefaultDispatcher
@Module
@InstallIn(SingletonComponent::class)
object CoroutineModule {

    @Singleton
    @DefaultDispatcher
    @Provides
    fun provideDefaultDispatcher() = Dispatchers.Default

    @Singleton
    @UnconfinedDispatcher
    @Provides
    fun provideUnconfinedDispatcher() = Dispatchers.Unconfined

}