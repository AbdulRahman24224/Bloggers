package com.example.domain.usecases.authors

import com.example.data.entities.Author
import com.example.domain.repositories.AuthorsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RetrieveSingleAuthorUseCase
@Inject constructor(private val repository: AuthorsRepository)  {

        operator fun invoke(authorId :Int): Flow<Author?> =
            flow { emit( getAuthor(authorId) ) }

    private suspend fun getAuthor(authorId: Int) = repository.getAuthorById(authorId).firstOrNull()
}