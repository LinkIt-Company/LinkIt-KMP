package com.linkit.company.data

import com.linkit.company.data.core.defaultJson
import de.jensklingenberg.ktorfit.Ktorfit
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json

// Hilt - DataModule
@ContributesTo(DataScope::class)
interface DataGraph {

    @Provides
    fun provideBaseUrl(): String = ""  // TODO : BaseUrl 수정

    @Provides
    fun provideJson(): Json = defaultJson()

    @Provides
    fun provideKtorfit(
        httpClient: HttpClient,
        baseUrl: String,
    ): Ktorfit {
        return Ktorfit.Builder()
            .httpClient(httpClient)
            .baseUrl(baseUrl)
            .build()
    }
}