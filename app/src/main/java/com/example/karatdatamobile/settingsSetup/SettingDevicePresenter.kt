package com.example.karatdatamobile.settingsSetup

import android.content.Context
import android.content.SharedPreferences
import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import com.example.karatdatamobile.App
import com.example.karatdatamobile.Enums.ConnectionMode
import com.example.karatdatamobile.FlowFragment
import com.example.karatdatamobile.Models.DeviceSettings
import com.example.karatdatamobile.Models.Prefs
import com.example.karatdatamobile.Models.Prefs.getOrDefault
import com.example.karatdatamobile.Models.Prefs.getOrEmpty
import com.example.karatdatamobile.main.ArchivesFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen
import moxy.InjectViewState
import moxy.MvpPresenter
import java.util.*
import javax.inject.Inject

@InjectViewState
class SettingDevicePresenter @Inject constructor(
    private val context: Context
): MvpPresenter<SettingDeviceView>() {
    private lateinit var mode: ConnectionMode
    private lateinit var prefs: SharedPreferences
    private val usbManager = context.getSystemService(Context.USB_SERVICE) as UsbManager

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
        mode = ConnectionMode.TCP
        prefs = context.getSharedPreferences(Prefs.DEVICE_SETTINGS, Context.MODE_PRIVATE)
    }

    fun enableTCP() {
        viewState.showTCPSettings()
        mode = ConnectionMode.TCP
    }

    fun enableUSB() {
        viewState.showUSBSettings()
        mode = ConnectionMode.USB
    }

    fun close() {
        App.application.getRouter().backTo(FragmentScreen { ArchivesFragment() })
    }

    fun loadSaved(){
        prefs = context.getSharedPreferences(Prefs.DEVICE_SETTINGS, Context.MODE_PRIVATE)
        val connectionMode = ConnectionMode.valueOf(prefs.getOrDefault(Prefs.MODE, ConnectionMode.NONE.toString()))
        val ip: String = prefs.getOrEmpty(Prefs.IP)
        val port: String = prefs.getOrEmpty(Prefs.PORT)
        val slaveId: String = prefs.getOrEmpty(Prefs.SLAVE_ID)

        viewState.setIP(ip)
        viewState.setPort(port)
        viewState.setSlaveID(slaveId)

        when (connectionMode) {
            ConnectionMode.TCP, ConnectionMode.NONE -> enableTCP()
            ConnectionMode.USB -> enableUSB()
        }
    }

    fun loadDevices(){
        val devices: HashMap<String, UsbDevice> = usbManager.deviceList
        if (devices.size > 0)
            viewState.loadDevices(devices, usbManager)
    }

    fun save(
        ip: String,
        port: String,
        slaveId: String
    ) {
        prefs.edit()
            .putString(Prefs.MODE, mode.name)
            .putString(Prefs.IP, ip)
            .putString(Prefs.PORT, port)
            .putString(Prefs.SLAVE_ID, slaveId)
            .apply()
        val settings = when (mode){
            ConnectionMode.USB -> DeviceSettings(mode, 19200, usbManager, slaveId)
            ConnectionMode.TCP -> DeviceSettings(mode, port, ip, slaveId)
            else -> return
        }
        App.application.getRouter()
            .replaceScreen(FragmentScreen { FlowFragment.newInstance(settings) })
    }
}