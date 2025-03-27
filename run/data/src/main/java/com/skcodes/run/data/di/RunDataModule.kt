package com.skcodes.run.data.di

import com.skcodes.core.domain.run.SyncRunScheduler
import com.skcodes.run.data.CreateRunWorker
import com.skcodes.run.data.DeleteRunWorker
import com.skcodes.run.data.FetchRunWorker
import com.skcodes.run.data.SyncRunWorkScheduler
import org.koin.androidx.workmanager.dsl.workerOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val runDataModule = module {
    workerOf(::FetchRunWorker)
    workerOf(::DeleteRunWorker)
    workerOf(::CreateRunWorker)
    singleOf(::SyncRunWorkScheduler).bind<SyncRunScheduler>()
}