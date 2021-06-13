package com.example.karatdatamobile.Models;

import android.hardware.usb.UsbManager;

import com.example.karatdatamobile.Enums.ConnectionMode;

import java.io.Serializable;

public class DeviceSettings implements Serializable {
    private final ConnectionMode connectionMode;
    private final String address;

    // TCP Settings
    private String port;
    private String ip;

    // USB Settings
    private int baudrate;
    private UsbManager manager;


    public DeviceSettings(ConnectionMode connectionMode, String port, String ip, String address) {
        this.connectionMode = connectionMode;
        this.port = port;
        this.ip = ip;
        this.address = address;
    }

    public DeviceSettings(ConnectionMode connectionMode, int baudrate, UsbManager manager, String address) {
        this.connectionMode = connectionMode;
        this.baudrate = baudrate;
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

    public int getBaudrate() { return baudrate; }

    public UsbManager getManager() { return manager; }
}
