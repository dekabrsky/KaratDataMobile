package com.example.karatdatamobile.di

import com.github.terrakok.cicerone.Cicerone
import com.github.terrakok.cicerone.Router
import toothpick.config.Module

class NavigationModule: Module() {
    init{
        val instance: Cicerone<Router> = Cicerone.create()
        bind(Cicerone::class.java).toInstance(instance)
        bind(Router::class.java).toInstance(instance.router)
    }
}