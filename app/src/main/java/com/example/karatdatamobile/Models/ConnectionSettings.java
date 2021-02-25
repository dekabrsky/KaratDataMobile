package com.example.karatdatamobile.Models;

import com.example.karatdatamobile.Enums.ConnectionMode;

public class ConnectionSettings {
    private final ConnectionMode connectionMode;

    // TCP Settings
    private final String port;
    private final String ip;
    private final String Address;

    // USB Settings


    public ConnectionSettings(ConnectionMode connectionMode, String port, String ip, String address) {
        this.connectionMode = connectionMode;
        this.port = port;
        this.ip = ip;
        Address = address;
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
        return Address;
    }
}
