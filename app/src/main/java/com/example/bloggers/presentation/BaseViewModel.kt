
package com.example.bloggers.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.bloggers.entities.BaseState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock


class SendSingleItemListener<T>(val item: (item: T) -> Unit) {
    fun sendItem(item: T) = item(item)
}

abstract class BaseViewModel<S : BaseState>(
    initialState: S  ,
    val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    private val stateMutex = Mutex()

    val  state: S
    get() = _state.value

    // todo :  expose state as StateFlow
    val liveData: LiveData<S>
        get() = _state.asLiveData()

     private val  statesList: MutableList<S> = mutableListOf()

    fun <T> Flow<T>.runAndCatch( loadingChanged: SendSingleItemListener<Boolean> , flowResult: SendSingleItemListener<T> ) {
        val flow = this
        viewModelScope.launch(defaultDispatcher) {

            loadingChanged.sendItem(true)

            flow
                .flowOn(defaultDispatcher)
                .catch { e -> setState {  this.apply { error = e.message?:"Exception Error" }} }
                .collect { it ->
                    flowResult.sendItem(it)
                    loadingChanged.sendItem(false)

                }

        }
    }

    protected suspend fun setState(reducer: S.() -> S) {

            stateMutex.withLock {
                _state.value = reducer(_state.value)
            //    Log.v("newState", state.value.toString())
                statesList.add(_state.value)
            }

    }

    override fun onCleared() {
        super.onCleared()
    }
}
