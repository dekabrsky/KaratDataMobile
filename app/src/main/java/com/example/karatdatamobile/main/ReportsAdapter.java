package com.example.karatdatamobile.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.karatdatamobile.R;

import java.util.List;

public class ReportsAdapter extends RecyclerView.Adapter<ReportsAdapter.ViewHolder>{

    private final LayoutInflater inflater;
    private final List<ReportsState> states;

    ReportsAdapter(Context context, List<ReportsState> states) {
        this.states = states;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public ReportsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = inflater.inflate(R.layout.report_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ReportsAdapter.ViewHolder holder, int position) {
        ReportsState state = states.get(position);
        holder.nameReportView.setText(state.getName());
        holder.formatView.setText(state.getCapital());
    }

    @Override
    public int getItemCount() {
        return states.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView nameReportView, formatView;
        ViewHolder(View view){
            super(view);
            nameReportView = view.findViewById(R.id.report);
            formatView = view.findViewById(R.id.format);
        }
    }
}
