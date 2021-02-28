package com.example.karatdatamobile.Services.Reader;

import com.example.karatdatamobile.Interfaces.IConnectionProvider;
import com.example.karatdatamobile.Models.DataBlock;
import com.example.karatdatamobile.Models.DataBlockInfo;

import java.util.ArrayList;

public class DataReader {

    private OnReadBlockListener onReadBlockListener;
    private OnErrorsListener onErrorsListener;

    private final IConnectionProvider connectionProvider;

    public DataReader(IConnectionProvider connectionProvider) {
        this.connectionProvider = connectionProvider;
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
