package com.example.karatdatamobile.reports

import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.core.content.FileProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.karatdatamobile.App
import com.example.karatdatamobile.R
import com.example.karatdatamobile.interfaces.IReportBuilder
import com.example.karatdatamobile.interfaces.ITemplateProvider
import com.example.karatdatamobile.models.ParsedData
import com.example.karatdatamobile.models.ParsedDataDataClass
import com.example.karatdatamobile.services.ReportBuilder
import com.example.karatdatamobile.services.TemplateProvider
import com.example.karatdatamobile.templater.TemplaterFragment
import com.example.karatdatamobile.utils.DateTime.toSimpleDateTime
import com.example.karatdatamobile.utils.Extensions
import com.example.karatdatamobile.utils.Fields
import com.example.karatdatamobile.utils.Files.addFileFormat
import com.example.karatdatamobile.utils.Views.hide
import com.example.karatdatamobile.utils.Views.show
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textview.MaterialTextView
import com.google.gson.Gson
import java.io.File
import java.util.*

class ReportsAdapter constructor(val context: Context, var files: List<ReportRecordModel>) :
    RecyclerView.Adapter<ReportsAdapter.ReportsViewHolder>() {

    private val directory = ContextWrapper(context).getExternalFilesDir("Karat").toString()

    class ReportsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var reportBlock: View = itemView.findViewById(R.id.reportBlock)
        var reportName: MaterialTextView = itemView.findViewById(R.id.reportName)
        var lastModified: MaterialTextView = itemView.findViewById(R.id.lastModified)

        var formatXLSX: MaterialTextView = itemView.findViewById(R.id.formatXLSX)
        var formatCSV: MaterialTextView = itemView.findViewById(R.id.formatCSV)

        var shareReport: MaterialTextView = itemView.findViewById(R.id.shareReport)
        var shareXLS: MaterialTextView = itemView.findViewById(R.id.shareXLS)
        var shareCSV: MaterialTextView = itemView.findViewById(R.id.shareCSV)

        var deleteReport: MaterialTextView = itemView.findViewById(R.id.deleteReport)
        var loadSign: MaterialTextView = itemView.findViewById(R.id.loadSign)
    }

    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportsViewHolder {
        val view = inflater.inflate(R.layout.report_list_item, parent, false)
        return ReportsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportsViewHolder, position: Int) {
        var isMenuShoved = false
        var isShareShowed = false

        holder.reportName.text = files[position].fileName
        holder.lastModified.text = files[position].lastModified.toSimpleDateTime()

        holder.reportBlock.setOnClickListener {
            isMenuShoved = onReportClick(holder, isMenuShoved, position)
        }

        holder.formatXLSX.setOnClickListener { onCreateXLSClick(holder, position) }

        holder.formatCSV.setOnClickListener { onCreateCSVClick(holder, position) }

        holder.shareReport.setOnClickListener {
            isShareShowed = onShareClick(holder, isShareShowed, position)
        }

        holder.shareCSV.setOnClickListener { onShareFile(Extensions.CSV, position) }

        holder.shareXLS.setOnClickListener { onShareFile(Extensions.XLSX, position) }
    }

    private fun onReportClick(holder: ReportsViewHolder, isMenuShoved: Boolean, position: Int): Boolean =
        if (isMenuShoved){
            holder.deleteReport.hide()
            holder.shareReport.hide()
            holder.formatXLSX.hide()
            holder.formatCSV.hide()
            holder.shareXLS.hide()
            holder.shareCSV.hide()
            holder.loadSign.hide()
            false
        } else {
            holder.deleteReport.show()
            if (hasFormatFile(position, Extensions.CSV) || hasFormatFile(position, Extensions.XLSX))
                holder.shareReport.show()
            if (hasFormatFile(position, Extensions.XLSX).not())
                holder.formatXLSX.show()
            if (hasFormatFile(position, Extensions.CSV).not())
                holder.formatCSV.show()
            true
        }

    private fun hasFormatFile(position: Int, format: String): Boolean {
        val f = "${directory}/${files[position].fileName.addFileFormat(format)}"
        return File(f).isFile
    }

    private fun onCreateXLSClick(holder: ReportsViewHolder, position: Int){
        holder.loadSign.show()

        val gson = Gson()
        val stringFromFile = files[position].file.readText()
        val data = gson.fromJson(stringFromFile, ParsedDataDataClass::class.java)

        App.application.getRouter().navigateTo(FragmentScreen{
            TemplaterFragment.newInstance(
                ParsedData(data) , files[position].fileName
            )
        })
    }

    private fun onCreateCSVClick(holder: ReportsViewHolder, position: Int) {
        val gson = Gson()
        val stringFromFile = files[position].file.readText()
        val data = gson.fromJson(stringFromFile, ParsedDataDataClass::class.java)
        val res = context.resources
        val parsedData = HashMap<String, List<List<String>>>()
        val parsedDataModel = ParsedData(data)
        parsedData[Fields.DATA] = parsedDataModel.archives
        parsedData[Fields.MODEL] = parsedDataModel.model
        parsedData[Fields.HEADER] = parsedDataModel.headers
        parsedData[Fields.DATE_TIME] = parsedDataModel.systemDate
        parsedData[Fields.SERIAL_NUMBER] = parsedDataModel.serNumber
        val templateProvider: ITemplateProvider = TemplateProvider(res)
        val reportProvider: IReportBuilder =
            ReportBuilder(directory, directory, templateProvider)

        try {
            reportProvider.constructCsvReport(
                files[position].fileName.addFileFormat(Extensions.CSV),
                parsedData
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }

        Snackbar
            .make(
                holder.itemView,
                "Файл записан: ${files[position].fileName.addFileFormat(Extensions.CSV)}",
                Snackbar.LENGTH_LONG)
            .show()
    }

    private fun onShareFile(ext: String, position: Int){
        val file = File("${directory}/${files[position].fileName.addFileFormat(ext)}")
        val path = FileProvider.getUriForFile(
            context,
            "com.example.karatdatamobile.fileprovider",
            file
        )
        val fileIntent = Intent(Intent.ACTION_VIEW)
        fileIntent.type = "application/vnd.ms-excel"
        fileIntent.data = path
        fileIntent.putExtra(Intent.EXTRA_SUBJECT, "Архив ${files[position].fileName}${ext}")
        fileIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        fileIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(context, fileIntent, fileIntent.extras)
    }

    private fun onShareClick(holder: ReportsViewHolder, isShareShowed: Boolean, pos: Int) : Boolean =
        if (isShareShowed) {
            holder.shareCSV.hide()
            holder.shareXLS.hide()
            false
        } else {
            if (hasFormatFile(pos, Extensions.CSV))
                holder.shareCSV.show()
            if (hasFormatFile(pos, Extensions.XLSX))
                holder.shareXLS.show()
            true
        }


    override fun getItemCount(): Int {
        return files.size
    }
}