package com.example.karatdatamobile.settingsSetup

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import java.util.HashMap

@StateStrategyType(OneExecutionStateStrategy::class)
interface SettingDeviceView: MvpView {
    fun showUSBSettings()
    fun showTCPSettings()
    fun setIP(ip: String)
    fun setPort(port: String)
    fun setSlaveID(sid: String)
    fun loadDevices(devices: HashMap<String, UsbDevice>, usbManager: UsbManager)
}