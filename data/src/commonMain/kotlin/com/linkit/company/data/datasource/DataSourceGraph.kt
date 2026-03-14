package com.linkit.company.data.datasource

import com.linkit.company.data.DataScope
import com.linkit.company.data.datasource.sample.SampleDataSource
import com.linkit.company.data.datasource.sample.SampleDataSourceImpl
import dev.zacsweers.metro.Binds
import dev.zacsweers.metro.ContributesTo

@ContributesTo(DataScope::class)
internal interface DataSourceGraph {

    @Binds
    val SampleDataSourceImpl.bind: SampleDataSource
}