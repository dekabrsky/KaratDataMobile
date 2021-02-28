package com.example.karatdatamobile.Services;

import com.example.karatdatamobile.Enums.ConnectionMode;
import com.example.karatdatamobile.Interfaces.IConnectionProvider;
import com.example.karatdatamobile.Models.AppSettings;
import com.example.karatdatamobile.Models.TcpConnectionSettings;

public class ConnectionProviderFactory {

    public static IConnectionProvider Create(AppSettings appSettings) {
        ConnectionMode connectionMode = appSettings.getConnectionMode();

        switch (connectionMode) {
            case TCP:
                return createTcpConnectionProvider(appSettings);
            case USB:
                return null;
        }

        return null;
    }

    private static IConnectionProvider createTcpConnectionProvider(AppSettings appSettings) {
        TcpConnectionSettings tcpConnectionSettings = toTcpConnectionSettings(appSettings);
        return new TcpConnectionProvider(tcpConnectionSettings);
    }

    private static TcpConnectionSettings toTcpConnectionSettings(AppSettings appSettings) {
        String port = appSettings.getPort();
        String ip = appSettings.getIp();
        int slaveId = Integer.parseInt(appSettings.getAddress());

        return new TcpConnectionSettings(port, ip, slaveId);
    }
}
