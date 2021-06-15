package com.example.karatdatamobile.reports

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.karatdatamobile.R
import com.google.android.material.textview.MaterialTextView

class ReportsAdapter constructor(context: Context, var files: List<ReportRecordModel>) :
    RecyclerView.Adapter<ReportsAdapter.ReportsViewHolder>() {

    var isMenuShoved = false

    class ReportsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var reportName: MaterialTextView = itemView.findViewById(R.id.reportName)
        var formatXLSX: MaterialTextView = itemView.findViewById(R.id.formatXLSX)
        var formatCSV: MaterialTextView = itemView.findViewById(R.id.formatCSV)
        var editReport: MaterialTextView = itemView.findViewById(R.id.editReport)
        var deleteReport: MaterialTextView = itemView.findViewById(R.id.deleteReport)
        var lastModified: MaterialTextView = itemView.findViewById(R.id.lastModified)
    }


    private val inflater: LayoutInflater = LayoutInflater.from(context)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReportsViewHolder {
        val view = inflater.inflate(R.layout.report_list_item, parent, false)
        return ReportsViewHolder(view)
    }

    override fun onBindViewHolder(holder: ReportsViewHolder, position: Int) {
        holder.reportName.text = files[position].fileName
        holder.lastModified.text = files[position].lastModified.toString()
        holder.reportName.setOnClickListener {
            if (isMenuShoved){
                holder.deleteReport.visibility = View.GONE
                holder.editReport.visibility = View.GONE
                holder.formatXLSX.visibility = View.GONE
                holder.formatCSV.visibility = View.GONE
                isMenuShoved = false
            }
            else{
                holder.deleteReport.visibility = View.VISIBLE
                holder.editReport.visibility = View.VISIBLE
                holder.formatXLSX.visibility = View.VISIBLE
                holder.formatCSV.visibility = View.VISIBLE
                isMenuShoved = true
            }
        }
    }

    override fun getItemCount(): Int {
        return files.size
    }
}