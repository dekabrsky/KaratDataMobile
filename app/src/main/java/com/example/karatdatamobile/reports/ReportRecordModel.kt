package com.example.karatdatamobile.reports

import java.io.File
import java.util.*

data class ReportRecordModel(
    val fileName: String,
    val lastModified: Date,
    val file: File
)
