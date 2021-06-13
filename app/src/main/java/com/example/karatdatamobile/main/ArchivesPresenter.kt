package com.example.karatdatamobile.main

import android.content.Context
import android.content.SharedPreferences
import com.example.karatdatamobile.Enums.ConnectionMode
import com.example.karatdatamobile.Models.DeviceDataQuery
import com.example.karatdatamobile.Models.DeviceSettings
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
            ConnectionMode.TCP -> tcpInOneString(
                prefs.getOrEmpty(Prefs.IP),
                prefs.getOrEmpty(Prefs.PORT),
                prefs.getOrEmpty(Prefs.SLAVE_ID)
            )
            ConnectionMode.USB -> usbInOneString(
                prefs.getOrEmpty(Prefs.SLAVE_ID),
                "FTDI RS232"
            )
        }
        viewState.updateConnectionSettingsText(result)
    }

    fun loadSaved(deviceSettings: DeviceSettings){
        with (deviceSettings) {
            val result = when (connectionMode){
                ConnectionMode.NONE -> return
                ConnectionMode.TCP -> tcpInOneString(ip, port, address)
                ConnectionMode.USB -> usbInOneString(address, "FTDI RS485")
            }
            viewState.updateConnectionSettingsText(result)
        }
    }

    private fun tcpInOneString(ip: String, port: String, slaveId: String): String = "TCP $ip:$port/$slaveId"

    private fun usbInOneString(slaveId: String, name: String): String = "USB #$slaveId - $name"

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