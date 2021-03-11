package com.example.karatdatamobile.Services;

import com.example.karatdatamobile.Interfaces.IConnectionProvider;
import com.example.karatdatamobile.Models.UsbConnectionSettings;

public class UsbConnectionProvider implements IConnectionProvider {
    public UsbConnectionProvider(UsbConnectionSettings usbConnectionSettings) {
    }

    @Override
    public void connect() throws Exception {

    }

    @Override
    public void disconnect() throws Exception {

    }

    @Override
    public boolean isConnected() {
        return false;
    }

    @Override
    public void write(int offset, byte[] data) throws Exception {

    }

    @Override
    public int[] read(int offset, int quantity) throws Exception {
        return new int[0];
    }
}
