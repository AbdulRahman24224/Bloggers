package com.example.bloggers.presentation.authors.list

interface ViewState

// Actions
sealed class AuthorsListIntents {
    data class RetrieveAuthors(val page: Int , val isConnected: Boolean ) : AuthorsListIntents()
}
