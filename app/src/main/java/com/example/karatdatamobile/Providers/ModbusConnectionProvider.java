package com.example.karatdatamobile.Providers;

import com.example.karatdatamobile.Interfaces.IConnectionProvider;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.serial.SerialParameters;
import com.intelligt.modbus.jlibmodbus.serial.SerialPortException;

import java.net.UnknownHostException;

public abstract class ModbusConnectionProvider<TSettings> implements IConnectionProvider {

    protected final TSettings settings;

    protected ModbusMaster master = null;

    public ModbusConnectionProvider(TSettings settings) {
        this.settings = settings;
    }

    @Override
    public void connect() throws ModbusIOException {
        if (master == null)
            createModbusMaster();
        if (isConnected())
            return;

        master.connect();
    }

    @Override
    public void disconnect() throws ModbusIOException {
        if (!isConnected())
            return;

        master.disconnect();
    }

    @Override
    public boolean isConnected() {
        return master.isConnected();
    }

    private void createModbusMaster() {
        try {
            SerialParameters serialParameters = getSerialParameters();
            master = ModbusMasterFactory.createModbusMasterRTU(serialParameters);
            master.setResponseTimeout(10000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected abstract SerialParameters getSerialParameters() throws Exception;
}
