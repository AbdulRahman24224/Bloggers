package com.example.bloggers.presentation.authors.details

import androidx.lifecycle.viewModelScope
import com.example.bloggers.base.di.DefaultDispatcher
import com.example.bloggers.presentation.BaseViewModel
import com.example.bloggers.presentation.SendSingleItemListener
import com.example.domain.usecases.authors.RetrieveAuthorPostsUseCase
import com.example.domain.usecases.authors.RetrieveSingleAuthorUseCase
import com.example.domain.usecases.authors.states.AuthorsProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AuthorsProfileViewModel
@Inject constructor(
    private val retrieveAuthors : RetrieveAuthorPostsUseCase ,
    private val retrieveSingleAuthor : RetrieveSingleAuthorUseCase ,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher

) : BaseViewModel<AuthorsProfileState>(
    AuthorsProfileState(),
    dispatcher
) {

    private val pendingActions = MutableSharedFlow<AuthorsProfileIntents>()

    init {
        viewModelScope.launch(defaultDispatcher) { handleIntents() }
    }

    private suspend fun handleIntents() {

        pendingActions
            .collect { action ->
            when (action) {
                is AuthorsProfileIntents.RetrieveAuthorData -> getCurrentAuthorData(action.authorId , action.isConnected)
                is AuthorsProfileIntents.RetrieveAuthorPosts -> getAuthorPosts(state.author.id , action.page , action.isConnected)
            }
        }
    }

    fun submitAction(action: AuthorsProfileIntents) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }

        private fun  getCurrentAuthorData(authorId: Int , isConnected : Boolean){
            viewModelScope.launch {

                    retrieveSingleAuthor(authorId)
                        .runAndCatch(
                            SendSingleItemListener {  } ,
                            SendSingleItemListener {
                              it?.apply { viewModelScope.launch {setState { copy(author = it) } } }
                            }
                        )

                getAuthorPosts(authorId , 1 ,isConnected )
            }
        }
    
    private fun getAuthorPosts(authorId: Int , page: Int , isConnected : Boolean) {
        retrieveAuthors(authorId  , page  , isConnected )
            .runAndCatch(

                SendSingleItemListener { b ->
                viewModelScope.launch {
                    setState {
                        copy(
                            isLoading = b
                        )
                    }
                }
            },
                SendSingleItemListener
                { newState ->
                    viewModelScope.launch {
                        setState {
                            copy( hasMoreData = newState.posts.isNotEmpty()
                             ,   posts = 
                            state.posts.let {
                                it.addAll(newState.posts)
                                it
                            })
                        }
                    }
                }
            )
    }


}