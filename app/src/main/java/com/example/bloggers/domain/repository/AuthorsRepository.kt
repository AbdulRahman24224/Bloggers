package com.example.bloggers.domain.repository


import com.example.bloggers.base.di.AppDatabase
import com.example.bloggers.domain.data.Result
import com.example.bloggers.domain.data.remote.AuthorsApis
import com.example.bloggers.entities.AuthorsListState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.FlowCollector
import kotlinx.coroutines.flow.flow


class AuthorsRepository
 constructor(
    private val service: AuthorsApis ,
    private val database: AppDatabase
 ) {

    fun getAuthors(page: Int, isConnected: Boolean): Flow<AuthorsListState> =
        flow<AuthorsListState> {

            val isDBEmpty = database.authorsDao.retrieveAuthorsCount() == 0
            //choosing source of data based on network state and cached data
            if (isConnected) retrieveFromServer(page)
            // todo think this condition
            else if (!(isConnected.not() and isDBEmpty)) retrieveFromDatabase(page)

        }


    private suspend fun FlowCollector<AuthorsListState>.retrieveFromServer(
        page: Int
    ) {
        // clear old data if New data will be retrieved from server
        if (page == 1) database.authorsDao.clearAll()

        when (val authorsResponse = service.getAuthors(page)) {
            is Result.Success -> {

                authorsResponse.data?.let {
                    it.forEach {it.page = page  }
                    database.authorsDao.insertAllAuthors(it)
                    AuthorsListState(authors = it.toMutableList()  )
                }
            }
            is Result.Failure -> {
                //todo get rid of this "false"
                AuthorsListState(status = "false", error = "Error Retrieving data")
            }
            else ->
                AuthorsListState(status = "false", error = "Connection Error")
        }?.let {
            emit(
                it
            )
        }
    }


    private suspend fun FlowCollector<AuthorsListState>.retrieveFromDatabase(
        page: Int
    ) {
        // todo : should it be a flow inside state flow?
        val authors = database.authorsDao.retrieveAuthorsByPage(page)
            emit( AuthorsListState(authors =  authors ) )

     /*   if (page == 1)
            "This is Offline Data , Please Check your Internet" +
                    " Connection and Refresh to get Latest Data"
       */

    }

}
