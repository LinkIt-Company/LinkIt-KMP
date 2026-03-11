package com.linkit.company.core.common.architecture

import com.linkit.company.core.common.architecture.contract.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

internal class UiStateHolder<S : UiState>(initialState: S) {
    private val _state: MutableStateFlow<S> = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    fun reduce(action: S.() -> S) {
        _state.update { it.action() }
    }
}
