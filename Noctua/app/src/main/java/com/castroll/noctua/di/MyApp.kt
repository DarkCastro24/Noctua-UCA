package com.castroll.noctua.di

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore
import androidx.lifecycle.ViewModelStoreOwner

class MyApp : Application(), ViewModelStoreOwner {

    private val appViewModelStore = ViewModelStore()

    override val viewModelStore: ViewModelStore
        get() = appViewModelStore

    val viewModelProvider: ViewModelProvider by lazy {
        ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this))
    }

    override fun onCreate() {
        super.onCreate()
    }
}



