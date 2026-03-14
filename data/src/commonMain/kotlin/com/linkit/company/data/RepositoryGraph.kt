package com.linkit.company.data

import com.linkit.company.data.repository.SampleRepositoryImpl
import com.linkit.company.domain.repository.SampleRepository
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(DataScope::class)
internal interface RepositoryGraph {

    @Binds
    val SampleRepositoryImpl.bind: SampleRepository
}