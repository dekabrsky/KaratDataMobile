package com.example.karatdatamobile;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.karatdatamobile.Enums.ArchiveType;
import com.example.karatdatamobile.Enums.ConnectionMode;
import com.example.karatdatamobile.Models.ConnectionSettings;
import com.example.karatdatamobile.Models.DeviceDataQuery;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String APP_PREFERENCES = "settingsMemory";
    SharedPreferences sharedSettings;

    ImageButton settingsBtn, openFileManagerBtn;

    EditText fileName;
    Spinner deviseType;
    CalendarView calendar;

    CheckBox h, d, m, emer, integ, prot, event;

    Button startReadDataBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        settingsBtn = findViewById(R.id.imageButton_setting);
        openFileManagerBtn = findViewById(R.id.imageButton_open);

        fileName = findViewById(R.id.editText_name);
        deviseType = findViewById(R.id.spinner_device);
        calendar = findViewById(R.id.calendarView);

        h = findViewById(R.id.checkBox3);
        d = findViewById(R.id.checkBox2);
        m = findViewById(R.id.checkBox4);
        emer = findViewById(R.id.checkBox6);
        integ = findViewById(R.id.checkBox5);
        prot = findViewById(R.id.checkBox8);
        event = findViewById(R.id.checkBox7);

        startReadDataBtn = findViewById(R.id.button_read);

        settingsBtn.setOnClickListener(v -> OpenSettings());
        openFileManagerBtn.setOnClickListener(v -> OpenFileManager());
        startReadDataBtn.setOnClickListener(v -> StartReadData());
    }

    private void OpenSettings() {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }

    private void OpenFileManager() {
        // todo
        Intent intent = new Intent(Intent.ACTION_VIEW);
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        File directory = cw.getExternalFilesDir("Karat");
        Uri uri = Uri.parse(directory.toString());
        Log.d("Uri", uri.getPath());
        intent.setDataAndType(uri, "*/*");
        try {
            startActivity(Intent.createChooser(intent, "Выберите менеджер файлов (проводник)"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "Установите один из менеджеров файлов.", Toast.LENGTH_SHORT).show();
        }
    }

    private void StartReadData() {
        DeviceDataQuery deviceDataQuery = CollectDeviceDataQuery();
        ConnectionSettings connectionSettings = GetConnectionSettings();

    }

    private DeviceDataQuery CollectDeviceDataQuery() {
        String deviceType = deviseType.getTransitionName();
        Date startDate = new Date(calendar.getDate());
        ArrayList<ArchiveType> archiveTypes = new ArrayList<>();

        if (h.isChecked())
            archiveTypes.add(ArchiveType.HOURLY);
        if (d.isChecked())
            archiveTypes.add(ArchiveType.DAILY);
        if (m.isChecked())
            archiveTypes.add(ArchiveType.MONTHLY);
        if (emer.isChecked())
            archiveTypes.add(ArchiveType.EMERGENCY);
        if (integ.isChecked())
            archiveTypes.add(ArchiveType.INTEGRAL);
        if (prot.isChecked())
            archiveTypes.add(ArchiveType.PROTECTIVE);
        if (event.isChecked())
            archiveTypes.add(ArchiveType.EVENTFUL);

        return new DeviceDataQuery(deviceType, startDate, archiveTypes);
    }

    private ConnectionSettings GetConnectionSettings() {
        ConnectionMode connectionMode = ConnectionMode.valueOf(
                sharedSettings.getString("ConnectionMode", ConnectionMode.TCP.toString()));
        String ip = sharedSettings.getString("Ip", null);
        String port = sharedSettings.getString("Port", null);
        String address = sharedSettings.getString("Address", null);

        return new ConnectionSettings(connectionMode, ip, port, address);
    }
}