package com.example.karatdatamobile.Models;

import com.example.karatdatamobile.Enums.ArchiveType;

import java.util.HashMap;

public class ArchivesRegisters {
    private HashMap<ArchiveType, Integer> NameToCode;

    public ArchivesRegisters() {
        NameToCode = new HashMap<>();
        NameToCode.put(ArchiveType.HOURLY, 0x00);
        NameToCode.put(ArchiveType.DAILY, 0x10);
        NameToCode.put(ArchiveType.MONTHLY, 0x20);
        NameToCode.put(ArchiveType.INTEGRAL, 0x30);
        NameToCode.put(ArchiveType.EMERGENCY, 0x40);
        NameToCode.put(ArchiveType.EVENTFUL, 0x50);
        NameToCode.put(ArchiveType.PROTECTIVE, 0x70);
    }

    public HashMap<ArchiveType, Integer> getNameToCode() {
        return NameToCode;
    }
}
