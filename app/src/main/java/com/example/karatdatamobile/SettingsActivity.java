package com.example.karatdatamobile;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.karatdatamobile.Enums.ConnectionMode;

public class SettingsActivity extends AppCompatActivity {

    private static final String APP_PREFERENCES = "settingsMemory";
    SharedPreferences sharedSettings;

    ImageButton saveSettingsBtn;

    EditText ip, port, address;
    RadioButton radioTCP, radioUSB;

    TextView ip_text, usb_text;
    Spinner spinner_usb;

    ConnectionMode mode = ConnectionMode.TCP;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sharedSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        saveSettingsBtn = findViewById(R.id.image_back_button);

        ip = findViewById(R.id.editText_ID);
        port = findViewById(R.id.editText_name);
        address = findViewById(R.id.editText_a);

        ip_text = findViewById(R.id.textView2);
        usb_text = findViewById(R.id.textView_usb);
        spinner_usb = findViewById(R.id.spinner_usb);

        radioTCP = (RadioButton) findViewById(R.id.TCP);
        radioUSB = (RadioButton) findViewById(R.id.usb);

        saveSettingsBtn.setOnClickListener(v -> SaveSettingsAdnReturn());

        SetSavedSettings();
    }

    private void SetSavedSettings() {
        ConnectionMode _connectionMode = ConnectionMode.valueOf(
                sharedSettings.getString("ConnectionMode", ConnectionMode.TCP.toString()));
        String _ip = sharedSettings.getString("Ip", "");
        String _port = sharedSettings.getString("Port", "");
        String _address = sharedSettings.getString("Address", "");

        ip.setText(_ip);
        port.setText(_port);
        address.setText(_address);

        switch (_connectionMode) {
            case TCP:
                enableTCP();
                break;
            case USB:
                enableUSB();
                break;
        }
    }

    private void SaveSettingsAdnReturn() {
        SaveSettings();

        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
    }

    private void SaveSettings() {
        SharedPreferences.Editor editor = sharedSettings.edit();
        editor.putString("Ip", ip.getText().toString());
        editor.putString("Port", port.getText().toString());
        editor.putString("Address", address.getText().toString());
        editor.putString("ConnectionMode", mode.toString());
        editor.apply();
    }

    public void onRadioButtonClicked(View view) {
        switch (view.getId()) {
            case R.id.TCP:
                enableTCP();
                break;
            case R.id.usb:
                enableUSB();
                break;
        }
    }

    private void enableUSB() {
        radioUSB.setChecked(true);
        radioTCP.setChecked(false);

        ip.setVisibility(View.GONE);
        ip_text.setVisibility(View.GONE);

        usb_text.setVisibility(View.VISIBLE);
        spinner_usb.setVisibility(View.VISIBLE);

        mode = ConnectionMode.USB;
    }

    private void enableTCP() {
        radioTCP.setChecked(true);
        radioUSB.setChecked(false);

        ip.setVisibility(View.VISIBLE);
        ip_text.setVisibility(View.VISIBLE);

        usb_text.setVisibility(View.GONE);
        spinner_usb.setVisibility(View.GONE);

        mode = ConnectionMode.TCP;
    }
}
