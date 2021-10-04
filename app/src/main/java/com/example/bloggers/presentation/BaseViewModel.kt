package com.example.bloggers.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.entities.BaseState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


class SendSingleItemListener<T>(val item: (item: T) -> Unit) {
    fun sendItem(item: T) = item(item)
}

abstract class BaseViewModel<S : BaseState>(
    initialState: S,
    val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S>
        get() = _state

    private val stateMutex = Mutex()


    // for unit testing
    private val statesList: MutableList<S> = mutableListOf()

    fun <T> Flow<T>.runAndCatch(
        loadingChanged: SendSingleItemListener<Boolean>,
        flowResult: SendSingleItemListener<T>
    ) {
        val flow = this
        viewModelScope.launch(defaultDispatcher) {

            loadingChanged.sendItem(true)

            flow
                .flowOn(defaultDispatcher)
                .catch { e ->
                    setState { this.apply { error = e.message ?: "Exception Error" } }
                    Log.e("Error", e.message ?: "Exception Error")
                }
                .collect { it ->
                    flowResult.sendItem(it)
                    loadingChanged.sendItem(false)
                }

        }
    }

    protected suspend fun setState(reducer: S.() -> S) {

        stateMutex.withLock {
            _state.value = reducer(_state.value)
            statesList.add(_state.value)
        }

    }

    override fun onCleared() {
        super.onCleared()
    }
}
