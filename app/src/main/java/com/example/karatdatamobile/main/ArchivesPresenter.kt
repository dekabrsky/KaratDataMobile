package com.example.karatdatamobile.main

import android.content.Context
import android.content.SharedPreferences
import android.hardware.usb.UsbManager
import com.example.karatdatamobile.App
import com.example.karatdatamobile.enums.ArchiveType
import com.example.karatdatamobile.enums.ConnectionMode
import com.example.karatdatamobile.models.DeviceDataQuery
import com.example.karatdatamobile.models.DeviceSettings
import com.example.karatdatamobile.utils.Prefs
import com.example.karatdatamobile.utils.Prefs.getOrDefault
import com.example.karatdatamobile.utils.Prefs.getOrEmpty
import com.example.karatdatamobile.terminal.TerminalFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen
import moxy.InjectViewState
import moxy.MvpPresenter
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList


@InjectViewState
class ArchivesPresenter @Inject constructor(
    private val context: Context
): MvpPresenter<ArchivesView>() {
    private lateinit var query: DeviceDataQuery
    private lateinit var prefs: SharedPreferences // todo: инжектить сами преференсы
    private var date: Date? = null

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        prefs = context.getSharedPreferences(Prefs.DEVICE_SETTINGS, Context.MODE_PRIVATE)
    }

    fun loadSaved() {
        val settings = getSettingsFromPrefs()
        if (settings != null)
            loadSaved(settings)
    }

    fun getSettingsFromPrefs(): DeviceSettings? {
        prefs = context.getSharedPreferences(Prefs.DEVICE_SETTINGS, Context.MODE_PRIVATE)
        val mode = ConnectionMode.valueOf(prefs.getOrDefault(Prefs.MODE, "NONE"))
        return when (mode){
            ConnectionMode.NONE -> return null
            ConnectionMode.TCP -> DeviceSettings(
                mode,
                prefs.getOrEmpty(Prefs.PORT),
                prefs.getOrEmpty(Prefs.IP),
                prefs.getOrEmpty(Prefs.SLAVE_ID)
            )
            ConnectionMode.USB -> DeviceSettings(
                mode,
                19200,
                context.getSystemService(Context.USB_SERVICE) as UsbManager,
                prefs.getOrEmpty(Prefs.SLAVE_ID)
            )
        }
    }

    fun loadSaved(deviceSettings: DeviceSettings){
        with (deviceSettings) {
            val result = when (connectionMode){
                ConnectionMode.TCP -> tcpInOneString(ip, port, address)
                ConnectionMode.USB -> usbInOneString(address, "FTDI RS485")
                else -> return
            }
            viewState.updateConnectionSettingsText(result)
        }
    }

    private fun tcpInOneString(ip: String, port: String, slaveId: String): String = "TCP $ip:$port/$slaveId"

    private fun usbInOneString(slaveId: String, name: String): String = "USB #$slaveId - $name"

    fun onDateClick(){
        viewState.showCalendarDialog()
    }

    fun onDateChanged(year: Int, month: Int, day: Int){
        val realMonth = month + 1
        val dateString = "$day/$realMonth/$year"
        viewState.updateDateText(dateString)
        date = Date(year-2000, month, day)
    }

    fun proceed(
        deviceType: String,
        archiveTypes: ArrayList<ArchiveType>,
        settings: DeviceSettings?,
        filename: String
    ) {
        if (date == null){
            viewState.showDateError()
            return
        }
        if (archiveTypes.size == 0){
            viewState.showArchivesError()
            return
        }
        if (settings == null) {
            viewState.showSettingsError()
            return
        }
        query = DeviceDataQuery(deviceType, date, archiveTypes)
        App.application.getRouter()
            .navigateTo(FragmentScreen {
                TerminalFragment.newInstance(query, settings, filename)
            }
        )
    }
}