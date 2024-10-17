package com.skcodes.core.data.di

import com.skcodes.core.data.networking.HttpClientFactory
import io.ktor.client.HttpClient
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreDataModule = module {
    single{
        HttpClientFactory().build()
    }
}