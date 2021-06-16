package com.example.karatdatamobile.utils

import java.util.*

object Lists {
    fun ArrayList<String>.addToBegin(new: String) {
        this.reverse()
        this.add(new)
        this.reverse()
    }
}