package com.linkit.company.feature.home.sample

import com.linkit.company.core.common.architecture.contract.Intent

sealed interface HomeIntent : Intent {
    data object Initialize : HomeIntent
}