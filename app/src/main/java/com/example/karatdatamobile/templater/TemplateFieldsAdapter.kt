package com.example.karatdatamobile.templater

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.RecyclerView
import com.example.karatdatamobile.R
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class TemplateFieldsAdapter(private val fieldsNames: ArrayList<String>) :
    RecyclerView.Adapter<TemplateFieldsAdapter.TemplateFieldsViewHolder>() {

    private var typeToValue: HashMap<String, String> = HashMap()

    class TemplateFieldsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var field: TextInputEditText = itemView.findViewById(R.id.template_field)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): TemplateFieldsViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.templater_item, parent, false)
        return TemplateFieldsViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TemplateFieldsViewHolder, position: Int) {
        holder.field.hint = fieldsNames[position]
        holder.field.addTextChangedListener {
            typeToValue[fieldsNames[position]] = holder.field.text.toString()
        }
    }

    fun getTypeToValue(): HashMap<String, String> = typeToValue


    override fun getItemCount(): Int = fieldsNames.size
}
