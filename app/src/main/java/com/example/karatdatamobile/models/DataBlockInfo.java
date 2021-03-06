package com.example.karatdatamobile.models;

import com.example.karatdatamobile.enums.DataBlockType;

public class DataBlockInfo {
    private final DataBlockType DataBlockName;
    private final int Offset;
    private final int Quantity;

    public DataBlockInfo(DataBlockType dataBlockName, int offset, int quantity) {
        DataBlockName = dataBlockName;
        Offset = offset;
        Quantity = quantity;
    }

    public DataBlockType getDataBlockName() {
        return DataBlockName;
    }

    public int getOffset() {
        return Offset;
    }

    public int getQuantity() {
        return Quantity;
    }



}

