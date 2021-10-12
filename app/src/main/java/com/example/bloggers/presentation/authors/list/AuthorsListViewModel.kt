package com.example.bloggers.presentation.authors.list

import androidx.lifecycle.viewModelScope
import com.example.bloggers.base.di.DefaultDispatcher
import com.example.bloggers.base.utils.coroutines.throttleFirst
import com.example.bloggers.presentation.BaseViewModel
import com.example.bloggers.presentation.SendSingleItemListener
import com.example.domain.usecases.authors.RetrieveAuthorsUseCase
import com.example.domain.usecases.authors.states.AuthorsListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject




@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class AuthorsListViewModel
@Inject constructor(
    private val retrieveAuthorsUseCase: RetrieveAuthorsUseCase,
    @DefaultDispatcher private val dispatcher: CoroutineDispatcher

) : BaseViewModel<AuthorsListState>(
    AuthorsListState(),
    dispatcher
) {

    private val pendingActions =
        MutableSharedFlow<AuthorsListIntents>()

    init {
        viewModelScope.launch(defaultDispatcher) { handleIntents() }
    }

    private suspend fun handleIntents() {

        pendingActions
            .throttleFirst(500)
            .collect { action ->
                when (action) {
                    is AuthorsListIntents.RetrieveAuthors ->{
                        state.value.apply {
                            if (isLoading.not() && hasMoreData) {
                                getAuthors(
                                    page++,
                                    action.isConnected
                                )
                            }
                        }

                    }
                    is AuthorsListIntents.RefreshScreen -> refreshState(action.isConnected)
                }
            }
    }

    private fun refreshState(isConnected: Boolean) {
        if (isConnected) viewModelScope.launch {
            setState {
                AuthorsListState(
                    authors = mutableListOf(),
                    page = 1
                )
            }
        }
        else viewModelScope.launch { setState { AuthorsListState(error = "couldn't refresh") } }
    }

    fun submitAction(action: AuthorsListIntents) {
        viewModelScope.launch {
            pendingActions.emit(action)
        }
    }

    private fun getAuthors(page: Int, isConnected: Boolean) {
        retrieveAuthorsUseCase(page, isConnected)
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
                            val hasMoreData = newState.authors.isNotEmpty()
                            val showingCached =
                                isConnected.not() and (page == 1) and newState.authors.isNotEmpty()

                            // todo  separate messsage from errors in basestate
                            newState.error = if (hasMoreData.not()) "no more data"
                            else if (showingCached) "showing cached"
                            else newState.error

                            copy(
                                hasMoreData = hasMoreData,
                                authors =
                                state.value.authors.let {
                                    it.addAll(newState.authors)
                                    it
                                },
                                error = if (state.value.error == newState.error) "" else newState.error
                            )
                        }
                    }
                }
            )
    }


}