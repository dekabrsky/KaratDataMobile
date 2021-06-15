package com.example.karatdatamobile.templater

import android.content.Context
import android.content.ContextWrapper
import com.example.karatdatamobile.App
import com.example.karatdatamobile.FlowFragment
import com.example.karatdatamobile.interfaces.IReportBuilder
import com.example.karatdatamobile.interfaces.ITemplateProvider
import com.example.karatdatamobile.models.ParsedData
import com.example.karatdatamobile.services.ReportBuilder
import com.example.karatdatamobile.services.TemplateProvider
import com.example.karatdatamobile.utils.Extensions
import com.example.karatdatamobile.utils.Fields
import com.example.karatdatamobile.utils.Files.addFileFormat
import com.github.terrakok.cicerone.androidx.FragmentScreen
import moxy.MvpPresenter
import javax.inject.Inject

class TemplaterPresenter @Inject constructor(
    private val context: Context
): MvpPresenter<TemplaterView>() {
    private lateinit var templateProvider: ITemplateProvider
    private lateinit var reportProvider: IReportBuilder
    private lateinit var typeToValue: HashMap<String, String>
    private lateinit var parsedData: HashMap<String, List<List<String>>>

    fun getAdapter(): TemplateFieldsAdapter {
        templateProvider = TemplateProvider(context.resources)
        val names = templateProvider.getUserFieldNames("Базовый шаблон")
        return TemplateFieldsAdapter(names)
    }

    fun prepareData(typeToValue: HashMap<String, String>, dataFromDevice: ParsedData) {
        this.typeToValue = typeToValue
        val res = context.resources
        val cw = ContextWrapper(context)
        val directory = cw.getExternalFilesDir("Karat")
        templateProvider= TemplateProvider(res)
        reportProvider = ReportBuilder(directory.toString(), directory.toString(), templateProvider)
        parsedData = java.util.HashMap<String, List<List<String>>>()
        parsedData[Fields.DATA] = dataFromDevice.archives
        parsedData[Fields.MODEL] = dataFromDevice.model
        parsedData[Fields.HEADER] = dataFromDevice.headers
        parsedData[Fields.DATE_TIME] = dataFromDevice.systemDate
        parsedData[Fields.SERIAL_NUMBER] = dataFromDevice.serNumber

        viewState.showDialog()
    }

    fun writeXLS(filename: String){
        viewState.showLoadSign()
        reportProvider.constructXlsxReport(filename.addFileFormat(Extensions.XLSX), "test", typeToValue, parsedData)

        viewState.showOnSuccessWrite("Файл записан")

        App.application.getRouter()
            .replaceScreen(FragmentScreen { FlowFragment.newInstance(isToReports = true) })
    }
}