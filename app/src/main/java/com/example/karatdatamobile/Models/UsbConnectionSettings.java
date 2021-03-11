package com.example.karatdatamobile.Models;

import android.hardware.usb.UsbManager;

import net.wimpi.modbus.usbserial.UsbSerialParameters;

public class UsbConnectionSettings {
    private final UsbSerialParameters params;
    private final UsbManager manager;
    private final int slaveId;

    public UsbConnectionSettings(UsbSerialParameters params, UsbManager manager, int slaveId){
        this.params = params;
        this.manager = manager;
        this.slaveId = slaveId;
    }

    public UsbSerialParameters getParams() {
        return params;
    }

    public UsbManager getManager() {
        return manager;
    }

    public int getSlaveId() {
        return slaveId;
    }
}
