package com.example.karatdatamobile.di

import com.example.karatdatamobile.main.ArchivesPresenter
import com.example.karatdatamobile.reports.ReportsPresenter
import com.example.karatdatamobile.settingsSetup.SettingDevicePresenter
import com.example.karatdatamobile.templater.TemplaterPresenter
import toothpick.config.Module

class PresentersModule: Module() {
    init
    {
        bind(ArchivesPresenter::class.java)
        bind(SettingDevicePresenter::class.java)
    }
}