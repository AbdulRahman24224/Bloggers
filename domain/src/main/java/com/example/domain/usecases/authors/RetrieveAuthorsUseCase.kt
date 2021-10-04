package com.example.domain.usecases.authors

import com.example.data.remote.Result
import com.example.domain.repositories.AuthorsRepository
import com.example.domain.usecases.authors.states.AuthorsListState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class RetrieveAuthorsUseCase
@Inject constructor(
    private val repository: AuthorsRepository
) {

   operator fun invoke(page: Int, isConnected: Boolean): Flow<AuthorsListState> =
        flow<AuthorsListState> {

            //choosing source of data based on network state and existence of cached data
          emit(
              when{
                  isConnected -> retrieveFromServer(page)
                  (!(isConnected.not() and repository.isDBEmpty())) -> retrieveFromDatabase(page)
                  else ->AuthorsListState(status = "false", error = "Couldn't retrieve any authors")
              }
          )

        }

    private suspend fun retrieveFromServer(
        page: Int
    ) : AuthorsListState{
        // clear old data if New data will be retrieved from server
        if (page == 1) repository.deleteAllAuthors()

        return when (val authorsResponse = repository.getAuthorsFromServer(page)) {
            is Result.Success -> {

                authorsResponse.data?.let {
                    it.forEach { it.page = page }
                    repository.insertAuthors(it)
                    AuthorsListState(authors = it.toMutableList())
                }?: AuthorsListState()
            }
            is Result.Failure -> {
                //todo get rid of this "false"
                AuthorsListState(status = "false", error = "Request failed")
            }
            else ->
                AuthorsListState(status = "false", error = "Couldn't retrieve authors")
        }
    }

    private suspend fun retrieveFromDatabase(
        page: Int
    )  = AuthorsListState(authors = repository.getAuthorsFromDatabase(page))

}