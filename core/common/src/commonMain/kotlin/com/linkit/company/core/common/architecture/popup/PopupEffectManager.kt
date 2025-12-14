package com.linkit.company.core.common.architecture.popup

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

interface PopupEffectManager {
    val popupEffect: Flow<PopupExposureType>
    suspend fun showToastPopup(
        message: String,
        toastExposureType: ToastExposureStatusType,
        action: (() -> Unit)? = null
    )

    suspend fun showDialogPopup(
        message: String,
        action: (() -> Unit)? = null,
    )

    fun closePopupEffect()
}

class InternalPopupEffectManager : PopupEffectManager {
    private val _popupEffect: Channel<PopupExposureType> = Channel(
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
    )
    override val popupEffect: Flow<PopupExposureType> = _popupEffect.receiveAsFlow()

    override suspend fun showToastPopup(
        message: String,
        toastExposureType: ToastExposureStatusType,
        action: (() -> Unit)?,
    ) {
        _popupEffect.send(
            PopupExposureType.Toast(
                message = message,
                action = action,
                status = toastExposureType,
            )
        )
    }

    override suspend fun showDialogPopup(message: String, action: (() -> Unit)?) {
        _popupEffect.send(
            PopupExposureType.Dialog(
                message = message,
                action = action,
            )
        )
    }

    override fun closePopupEffect() {
        _popupEffect.close()
    }
}