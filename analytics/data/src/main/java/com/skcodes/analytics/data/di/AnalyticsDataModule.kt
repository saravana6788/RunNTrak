package com.skcodes.analytics.data.di

import com.skcodes.analytics.data.RoomAnalyticsRepository
import com.skcodes.analytics.domain.AnalyticsRepository
import com.skcodes.core.database.RunsDatababse
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val analyticsDataModule = module{

    singleOf(::RoomAnalyticsRepository).bind<AnalyticsRepository>()

    single{
        get<RunsDatababse>().analyticsDao
    }

}