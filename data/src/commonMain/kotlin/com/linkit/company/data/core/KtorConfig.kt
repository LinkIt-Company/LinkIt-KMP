package com.linkit.company.data.core

import io.ktor.client.HttpClientConfig
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

fun HttpClientConfig<*>.defaultKtorConfig(
    ktorJsonSettings: Json,
) {
    install(ContentNegotiation) {
        json(ktorJsonSettings)
    }
}

fun defaultJson(): Json {
    return Json {
        encodeDefaults = true
        isLenient = true
        prettyPrint = false
        ignoreUnknownKeys = true
    }
}