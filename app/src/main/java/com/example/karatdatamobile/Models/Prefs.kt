package com.example.karatdatamobile.Models

import android.content.SharedPreferences

object Prefs {
    const val DEVICE_SETTINGS = "Device Settings"
    const val IP = "IP"
    const val PORT = "Port"
    const val SLAVE_ID = "SlaveID"
    const val MODE = "Mode"

    fun SharedPreferences.getOrEmpty(key: String): String =
        this.getString(key, "") ?: ""

    fun SharedPreferences.getOrDefault(key: String, default: String): String =
        this.getString(key, default) ?: default
}