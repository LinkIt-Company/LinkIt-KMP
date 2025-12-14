package com.linkit.company.core.common.architecture

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linkit.company.core.common.architecture.coroutine.CoroutineLauncherImpl
import com.linkit.company.core.common.architecture.intent.Intent
import com.linkit.company.core.common.architecture.sideeffect.SideEffect
import com.linkit.company.core.common.architecture.sideeffect.SideEffectManagerImpl
import com.linkit.company.core.common.architecture.state.UiState
import com.linkit.company.core.common.architecture.state.UiStateManagerImpl
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

abstract class MVIViewModel<I : Intent, SE : SideEffect, S : UiState>(
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val uiStateManager by lazy { UiStateManagerImpl<S>(createInitialState(savedStateHandle)) }
    private val sideEffectManager by lazy { SideEffectManagerImpl<SE>() }
    private val coroutineLauncher by lazy {
        CoroutineLauncherImpl(
            onException = { handleClientException(it) }
        )
    }

    val uiState: StateFlow<S> = uiStateManager.uiState
    val sideEffect: Flow<SE> = sideEffectManager.sideEffect

    protected val currentState: S
        get() = uiStateManager.currentState

    protected abstract fun createInitialState(savedStateHandle: SavedStateHandle): S
    protected abstract fun handleIntent(intent: I)

    protected suspend fun postSideEffect(sideEffect: SE) {
        sideEffectManager.postSideEffect(sideEffect)
    }

    protected fun reduce(action: S.() -> S) = uiStateManager.reduce(action)

    protected fun launch(
        context: CoroutineContext = EmptyCoroutineContext,
        action: suspend CoroutineScope.() -> Unit,
    ): Job = coroutineLauncher.launch(viewModelScope, context, action)

    fun intent(intent: I): Job = launch {
        handleIntent(intent)
    }

    private fun handleClientException(throwable: Throwable) {
//        Log.d("EX", "handleClientException: $throwable")
    }

    override fun onCleared() {
        super.onCleared()
        sideEffectManager.closeSideEffect()
    }
}