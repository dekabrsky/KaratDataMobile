package com.example.karatdatamobile.services;

import com.example.karatdatamobile.models.TcpConnectionSettings;
import net.wimpi.modbus.io.ModbusRTUTCPTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteMultipleRegistersRequest;
import net.wimpi.modbus.net.RTUTCPMasterConnection;

import static com.example.karatdatamobile.services.BinaryDataParser.timeBytesToRegisters;

public class TcpConnectionProvider extends ModbusConnectionProvider<TcpConnectionSettings> {

    public TcpConnectionProvider(TcpConnectionSettings tcpConnectionSettings) {
        super(tcpConnectionSettings);
    }

    @Override
    public void write(int offset, byte[] data) throws Exception {
        WriteMultipleRegistersRequest writeRequest = new WriteMultipleRegistersRequest();
        writeRequest.setReference(offset);
        writeRequest.setRegisters(timeBytesToRegisters(data));
        writeRequest.setUnitID(settings.getSlaveId());
        ModbusRTUTCPTransaction trans = new ModbusRTUTCPTransaction((RTUTCPMasterConnection) master);
        trans.setRequest(writeRequest);
        trans.execute();
    }

    @Override
    public int[] read(int offset, int quantity) throws Exception {
        ReadMultipleRegistersRequest req = new ReadMultipleRegistersRequest(offset, quantity);
        req.setUnitID(settings.getSlaveId());

        ModbusRTUTCPTransaction trans = new ModbusRTUTCPTransaction((RTUTCPMasterConnection) master);
        trans.setRequest(req);
        trans.execute();
        ReadMultipleRegistersResponse res = (ReadMultipleRegistersResponse) trans.getResponse();

        int[] values = new int[quantity];

        for (int i = 0; i < quantity; i++) {
            values[i] = res.getRegisterValue(i);
        }

        return values;
    }
}
