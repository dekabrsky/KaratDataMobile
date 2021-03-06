package com.example.karatdatamobile.models

import java.io.Serializable

class ParsedData(): Serializable{
    lateinit var model: List<List<String>>
    lateinit var serNumber: List<List<String>>
    lateinit var systemDate: List<List<String>>
    lateinit var headers: List<List<String>>
    var archives: MutableList<List<String>> = mutableListOf()

    constructor(parsedDataDataClass: ParsedDataDataClass?) : this() {
        if (parsedDataDataClass != null) {
            model = parsedDataDataClass.model
            serNumber = parsedDataDataClass.serNumber
            systemDate = parsedDataDataClass.systemDate
            headers = parsedDataDataClass.headers
            archives = parsedDataDataClass.archives
        }

    }

    fun setModel(modelString: String){
        model = modelString.cellWrap()
    }

    fun setSerNumber(serNumberString: String){
        serNumber = serNumberString.cellWrap()
    }

    fun setSystemDate(date: String){
        systemDate = date.cellWrap()
    }

    @JvmName("setHeaders1")
    fun setHeaders(headersInput: List<String>){
        headers = listOf(headersInput)
    }

    @JvmName("setArchives1")
    fun setArchives(archivesInput: List<List<String>>){
        archives = archivesInput.toMutableList()
    }

    fun addArchiveRows(rows: List<List<String>>){
        archives.addAll(rows)
    }

    fun addArchiveHeader(header: String){
        archives.add(listOf(header))
    }

    private fun String.cellWrap(): List<List<String>> = listOf(listOf(this))
}
