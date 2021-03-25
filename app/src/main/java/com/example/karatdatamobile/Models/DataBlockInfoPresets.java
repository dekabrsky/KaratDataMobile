package com.example.karatdatamobile.Models;

import com.example.karatdatamobile.Enums.DataBlockType;

public class DataBlockInfoPresets {
    public static DataBlockInfo Model() {
        return new DataBlockInfo(DataBlockType.MODEL, 0x0708, Integer.parseInt("2", 16) / 2);
    }

    public static DataBlockInfo DateTime() {
        return new DataBlockInfo(DataBlockType.DATE_TIME, 0x0062, Integer.parseInt("8", 16) / 2);
    }

    public static DataBlockInfo SerialNumber() {
        return new DataBlockInfo(DataBlockType.SERIAL_NUMBER, 0x0101, Integer.parseInt("36", 16) / 2);
    }

    public static DataBlockInfo ArchivesConfig() {
        return new DataBlockInfo(DataBlockType.ARCHIVES_CONFIG, 0x0106, Integer.parseInt("38", 16) / 2);
    }
}
