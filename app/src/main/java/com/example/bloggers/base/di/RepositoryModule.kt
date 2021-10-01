package com.example.bloggers.base.di

import com.example.bloggers.domain.data.remote.AuthorsApis
import com.example.bloggers.domain.repository.AuthorsRepository
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
    fun provideArticlesRepository(
        articleApi: AuthorsApis,
        database: AppDatabase,
    ): AuthorsRepository {
        return AuthorsRepository(articleApi , database)
    }
}