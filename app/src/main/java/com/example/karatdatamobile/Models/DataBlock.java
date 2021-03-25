package com.example.karatdatamobile.Models;

public class DataBlock {
    private final DataBlockInfo dataBlockInfo;
    private final int[] data;

    public DataBlock(DataBlockInfo dataBlockInfo, int[] data) {
        this.dataBlockInfo = dataBlockInfo;
        this.data = data;
    }

    public DataBlockInfo getDataBlockInfo() {
        return dataBlockInfo;
    }

    public int[] getData() {
        return data;
    }
}
