package com.example.domain.usecases.authors


import com.example.domain.repositories.AuthorsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AuthorUseCasesModule {

    @Provides
    @Singleton
    private fun provideRetrieveAuthorPosts(
        repository: AuthorsRepository
    ): RetrieveAuthorPostsUseCase = RetrieveAuthorPostsUseCase(repository)

    @Provides
    @Singleton
    private fun provideRetrieveAuthorsUseCase(
        repository: AuthorsRepository
    ): RetrieveAuthorsUseCase = RetrieveAuthorsUseCase(repository)

    @Provides
    @Singleton
    private fun provideRetrieveSingleAuthorUseCase(
        repository: AuthorsRepository
    ): RetrieveSingleAuthorUseCase = RetrieveSingleAuthorUseCase(repository)
}