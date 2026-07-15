package com.erazero1.tasks

import android.app.Application
import com.erazero1.tasks.di.dataModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class TasksApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger()
            androidContext(this@TasksApplication)
            modules(listOf(dataModule))
        }
    }
}