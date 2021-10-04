package com.example.bloggers.presentation.authors.list


// Actions
sealed class AuthorsListIntents {
    data class RetrieveAuthors(val page: Int, val isConnected: Boolean) : AuthorsListIntents()
    data class RefreshScreen (val isConnected: Boolean): AuthorsListIntents()
}
