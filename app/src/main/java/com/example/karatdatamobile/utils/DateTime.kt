package com.example.karatdatamobile.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object DateTime {
    @SuppressLint("SimpleDateFormat")
    fun currentDateTimeString(): String {
        val currentTime = Calendar.getInstance().time
        val dateFormat = SimpleDateFormat("dd.MM.yyyy-HH.mm")
        return dateFormat.format(currentTime)
    }

    @SuppressLint("SimpleDateFormat")
    fun Date.toSimpleDate(): String{
        val dateFormat = SimpleDateFormat("dd.MM.yyyy")
        return dateFormat.format(this)
    }

    @SuppressLint("SimpleDateFormat")
    fun Date.toSimpleDateTime(): String{
        val dateFormat = SimpleDateFormat("dd.MM.yyyy HH:mm")
        return dateFormat.format(this)
    }
}