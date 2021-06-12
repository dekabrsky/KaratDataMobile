package com.example.karatdatamobile.main

import com.example.karatdatamobile.Models.DeviceDataQuery
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject


@InjectViewState
class ArchivesPresenter @Inject constructor(): MvpPresenter<ArchivesView>() {
    private lateinit var query: DeviceDataQuery

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
    }

    fun onDateClick(hasFocus: Boolean = true){
        if (hasFocus) {
            viewState.showCalendarDialog()
        }
    }

    fun onDateChanged(year: Int, month: Int, day: Int){
        val realMonth = month + 1
        val date = "$day/$realMonth/$year"
        viewState.updateDateText(date)
    }
}