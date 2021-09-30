package com.example.bloggers.presentation.authors.list

interface ViewState
// Actions
sealed class AuthorsListIntents {
    data class RetrieveAuthors(val page :Int) : AuthorsListIntents()
}


/*
// Actions
sealed class ArticleDetailsIntents {
    data class InitializeCurrentArticle(val article :Article) : ArticleDetailsIntents()
}
data class ArticleDetails (val isIdle :Boolean = true , val article:Article?=null ,
                           override var error: String ="",
                           override var isLoading: Boolean = false) : BaseState()*/
