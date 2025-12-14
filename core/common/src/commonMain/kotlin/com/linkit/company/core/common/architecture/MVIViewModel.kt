package com.linkit.company.core.common.architecture

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class MVIViewModel<I : Intent, SE : SideEffect, S : UiState>(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val initialState: S by lazy { createInitialState(savedStateHandle) }

    private val _uiState: MutableStateFlow<S> = MutableStateFlow(initialState)
    val uiState: StateFlow<S> = _uiState.asStateFlow()

    private val _sideEffect: Channel<SE> = Channel(onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val sideEffect: Flow<SE> = _sideEffect.receiveAsFlow()

    protected val currentState: S
        get() = _uiState.value

    protected val coroutineExceptionHandler: CoroutineExceptionHandler =
        CoroutineExceptionHandler { _, throwable ->
            handleClientException(throwable)
        }

    protected abstract fun createInitialState(savedStateHandle: SavedStateHandle): S
    protected abstract fun handleIntent(intent: I)

    protected suspend fun postSideEffect(sideEffect: SE) {
        _sideEffect.send(sideEffect)
    }

    protected fun reduce(action: S.() -> S) {
        _uiState.update { currentState.action() }
    }

    protected inline fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        crossinline action: suspend CoroutineScope.() -> Unit,
    ): Job = viewModelScope.launch(context = context + coroutineExceptionHandler) {
        action()
    }

    fun intent(intent: I): Job = launch {
        handleIntent(intent)
    }

    private fun handleClientException(throwable: Throwable) {
//        Log.d("EX", "handleClientException: $throwable")
    }

    override fun onCleared() {
        super.onCleared()
        _sideEffect.close()
    }
}
