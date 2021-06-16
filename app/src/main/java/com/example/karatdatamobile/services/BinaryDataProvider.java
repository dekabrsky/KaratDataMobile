package com.example.karatdatamobile.services;

import com.example.karatdatamobile.interfaces.IConnectionProvider;
import com.example.karatdatamobile.interfaces.OnErrorsListener;
import com.example.karatdatamobile.interfaces.OnReadBlockListener;
import com.example.karatdatamobile.models.DataBlock;
import com.example.karatdatamobile.models.DataBlockInfo;

import java.util.ArrayList;
import java.util.Date;

public class BinaryDataProvider {

    private OnReadBlockListener onReadBlockListener;
    private OnErrorsListener onErrorsListener;

    private final IConnectionProvider connectionProvider;

    public BinaryDataProvider(IConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
    }

    public void write(Date start) {
        try {
            connectionProvider.connect();

            byte day = Byte.parseByte(Integer.toHexString(start.getDate()), 16);
            byte month = Byte.parseByte(Integer.toHexString(start.getMonth() + 1), 16);
            byte year = Byte.parseByte(Integer.toHexString(start.getYear()), 16);

            connectionProvider.write(0x0060, new byte[]{0x00, day, month, year});

            connectionProvider.disconnect();

        } catch (Exception e) {
            onErrorsListener.onError(e);
        }
    }

    public DataBlock read(DataBlockInfo dataBlockInfo) {
        return read(new DataBlockInfo[]{dataBlockInfo}).get(0);
    }

    public ArrayList<DataBlock> read(DataBlockInfo[] dataBlocks) {
        ArrayList<DataBlock> result = new ArrayList<>();

        try {
            connectionProvider.connect();

            for (DataBlockInfo dataBlockInfo : dataBlocks) {
                DataBlock readBlock = readBlock(dataBlockInfo);
                onReadBlockListener.onReadBlock(readBlock);
                result.add(readBlock);
            }

            connectionProvider.disconnect();

        } catch (Exception e) {
            onErrorsListener.onError(e);
        }

        return result;
    }

    private DataBlock readBlock(DataBlockInfo dataBlockInfo) throws Exception {
        int[] rawBytes = connectionProvider
                .read(dataBlockInfo.getOffset(), dataBlockInfo.getQuantity());

        return new DataBlock(dataBlockInfo, rawBytes);
    }

    public void onReadBlock(OnReadBlockListener listener) {
        this.onReadBlockListener = listener;
    }

    public void onErrors(OnErrorsListener listener) {
        this.onErrorsListener = listener;
    }
}
