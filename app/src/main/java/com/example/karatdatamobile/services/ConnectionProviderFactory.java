package com.example.karatdatamobile.services;

import android.hardware.usb.UsbManager;

import com.example.karatdatamobile.enums.ConnectionMode;
import com.example.karatdatamobile.interfaces.IConnectionProvider;
import com.example.karatdatamobile.models.DeviceSettings;
import com.example.karatdatamobile.models.TcpConnectionSettings;
import com.example.karatdatamobile.models.UsbConnectionSettings;

import net.wimpi.modbus.usbserial.UsbSerialParameters;

public class ConnectionProviderFactory {

    public static IConnectionProvider Create(DeviceSettings appSettings) {
        ConnectionMode connectionMode = appSettings.getConnectionMode();

        switch (connectionMode) {
            case TCP:
                return createTcpConnectionProvider(appSettings);
            case USB:
                return createUsbConnectionProvider(appSettings);
        }

        return null;
    }

    private static IConnectionProvider createTcpConnectionProvider(DeviceSettings appSettings) {
        TcpConnectionSettings tcpConnectionSettings = toTcpConnectionSettings(appSettings);
        return new TcpConnectionProvider(tcpConnectionSettings);
    }

    private static TcpConnectionSettings toTcpConnectionSettings(DeviceSettings appSettings) {
        String port = appSettings.getPort();
        String ip = appSettings.getIp();
        int slaveId = Integer.parseInt(appSettings.getAddress());

        return new TcpConnectionSettings(port, ip, slaveId);
    }

    private static IConnectionProvider createUsbConnectionProvider(DeviceSettings appSettings) {
        UsbConnectionSettings usbConnectionSettings = toUsbConnectionSettings(appSettings);
        return new UsbConnectionProvider(usbConnectionSettings);
    }

    private static UsbConnectionSettings toUsbConnectionSettings(DeviceSettings appSettings) {
        UsbSerialParameters params = new UsbSerialParameters();
        params.setPortName("COM1");
        params.setBaudRate(appSettings.getBaudrate());
        params.setDatabits(8);
        params.setParity("None");
        params.setStopbits(1);
        params.setEncoding("rtu");
        params.setEcho(false);
        UsbManager manager = appSettings.getManager();
        int slaveId = Integer.parseInt(appSettings.getAddress());
        return new UsbConnectionSettings(params, manager, slaveId);
    }
}
