package com.example.karatdatamobile.models;

import com.example.karatdatamobile.enums.ArchiveType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class DeviceDataQuery implements Serializable {
    private final String deviceType;
    private final Date startDate;
    private final ArrayList<ArchiveType> archiveTypes;

    public DeviceDataQuery(String deviceType, Date startDate, ArrayList<ArchiveType> archiveTypes) {
        this.deviceType = deviceType;
        this.startDate = startDate;
        this.archiveTypes = archiveTypes;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public Date getStartDate() {
        return startDate;
    }

    public ArrayList<ArchiveType> getArchiveTypes() {
        return archiveTypes;
    }
}
