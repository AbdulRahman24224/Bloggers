package com.example.domain.usecases.authors

import com.example.data.remote.Result
import com.example.domain.repositories.AuthorsRepository
import com.example.domain.usecases.authors.states.AuthorsProfileState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject


class RetrieveAuthorPostsUseCase
@Inject constructor(
    private val repository: AuthorsRepository
) {

    operator fun invoke(  authorId :Int , page: Int,isConnected: Boolean): Flow<AuthorsProfileState> =
        flow<AuthorsProfileState> {

            //choosing source of data based on network state and existence of cached data
          emit(
              when{
                  isConnected -> retrieveFromServer(page , authorId)
                  (!(isConnected.not() and repository.isDBEmpty())) -> retrieveFromDatabase(page , authorId)
                  else ->AuthorsProfileState(status = "false", error = "Couldn't retrieve author data")
              }
          )

        }

    private suspend fun retrieveFromServer(
        page: Int ,
        authorId: Int
    ) : AuthorsProfileState{
        // clear old data if New data will be retrieved from server
        if (page == 1) repository.deleteAuthorPosts(authorId)

        return when (val authorsResponse = repository.getAuthorPostsFromServer(authorId ,page)) {
            is Result.Success -> {

                authorsResponse.data?.let {
                    it.forEach { it.page = page }
                    repository.insertAuthorPosts(it)
                    AuthorsProfileState(posts = it.toMutableList())
                }?:AuthorsProfileState()
            }
            is Result.Failure -> {
                AuthorsProfileState(status = "false", error = "Request failed")
            }
            else ->
                AuthorsProfileState(status = "false", error = "Couldn't retrieve authors")
        }
    }

    private suspend fun retrieveFromDatabase(
        page: Int ,
        authorId: Int
    )  = AuthorsProfileState(posts = repository.getAuthorPostsFromDatabase(authorId ,page))

}