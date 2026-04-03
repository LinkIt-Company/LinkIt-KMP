package com.linkit.company.feature.home.sample

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.linkit.company.core.common.architecture.MviContainer
import com.linkit.company.core.common.architecture.MviContext
import com.linkit.company.core.common.architecture.popup.InternalPopupEffectManager
import com.linkit.company.core.common.architecture.popup.PopupEffectManager
import com.linkit.company.core.common.architecture.popup.ToastExposureStatusType
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import dev.zacsweers.metrox.viewmodel.CreationParams
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@ContributesIntoMap(AppScope::class)
@ViewModelKey(HomeViewModel::class)
@Inject
class HomeViewModel(
    @Assisted val creationParams: CreationParams,
) : ViewModel(),
    PopupEffectManager by InternalPopupEffectManager() {

    private val savedStateHandle = creationParams.savedStateHandle

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
        creationParams
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
