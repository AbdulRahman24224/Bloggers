package com.example.bloggers.domain.repository

import com.example.bloggers.domain.data.AuthorsApis
import com.example.bloggers.domain.data.Result
import com.example.bloggers.entities.AuthorsListState

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow



class AuthorsRepository
constructor(
    private val service: AuthorsApis

) {

    fun getAuthors(page: Int) : Flow<AuthorsListState> =
        flow<AuthorsListState> {

            when (val authorsResponse = service.getAuthors(page)) {
                is Result.Success -> {
                    emit(AuthorsListState(authors = authorsResponse.data?.toMutableList()?: mutableListOf()))
                }
                is Result.Failure -> {
                    emit(AuthorsListState(status = "false" ,error = "Error Retrieving data"))
                }
                is Result.NetworkError ->  emit(AuthorsListState(status = "false",  error = "Network Error"))
            }

        
        }



}
