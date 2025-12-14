package com.linkit.company.core.common.architecture.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

interface UiStateManager<S : UiState> {
    val uiState: StateFlow<S>
    val currentState: S
    fun reduce(action: S.() -> S)
}

internal class UiStateManagerImpl<S : UiState>(initialState: S) : UiStateManager<S> {
    private val _uiState: MutableStateFlow<S> = MutableStateFlow(initialState)
    override val uiState: StateFlow<S> = _uiState.asStateFlow()
    override val currentState: S
        get() = _uiState.value

    override fun reduce(action: S.() -> S) {
        _uiState.update { currentState.action() }
    }
}