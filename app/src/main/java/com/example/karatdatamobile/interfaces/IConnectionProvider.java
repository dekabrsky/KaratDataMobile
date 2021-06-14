package com.example.karatdatamobile.interfaces;

public interface IConnectionProvider {
    void connect() throws Exception;

    void disconnect() throws Exception;

    boolean isConnected();

    void write(int offset, byte[] data) throws Exception;

    int[] read(int offset, int quantity) throws Exception;
}
