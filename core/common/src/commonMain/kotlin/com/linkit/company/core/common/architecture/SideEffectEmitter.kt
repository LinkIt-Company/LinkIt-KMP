package com.linkit.company.core.common.architecture

import com.linkit.company.core.common.architecture.contract.SideEffect
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

internal class SideEffectEmitter<SE : SideEffect> {
    private val _sideEffect: Channel<SE> = Channel(onBufferOverflow = BufferOverflow.DROP_OLDEST)
    val sideEffect: Flow<SE> = _sideEffect.receiveAsFlow()

    fun emit(sideEffect: SE) {
        _sideEffect.trySend(sideEffect)
    }
}