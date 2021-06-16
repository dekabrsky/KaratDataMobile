package com.example.karatdatamobile

import android.app.Application
import com.example.karatdatamobile.di.NavigationModule
import com.example.karatdatamobile.di.PresentersModule
import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.NavigatorHolder
import com.github.terrakok.cicerone.Router
import io.reactivex.disposables.CompositeDisposable
import toothpick.Toothpick
import javax.inject.Inject


class App : Application() {

    companion object {
        lateinit var application: App
    }

    @Inject
    lateinit var cicerone: Cicerone<Router>

    private lateinit var disposable: CompositeDisposable

    override fun onCreate() {
        super.onCreate()
        application = this

        disposable = CompositeDisposable()

       Toothpick.openRootScope()
           .installModules(NavigationModule(), PresentersModule())
           .inject(this)
    }

    fun getNavigatorHolder(): NavigatorHolder {
        return cicerone.getNavigatorHolder()
    }

    fun getRouter(): Router {
        return cicerone.router
    }

    fun getDisposable(): CompositeDisposable {
        return disposable
    }
}