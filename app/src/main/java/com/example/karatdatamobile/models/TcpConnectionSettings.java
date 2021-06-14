package com.example.karatdatamobile.models;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TcpConnectionSettings {
    private final int port;
    private InetAddress ip;
    private final int slaveId;

    public TcpConnectionSettings(String port, String ip, int slaveId) {
        this.port = Integer.parseInt(port);
        try {
            this.ip = InetAddress.getByName(ip);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        this.slaveId = slaveId;
    }

    public int getPort() {
        return port;
    }

    public InetAddress getIp() {
        return ip;
    }

    public int getSlaveId() {
        return slaveId;
    }
}
