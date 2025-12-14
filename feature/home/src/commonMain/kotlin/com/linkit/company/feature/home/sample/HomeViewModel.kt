package com.linkit.company.feature.home.sample

import androidx.lifecycle.SavedStateHandle
import com.linkit.company.core.common.architecture.MVIViewModel
import kotlinx.coroutines.delay

class HomeViewModel(
    savedStateHandle: SavedStateHandle,
) : MVIViewModel<HomeIntent, HomeSideEffect, HomeUiState>(
    savedStateHandle = savedStateHandle
) {
    init {
        updateLoading()
    }

    override fun createInitialState(savedStateHandle: SavedStateHandle): HomeUiState {
        return HomeUiState.INITIAL_STATE
    }

    override fun handleIntent(intent: HomeIntent) {
        when (intent) {
            else -> {}
        }
    }

    private fun updateLoading() = launch {
        reduce { copy(isLoading = true) }
        delay(3000L)
        reduce { copy(isLoading = false) }
    }
}