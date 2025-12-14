package com.linkit.company.feature.home.sample

import com.linkit.company.core.common.architecture.state.UiState

data class HomeUiState(
    val isLoading: Boolean,
) : UiState {
    companion object {
        val INITIAL_STATE: HomeUiState = HomeUiState(
            isLoading = false,
        )
    }
}
