package com.example.karatdatamobile.terminal

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.karatdatamobile.R

class TerminalAdapter(private val messages: ArrayList<String>) :
    RecyclerView.Adapter<TerminalAdapter.TerminalViewHolder>() {
    class TerminalViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var message: TextView = itemView.findViewById(R.id.terminalMessage)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TerminalViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.terminal_list_item, parent, false)
        return TerminalViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TerminalViewHolder, position: Int) {
        holder.message.text = messages[position]

    }

    override fun getItemCount(): Int = messages.size
}
