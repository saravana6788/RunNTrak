package com.skcodes.core.data.di

import android.content.SharedPreferences
import com.skcodes.core.data.auth.EncryptedSessionStorage
import com.skcodes.core.data.networking.HttpClientFactory
import com.skcodes.core.data.run.OfflineRunRepository
import com.skcodes.core.domain.SessionStorage
import com.skcodes.core.domain.run.RunRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val coreDataModule = module {
    single{
        HttpClientFactory(get()).build()
    }

    singleOf(::EncryptedSessionStorage).bind<SessionStorage>()

    singleOf(::OfflineRunRepository).bind<RunRepository>()
}