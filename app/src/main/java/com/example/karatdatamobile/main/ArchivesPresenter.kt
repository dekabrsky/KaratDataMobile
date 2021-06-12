package com.example.karatdatamobile.main

import android.content.Context
import android.content.SharedPreferences
import com.example.karatdatamobile.Enums.ConnectionMode
import com.example.karatdatamobile.Models.DeviceDataQuery
import com.example.karatdatamobile.Models.Prefs
import com.example.karatdatamobile.Models.Prefs.getOrDefault
import com.example.karatdatamobile.Models.Prefs.getOrEmpty
import moxy.InjectViewState
import moxy.MvpPresenter
import javax.inject.Inject


@InjectViewState
class ArchivesPresenter @Inject constructor(
    private val context: Context
): MvpPresenter<ArchivesView>() {
    private lateinit var query: DeviceDataQuery
    private lateinit var prefs: SharedPreferences // todo: инжектить сами преференсы

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        prefs = context.getSharedPreferences(Prefs.DEVICE_SETTINGS, Context.MODE_PRIVATE)
    }

    fun loadSaved() {
        prefs = context.getSharedPreferences(Prefs.DEVICE_SETTINGS, Context.MODE_PRIVATE)
        val result = when (ConnectionMode.valueOf(prefs.getOrDefault(Prefs.MODE, "NONE"))){
            ConnectionMode.NONE -> return
            ConnectionMode.TCP -> tcpInOneString()
            ConnectionMode.USB -> usbInOneString()
        }
        viewState.updateConnectionSettingsText(result)
    }

    private fun tcpInOneString(): String {
        val ip = prefs.getOrEmpty(Prefs.IP)
        val port = prefs.getOrEmpty(Prefs.PORT)
        val slaveId = prefs.getOrEmpty(Prefs.SLAVE_ID)
        return "TCP $ip:$port/$slaveId"
    }

    private fun usbInOneString(): String {
        val slaveId = prefs.getOrEmpty(Prefs.SLAVE_ID)
        val name = "FTDI RS232" // todo тут будет имя девайса
        return "USB #$slaveId - $name"
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