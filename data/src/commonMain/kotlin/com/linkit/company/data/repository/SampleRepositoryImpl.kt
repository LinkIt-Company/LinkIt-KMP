package com.linkit.company.data.repository

import com.linkit.company.data.datasource.sample.SampleDataSource
import com.linkit.company.domain.repository.SampleRepository
import dev.zacsweers.metro.Inject

@Inject
class SampleRepositoryImpl(
    private val sampleDataSource: SampleDataSource,
) : SampleRepository {
    override suspend fun getSample(): String {
        return sampleDataSource.getSample()
    }
}