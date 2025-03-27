package com.skcodes.core.database.di

import androidx.room.Room
import com.skcodes.core.database.RoomLocalRunDataSource
import com.skcodes.core.database.RunsDatababse
import com.skcodes.core.database.dao.AnalyticsDao
import com.skcodes.core.domain.run.LocalRunDataSource
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dataBaseModule = module{
    single {
        Room.databaseBuilder(androidApplication(),
            RunsDatababse::class.java,
            "runs.db")
            .build()
    }
    single { get<RunsDatababse>().runDao }

    single {get<RunsDatababse>().runPendingSyncDao}

    single {get<RunsDatababse>().analyticsDao}

    singleOf(::RoomLocalRunDataSource).bind<LocalRunDataSource>()
}