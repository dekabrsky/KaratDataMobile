package com.example.karatdatamobile.templater

import android.content.Context
import android.content.ContextWrapper
import com.example.karatdatamobile.interfaces.IReportBuilder
import com.example.karatdatamobile.interfaces.ITemplateProvider
import com.example.karatdatamobile.models.ParsedData
import com.example.karatdatamobile.services.ReportBuilder
import com.example.karatdatamobile.services.TemplateProvider
import com.example.karatdatamobile.utils.Fields
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

    fun writeXLS(typeToValue: HashMap<String, String>, dataFromDevice: ParsedData) {
        val res = context.resources
        val cw = ContextWrapper(context)
        val directory = cw.getExternalFilesDir("Karat")
        val templateProvider: ITemplateProvider = TemplateProvider(res)
        val reportProvider: IReportBuilder =
            ReportBuilder(directory.toString(), directory.toString(), templateProvider)
        val parsedData = java.util.HashMap<String, List<List<String>>>()
        parsedData[Fields.DATA] = dataFromDevice.archives
        parsedData[Fields.MODEL] = dataFromDevice.model
        parsedData[Fields.HEADER] = dataFromDevice.headers
        parsedData[Fields.DATE_TIME] = dataFromDevice.systemDate
        parsedData[Fields.SERIAL_NUMBER] = dataFromDevice.serNumber

        reportProvider.constructXlsxReport("test.xlsx", "test", typeToValue, parsedData)
    }

}