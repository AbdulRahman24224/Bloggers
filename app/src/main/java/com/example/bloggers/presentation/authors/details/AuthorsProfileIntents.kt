package com.example.bloggers.presentation.authors.details

// Actions
sealed class AuthorsProfileIntents {
    data class RetrieveAuthorData(val authorId: Int , val isConnected: Boolean ) : AuthorsProfileIntents()
    data class RetrieveAuthorPosts(val page: Int , val isConnected: Boolean ) : AuthorsProfileIntents()
}