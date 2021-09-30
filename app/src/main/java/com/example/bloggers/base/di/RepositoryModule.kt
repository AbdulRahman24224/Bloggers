package com.example.bloggers.base.di

import com.example.bloggers.domain.data.AuthorsApis
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
    ): AuthorsRepository {
        return AuthorsRepository(articleApi )
    }
}