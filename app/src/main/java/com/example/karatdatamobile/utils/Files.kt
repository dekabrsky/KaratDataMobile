package com.example.karatdatamobile.utils

import android.content.Context
import android.content.ContextWrapper
import javax.inject.Inject

object Files {
    fun String.addFileFormat(format: String) = "${this}${format}"
}