package com.skcodes.presentation.di

import com.skcodes.presentation.register.RegisterViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val authViewModelModule =  module {
    viewModelOf(::RegisterViewModel)
}