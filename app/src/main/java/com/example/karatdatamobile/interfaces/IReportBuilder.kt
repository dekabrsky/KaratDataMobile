package com.example.karatdatamobile.interfaces

import java.io.IOException
import java.util.*

interface IReportBuilder {
    @Throws(IOException::class)
    fun constructCsvReport(
        fileName: String,
        parsedDataBlocks: HashMap<String, List<List<String>>>
    )

    @Throws(IOException::class)
    fun constructXlsxReport(
        fileName: String,
        templateName: String,
        userData: HashMap<String, String>,
        parsedDataBlocks: HashMap<String, List<List<String>>>
    )
}