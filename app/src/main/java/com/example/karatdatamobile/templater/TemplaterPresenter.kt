package com.example.karatdatamobile.templater

import android.content.Context
import com.example.karatdatamobile.Services.TemplateProvider
import moxy.MvpPresenter
import javax.inject.Inject

class TemplaterPresenter @Inject constructor(
    private val context: Context
): MvpPresenter<TemplaterView>() {
    private lateinit var templateProvider: TemplateProvider

    override fun onFirstViewAttach() {
        super.onFirstViewAttach()
       // templateProvider = TemplateProvider(context.resources)
    }

    fun getAdapter(): TemplateFieldsAdapter {
        templateProvider = TemplateProvider(context.resources)
        val names = templateProvider.getUserFieldNames("Базовый шаблон")
        return TemplateFieldsAdapter(names)
    }
}