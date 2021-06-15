package com.example.karatdatamobile.reports

import android.content.Context
import android.content.ContextWrapper
import com.example.karatdatamobile.models.RecordRow
import com.example.karatdatamobile.utils.Extensions
import moxy.MvpPresenter
import java.io.File
import java.util.*
import javax.inject.Inject

class ReportsPresenter @Inject constructor(
    private val context: Context
) : MvpPresenter<ReportsView>() {

    fun getAdapter(): ReportsAdapter {
        val cw = ContextWrapper(context)
        val directory = cw.getExternalFilesDir("Karat")
        return ReportsAdapter(context, listFilesWithSubFolders(directory))
    }

    private fun listFilesWithSubFolders(dir: File?): List<ReportRecordModel> {
        val files = mutableListOf<File>()
        if (dir != null) {
            for (file in dir.listFiles()) {
                files.add(file)
            }
        }
        return getFileNames(files.filter { it.name.endsWith(Extensions.JSON) })
    }

    private fun getFileNames(files: List<File>): List<ReportRecordModel>{
        return files.map {
            ReportRecordModel(
                it.name.replace(".json", ""),
                Date(it.lastModified()),
                it
            )
        }
    }
}