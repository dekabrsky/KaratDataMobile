package com.example.karatdatamobile.Models;

import com.example.karatdatamobile.Enums.ConnectionMode;

import java.io.Serializable;

public class AppSettings implements Serializable {
    private final ConnectionMode connectionMode;

    // TCP Settings
    private final String port;
    private final String ip;
    private final String address;

    // USB Settings


    public AppSettings(ConnectionMode connectionMode, String port, String ip, String address) {
        this.connectionMode = connectionMode;
        this.port = port;
        this.ip = ip;
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
}
