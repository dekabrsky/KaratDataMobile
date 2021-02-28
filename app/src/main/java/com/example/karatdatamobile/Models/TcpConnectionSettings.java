package com.example.karatdatamobile.Models;

public class TcpConnectionSettings {

    private final String port;
    private final String ip;

    private final int slaveId;

    public TcpConnectionSettings(String port, String ip, int slaveId) {
        this.port = port;
        this.ip = ip;
        this.slaveId = slaveId;
    }

    public String getPort() {
        return port;
    }

    public String getIp() {
        return ip;
    }

    public int getSlaveId() {
        return slaveId;
    }
}
