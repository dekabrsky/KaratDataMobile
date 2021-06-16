package com.example.karatdatamobile.main

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface ArchivesView : MvpView {
    fun showSettingsScreen()
    fun showCalendarDialog()
    fun updateDateText(date: String)
    fun updateConnectionSettingsText(text: String)
    fun showDateError()
    fun showArchivesError()
    fun showSettingsError()
}