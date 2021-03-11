package com.example.karatdatamobile.Models;

import android.hardware.usb.UsbManager;

import com.example.karatdatamobile.Enums.ConnectionMode;

import net.wimpi.modbus.usbserial.UsbSerialParameters;

import java.io.Serializable;
import java.net.InetAddress;

public class AppSettings implements Serializable {
    private final ConnectionMode connectionMode;
    private final String address;

    // TCP Settings
    private String port;
    private String ip;

    // USB Settings
    private UsbSerialParameters params;
    private UsbManager manager;


    public AppSettings(ConnectionMode connectionMode, String port, String ip, String address) {
        this.connectionMode = connectionMode;
        this.port = port;
        this.ip = ip;
        this.address = address;
    }

    public AppSettings(ConnectionMode connectionMode, UsbSerialParameters params, UsbManager manager, String address) {
        this.connectionMode = connectionMode;
        this.params = params;
        this.manager = manager;
        this.address = address;
    }

    public ConnectionMode getConnectionMode() {
        return connectionMode;
    }

    public String getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public String getAddress() {
        return address;
    }

    public UsbSerialParameters getParams(){ return params; }

    public UsbManager getManager(){ return manager; }
}
