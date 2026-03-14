package com.linkit.company.data

import com.linkit.company.data.core.defaultKtorConfig
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.serialization.json.Json

@ContributesTo(DataScope::class)
interface AndroidDataGraph {
    @Provides
    fun provideHttpClient(json: Json): HttpClient {
        return HttpClient(engineFactory = OkHttp) {
            defaultKtorConfig(json)
        }
    }
}
