package com.skcodes.auth.data.di

import com.skcodes.auth.data.AuthRepositoryImpl
import com.skcodes.auth.data.EmailPatternValidator
import com.skcodes.auth.domain.AuthRepository
import com.skcodes.auth.domain.PatternValidator
import com.skcodes.auth.domain.UserDataValidator
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val authDataModule = module {

    single <PatternValidator>{
        EmailPatternValidator
    }
    singleOf(::UserDataValidator)
    singleOf(::AuthRepositoryImpl).bind<AuthRepository>()
}