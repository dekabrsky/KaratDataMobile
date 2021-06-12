package com.example.karatdatamobile.settingsSetup

import moxy.MvpView
import moxy.viewstate.strategy.OneExecutionStateStrategy
import moxy.viewstate.strategy.StateStrategyType

@StateStrategyType(OneExecutionStateStrategy::class)
interface SettingDeviceView: MvpView {
    fun showUSBSettings()
    fun showTCPSettings()
    fun setIP(ip: String)
    fun setPort(port: String)
    fun setSlaveID(sid: String)
}