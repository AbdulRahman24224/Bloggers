package com.example.bloggers.presentation.authors.details

import androidx.lifecycle.viewModelScope
import com.example.bloggers.base.di.DefaultDispatcher
import com.example.bloggers.domain.repository.AuthorsRepository
import com.example.bloggers.entities.AuthorsProfileState
import com.example.bloggers.presentation.BaseViewModel
import com.example.bloggers.presentation.SendSingleItemListener
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
    private val authorsRepository: AuthorsRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher

) : BaseViewModel<AuthorsProfileState>(
    AuthorsProfileState(),
    dispatcher
) {

    private val pendingActions =
        MutableSharedFlow<AuthorsProfileIntents>()

    init {
        viewModelScope.launch(defaultDispatcher) {
            handleIntents()
        }
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
               val author =  authorsRepository.getAuthorById(authorId).firstOrNull()
                author?.apply { setState { copy(author = author) }  }
                getAuthorPosts(authorId , 1 ,isConnected )
            }
        }
    
    private fun getAuthorPosts(authorId: Int , page: Int , isConnected : Boolean) {
        authorsRepository.getAuthorPosts( authorId  , page  , isConnected )
            .runAndCatch(SendSingleItemListener { b ->
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


                })
    }


}