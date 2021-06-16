package com.example.karatdatamobile.utils

import android.view.View

object Views {
    fun View.show() { this.visibility = View.VISIBLE }
    fun View.hide() { this.visibility = View.GONE }
    fun View.softHide() { this.visibility = View.INVISIBLE }
}