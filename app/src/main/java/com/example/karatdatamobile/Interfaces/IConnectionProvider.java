package com.example.karatdatamobile.Interfaces;

public interface IConnectionProvider {
    void connect() throws Exception;
    void disconnect() throws Exception;
    boolean isConnected();
    int[] read(int offset, int quantity) throws Exception;
}
