package com.example.domain.repositories

import com.example.data.di.AppDatabase
import com.example.data.remote.Authors
import com.example.data.remote.AuthorsApis
import com.example.data.remote.Posts
import com.example.domain.usecases.authors.RetrieveAuthorPostsUseCase
import com.example.domain.usecases.authors.RetrieveAuthorsUseCase
import com.example.domain.usecases.authors.RetrieveSingleAuthorUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
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

