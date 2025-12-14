package com.linkit.company.core.common.architecture.coroutine

import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

interface CoroutineLauncher {
    fun launch(
        scope: CoroutineScope,
        context: CoroutineContext = EmptyCoroutineContext,
        action: suspend CoroutineScope.() -> Unit,
    ): Job
}

internal class CoroutineLauncherImpl(
    private val onException: (Throwable) -> Unit = {},
) : CoroutineLauncher {

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onException(throwable)
    }

    override fun launch(
        scope: CoroutineScope,
        context: CoroutineContext,
        action: suspend CoroutineScope.() -> Unit,
    ): Job {
        return scope.launch(context = context + exceptionHandler) {
            action()
        }
    }
}