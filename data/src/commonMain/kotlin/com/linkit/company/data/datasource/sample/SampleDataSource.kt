package com.linkit.company.data.datasource.sample

interface SampleDataSource {
    suspend fun getSample(): String
}