package com.example.karatdatamobile.di

import android.app.Activity
import android.content.Context
import toothpick.config.Module

class ContextModule(activity:Activity):Module(){
    init {
        bind(Activity::class.java).toInstance(activity)
        bind(Context::class.java).toInstance(activity.applicationContext)
    }
}