package ru.andrewkir.hse_mooc

import android.app.Application
import ru.andrewkir.hse_mooc.di.AppComponent
import ru.andrewkir.hse_mooc.di.AppModule

class App : Application() {
    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()

//        appComponent = DaggerAppComponent
//            .builder()
//            .appModule(AppModule(context = this))
//            .build()
    }

}