package com.linkit.company.feature.home.sample

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewModelScope
import com.linkit.company.core.common.architecture.MviContainer
import com.linkit.company.core.common.architecture.MviContext
import com.linkit.company.core.common.architecture.popup.InternalPopupEffectManager
import com.linkit.company.core.common.architecture.popup.PopupEffectManager
import com.linkit.company.core.common.architecture.popup.ToastExposureStatusType
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.AssistedFactory
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactoryKey
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Inject
class HomeViewModel(
    @Assisted val savedStateHandle: SavedStateHandle,
) : ViewModel(),
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

    @AssistedFactory
    @ViewModelAssistedFactoryKey(HomeViewModel::class)
    @ContributesIntoMap(AppScope::class)
    fun interface Factory : ViewModelAssistedFactory {
        override fun create(extras: CreationExtras): HomeViewModel {
            return create(extras.createSavedStateHandle())
        }
        fun create(@Assisted savedStateHandle: SavedStateHandle): HomeViewModel
    }
}
