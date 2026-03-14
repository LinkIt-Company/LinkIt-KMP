package com.linkit.company.domain.repository

interface SampleRepository {
    suspend fun getSample(): String
}