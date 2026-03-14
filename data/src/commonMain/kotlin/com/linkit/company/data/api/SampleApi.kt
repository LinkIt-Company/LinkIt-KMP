package com.linkit.company.data.api

import de.jensklingenberg.ktorfit.http.GET

internal interface SampleApi {

    @GET("sample")
    suspend fun getSample(): String
}