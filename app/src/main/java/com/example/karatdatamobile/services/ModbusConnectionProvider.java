package com.example.karatdatamobile.services;

import com.example.karatdatamobile.interfaces.IConnectionProvider;
import com.example.karatdatamobile.models.TcpConnectionSettings;
import com.example.karatdatamobile.models.UsbConnectionSettings;

import net.wimpi.modbus.net.MasterConnection;
import net.wimpi.modbus.net.RTUTCPMasterConnection;
import net.wimpi.modbus.net.SerialConnection;

public abstract class ModbusConnectionProvider<TSettings> implements IConnectionProvider {

    protected final TSettings settings;

    protected MasterConnection master = null;

    public ModbusConnectionProvider(TSettings settings) {
        this.settings = settings;
    }

    @Override
    public void connect() throws Exception /* дописать нужный Exception в yamod */ {
        if (master == null)
            createModbusMaster();
        if (isConnected())
            return;

        master.connect();
    }

    @Override
    public void disconnect(){
        if (!isConnected())
            return;

        master.close();
    }

    @Override
    public boolean isConnected() {
        return master.isConnected();
    }

    private void createModbusMaster() {
        try {
            if (settings.getClass().equals(TcpConnectionSettings.class)){
                TcpConnectionSettings tcpSettings = (TcpConnectionSettings) settings;
                master = new RTUTCPMasterConnection(tcpSettings.getIp(), tcpSettings.getPort());
            }
            else if (settings.getClass().equals(UsbConnectionSettings.class)){
                UsbConnectionSettings usbSettings = (UsbConnectionSettings) settings;
                master = new SerialConnection(usbSettings.getManager(), usbSettings.getParams());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
