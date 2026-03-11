package com.linkit.company.core.common.architecture

import com.linkit.company.core.common.architecture.contract.Intent
import com.linkit.company.core.common.architecture.contract.SideEffect
import com.linkit.company.core.common.architecture.contract.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

class MviContainer<I : Intent, SE : SideEffect, S : UiState>(
    initialState: S,
    private val onIntent: MviContext<S, SE>.(I) -> Unit
) {
    private val uiStateHolder: UiStateHolder<S> = UiStateHolder(initialState)
    private val sideEffectEmitter: SideEffectEmitter<SE> = SideEffectEmitter()

    val uiState: StateFlow<S> = uiStateHolder.state
    val sideEffect: Flow<SE> = sideEffectEmitter.sideEffect

    val mviContext = object : MviContext<S, SE> {
        override val currentState: S
            get() = uiState.value

        override fun reduce(action: S.() -> S) {
            uiStateHolder.reduce(action)
        }

        override fun postSideEffect(sideEffect: SE) {
            sideEffectEmitter.emit(sideEffect)
        }
    }

    fun intent(intent: I) {
        onIntent(mviContext, intent)
    }
}

interface MviContext<S : UiState, SE : SideEffect> {
    val currentState: S
    fun reduce(action: S.() -> S)
    fun postSideEffect(sideEffect: SE)
}