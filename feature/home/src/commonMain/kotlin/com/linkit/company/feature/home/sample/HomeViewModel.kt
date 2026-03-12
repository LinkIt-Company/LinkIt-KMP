package com.linkit.company.feature.home.sample

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linkit.company.core.common.architecture.MviContainer
import com.linkit.company.core.common.architecture.MviContext
import com.linkit.company.core.common.architecture.popup.InternalPopupEffectManager
import com.linkit.company.core.common.architecture.popup.PopupEffectManager
import com.linkit.company.core.common.architecture.popup.ToastExposureStatusType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class HomeViewModel(savedStateHandle: SavedStateHandle) : ViewModel(),
    PopupEffectManager by InternalPopupEffectManager() {

    private val container by lazy {
        MviContainer(
            initialState = createInitialState(savedStateHandle),
            onIntent = { handleIntent(it) }
        )
    }

    init {
        intent(HomeIntent.Initialize)
    }

    val uiState = container.uiState
    val sideEffect = container.sideEffect

    private fun createInitialState(savedStateHandle: SavedStateHandle): HomeUiState {
        return HomeUiState.INITIAL_STATE
    }

    private fun intent(intent: HomeIntent) = container.intent(intent)

    private fun MviContext<HomeUiState, HomeSideEffect>.handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.Initialize -> updateLoading()
        }
    }

    private fun updateLoading() {
        viewModelScope.launch {
            updateLoadingStatus(true)
            delay(3000L)
            updateLoadingStatus(false)
            showToastPopup(
                message = "Toast Popup",
                toastExposureType = ToastExposureStatusType.DEFAULT,
            )
        }
    }

    private fun updateLoadingStatus(isLoading: Boolean) {
        container.mviContext.reduce { copy(isLoading = isLoading) }
    }

    override fun onCleared() {
        closePopupEffect()
        container.close()
        super.onCleared()
    }
}