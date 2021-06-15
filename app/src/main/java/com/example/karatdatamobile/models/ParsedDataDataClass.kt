package com.example.karatdatamobile.models

import java.io.Serializable

data class ParsedDataDataClass(
    var model: List<List<String>>,
    var serNumber: List<List<String>>,
    var systemDate: List<List<String>>,
    var headers: List<List<String>>,
    var archives: MutableList<List<String>>
): Serializable
