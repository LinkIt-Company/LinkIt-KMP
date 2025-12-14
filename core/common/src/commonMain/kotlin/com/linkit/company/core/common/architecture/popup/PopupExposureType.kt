package com.linkit.company.core.common.architecture.popup

sealed interface PopupExposureType {
    val message: String
    val action: (() -> Unit)?

    data class Toast(
        override val message: String,
        override val action: (() -> Unit)?,
        val status: ToastExposureStatusType = ToastExposureStatusType.DEFAULT,
    ) : PopupExposureType

    data class Dialog(
        override val message: String,
        override val action: (() -> Unit)?,
    ) : PopupExposureType
}

enum class ToastExposureStatusType {
    DEFAULT,
    SUCCESS,
    WARNING,
    ERROR,
}
