package com.linkit.company.core.common.architecture.sideeffect

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

interface SideEffectManager<SE : SideEffect> {
    val sideEffect: Flow<SE>
    suspend fun postSideEffect(sideEffect: SE)
    fun closeSideEffect()
}

internal class SideEffectManagerImpl<SE : SideEffect> : SideEffectManager<SE> {
    private val _sideEffect: Channel<SE> = Channel(
        capacity = Channel.BUFFERED,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )

    override val sideEffect: Flow<SE> = _sideEffect.receiveAsFlow()
    override suspend fun postSideEffect(sideEffect: SE) {
        _sideEffect.send(sideEffect)
    }

    override fun closeSideEffect() {
        _sideEffect.close()
    }
}