package com.example.karatdatamobile.di

import com.example.karatdatamobile.main.ArchivesPresenter
import toothpick.config.Module

class PresentersModule: Module() {
    init
    {
        bind(ArchivesPresenter::class.java)
    }
}