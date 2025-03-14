package com.skcodes.runntrak

import android.app.Application
import com.skcodes.auth.data.di.authDataModule
import com.skcodes.core.data.di.coreDataModule
import com.skcodes.presentation.di.authViewModelModule
import com.skcodes.run.location.di.locationModule
import com.skcodes.run.presentation.di.runPresentationModule
import com.skcodes.runntrak.di.appModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import timber.log.Timber

class RunNTrakApplication:Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG){
            Timber.plant(Timber.DebugTree())
        }

        startKoin(){
            androidLogger()
            androidContext(this@RunNTrakApplication)
            modules(
                authDataModule,
                authViewModelModule,
                appModule,
                coreDataModule,
                runPresentationModule,
                locationModule
            )
        }

    }
}