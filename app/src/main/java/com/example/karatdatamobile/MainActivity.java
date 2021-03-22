package com.example.karatdatamobile;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.karatdatamobile.Enums.ArchiveType;
import com.example.karatdatamobile.Enums.ConnectionMode;
import com.example.karatdatamobile.Models.AppSettings;
import com.example.karatdatamobile.Models.DeviceDataQuery;

import net.wimpi.modbus.usbserial.UsbSerialParameters;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final String APP_PREFERENCES = "settingsMemory";
    SharedPreferences sharedSettings;

    ImageButton settingsBtn, openFileManagerBtn;

    EditText fileName;
    Spinner deviseType;
    CalendarView calendar;
    Date startDate;

    CheckBox hourly, daily, monthly, emergency, integral, protective, eventful;

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

        daily = findViewById(R.id.checkBox2);
        hourly = findViewById(R.id.checkBox3);
        monthly = findViewById(R.id.checkBox4);
        integral = findViewById(R.id.checkBox5);
        emergency = findViewById(R.id.checkBox6);
        eventful = findViewById(R.id.checkBox7);
        protective = findViewById(R.id.checkBox8);

        startReadDataBtn = findViewById(R.id.button_read);

        settingsBtn.setOnClickListener(this::openSettings);
        openFileManagerBtn.setOnClickListener(this::openFileManager);
        startReadDataBtn.setOnClickListener(this::startReadData);

        calendar = findViewById(R.id.calendarView);
        startDate = new Date(calendar.getDate());
        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year,
                                            int month, int dayOfMonth) {
                startDate = new Date(year - 1900, month, dayOfMonth);
            }
        });
    }

    private void openSettings(View view) {
        startActivity(new Intent(MainActivity.this, SettingsActivity.class));
    }

    private void openFileManager(View view) {
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

    private void startReadData(View view) {
        DeviceDataQuery deviceDataQuery = collectDeviceDataQuery();
        //AppSettings appSettings = getAppSettings();

        Intent toTerm = new Intent(MainActivity.this, TerminalActivity.class);
        toTerm.putExtra("DeviceDataQuery", deviceDataQuery);
        //toTerm.putExtra("AppSettings", appSettings);

        startActivity(toTerm);
    }

    private DeviceDataQuery collectDeviceDataQuery() {
        String deviceType = deviseType.getTransitionName();
        //Date startDate = new Date(calendar.getDate());
        Log.d("Start Date", String.valueOf(startDate));
        ArrayList<ArchiveType> archiveTypes = new ArrayList<>();

        if (hourly.isChecked())
            archiveTypes.add(ArchiveType.HOURLY);
        if (daily.isChecked())
            archiveTypes.add(ArchiveType.DAILY);
        if (monthly.isChecked())
            archiveTypes.add(ArchiveType.MONTHLY);
        if (emergency.isChecked())
            archiveTypes.add(ArchiveType.EMERGENCY);
        if (integral.isChecked())
            archiveTypes.add(ArchiveType.INTEGRAL);
        if (protective.isChecked())
            archiveTypes.add(ArchiveType.PROTECTIVE);
        if (eventful.isChecked())
            archiveTypes.add(ArchiveType.EVENTFUL);

        return new DeviceDataQuery(deviceType, startDate, archiveTypes);
    }

    private AppSettings getAppSettings() {
        ConnectionMode connectionMode = ConnectionMode.valueOf(
                sharedSettings.getString("ConnectionMode", ConnectionMode.TCP.toString()));
        String ip = sharedSettings.getString("Ip", null);
        String port = sharedSettings.getString("Port", null);
        String address = sharedSettings.getString("Address", null);
        int baudrate = sharedSettings.getInt("Baudrate", 19200);

        if (connectionMode.equals(ConnectionMode.TCP))
            return new AppSettings(connectionMode, port, ip, address);
        else {
            UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
            return new AppSettings(connectionMode, baudrate, usbManager, address);
        }
    }
}