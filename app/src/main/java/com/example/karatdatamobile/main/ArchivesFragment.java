package com.example.karatdatamobile.main;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.example.karatdatamobile.R;
import com.example.karatdatamobile.SettingDeviceActivity;
@SuppressLint("NewApi")
public class ArchivesFragment extends Fragment {

    Spinner spinner;
    EditText edit_text_date;
    EditText edit_text_setting;
    ImageButton img_setting;
    ImageButton img_date;
    public static final String TAG = "EditActivity";
    private DatePickerDialog.OnDateSetListener mDateSetListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_archives, container, false);
        String[] countries = { "Выберите прибор", "213/223", "306/307/308"};
        spinner = view.findViewById(R.id.spinner);
        edit_text_date = view.findViewById(R.id.edit_text_date);
        edit_text_setting = view.findViewById(R.id.edit_text_setting);
        img_setting = (ImageButton) view.findViewById(R.id.img_setting);
        edit_text_setting.setFocusable(false);
        edit_text_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(getActivity(), SettingDeviceActivity.class);
                getActivity().startActivity(intent);
            }
        });

        img_date = view.findViewById(R.id.img_data);
        edit_text_date.setFocusable(false);
        edit_text_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        R.style.CalendarDatePickerDialog1,
                        mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });
        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                String date = day + "/" + month + "/" + year;
                edit_text_date.setText(date);

            }
        };

        return view;
    }
}
