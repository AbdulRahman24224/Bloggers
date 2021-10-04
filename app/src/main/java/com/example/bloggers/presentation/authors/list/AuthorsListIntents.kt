package com.example.bloggers.presentation.authors.list


// Actions
sealed class AuthorsListIntents {
    data class RetrieveAuthors(val page: Int, val isConnected: Boolean) : AuthorsListIntents()
    object RefreshScreen: AuthorsListIntents()
}
