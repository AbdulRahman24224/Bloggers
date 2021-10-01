package com.example.bloggers.presentation.authors.list

import androidx.lifecycle.viewModelScope
import com.example.bloggers.base.di.DefaultDispatcher
import com.example.bloggers.domain.repository.AuthorsRepository
import com.example.bloggers.entities.AuthorsListState
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
class AuthorsStateViewModel
@Inject constructor(
    private val authorsRepository: AuthorsRepository,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher

) : BaseViewModel<AuthorsListState>(
    AuthorsListState(),
    dispatcher
) {

    private val pendingActions =
        MutableSharedFlow<AuthorsListIntents>()

    init {
        viewModelScope.launch(defaultDispatcher) {
            handleIntents()
        }
    }


    private suspend fun handleIntents() {

        pendingActions
            .collect { action ->
            when (action) {
                is AuthorsListIntents.RetrieveAuthors -> getAuthors(action.page , action.isConnected)
            }
        }
    }

    fun submitAction(action: AuthorsListIntents) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }

    /*    private fun  setCurrentArticle(article : Article){
            viewModelScope.launch {
                setState { copy(article = article) }
                getArticlefor(article.url)
            }
        }*/
    private fun getAuthors(page: Int , isConnected : Boolean) {
        authorsRepository.getAuthors(page  , isConnected )
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
                            copy( hasMoreData = newState.authors.isNotEmpty()
                             ,   authors =
                            state.authors.let {
                                it.addAll(newState.authors)
                                it
                            })
                        }
                    }


                })
    }


}