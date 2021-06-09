package com.example.karatdatamobile.settingsSetup;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.karatdatamobile.MainActivity;
import com.example.karatdatamobile.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class SettingDeviceActivity extends AppCompatActivity {
    FloatingActionButton floatingActionButton_close;
    TextInputLayout field_address, field_port, field_ip;
    TextInputEditText address, port, ip;
    RadioButton radioTCP, radioUSB;
    Boolean mode = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_device);
        field_address = findViewById(R.id.text_field_address);
        field_ip = findViewById(R.id.text_field_ip);
        field_port = findViewById(R.id.text_field_port);
        port = findViewById(R.id.edit_text_port);
        ip = findViewById(R.id.edit_text_ip);
        address = findViewById(R.id.edit_text_address);
        address.setVisibility(View.GONE);
        ip.setVisibility(View.GONE);
        port.setVisibility(View.GONE);
        field_port.setVisibility(View.GONE);
        field_ip.setVisibility(View.GONE);
        field_address.setVisibility(View.GONE);
        radioTCP = (RadioButton) findViewById(R.id.tcp);
        radioUSB = (RadioButton) findViewById(R.id.usb);
        floatingActionButton_close = findViewById(R.id.fab_close);

        floatingActionButton_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingDeviceActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onRadioButtonClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        switch(view.getId()) {
            case R.id.tcp:
                enableTCP();
                break;
            case R.id.usb:
                enableUSB();
                break;
        }
    }

    private void enableUSB() {
        radioTCP.setChecked(true);
        ip.setVisibility(View.GONE);
        port.setVisibility(View.GONE);
        address.setVisibility(View.GONE);
        field_port.setVisibility(View.GONE);
        field_ip.setVisibility(View.GONE);
        field_address.setVisibility(View.GONE);
        radioTCP.setChecked(false);
        mode = false;
    }

    private void enableTCP() {
        radioUSB.setChecked(true);
        ip.setVisibility(View.VISIBLE);
        port.setVisibility(View.VISIBLE);
        address.setVisibility(View.VISIBLE);
        field_port.setVisibility(View.VISIBLE);
        field_ip.setVisibility(View.VISIBLE);
        field_address.setVisibility(View.VISIBLE);
        radioUSB.setChecked(false);
        mode = true;
    }
}