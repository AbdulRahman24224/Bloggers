package com.example.domain.repositories

import com.example.data.di.AppDatabase
import com.example.data.remote.AuthorsApis
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    private fun provideArticlesRepository(
        articleApi: AuthorsApis,
        database: AppDatabase,
    ): AuthorsRepository {
        return AuthorsRepository(articleApi, database)
    }
}

