package com.example.karatdatamobile.models;

import org.apache.poi.ss.util.CellAddress;

import java.util.HashMap;

public class XlsxDataCells {

    private final HashMap<String, CellAddress> userDataCells;
    private final HashMap<String, CellAddress> deviceDataCells;

    public XlsxDataCells(HashMap<String, CellAddress> userDataCells, HashMap<String, CellAddress> deviceDataCells) {
        this.userDataCells = userDataCells;
        this.deviceDataCells = deviceDataCells;
    }

    public HashMap<String, CellAddress> getUserDataCells() {
        return userDataCells;
    }

    public HashMap<String, CellAddress> getDeviceDataCells() {
        return deviceDataCells;
    }
}
