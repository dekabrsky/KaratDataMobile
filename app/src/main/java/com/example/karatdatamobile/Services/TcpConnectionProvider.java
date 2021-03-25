package com.example.karatdatamobile.Services;

import com.example.karatdatamobile.Models.TcpConnectionSettings;
import com.intelligt.modbus.jlibmodbus.serial.SerialParameters;
import com.intelligt.modbus.jlibmodbus.serial.SerialPort;
import com.intelligt.modbus.jlibmodbus.serial.SerialPortFactoryTcpClient;
import com.intelligt.modbus.jlibmodbus.serial.SerialUtils;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

import net.wimpi.modbus.io.ModbusRTUTCPTransaction;
import net.wimpi.modbus.msg.ReadMultipleRegistersRequest;
import net.wimpi.modbus.msg.ReadMultipleRegistersResponse;
import net.wimpi.modbus.msg.WriteMultipleRegistersRequest;
import net.wimpi.modbus.net.RTUTCPMasterConnection;
import net.wimpi.modbus.procimg.SimpleRegister;

import static com.example.karatdatamobile.Services.BinaryDataParser.timeBytesToRegisters;

public class TcpConnectionProvider extends ModbusConnectionProvider<TcpConnectionSettings> {

    public TcpConnectionProvider(TcpConnectionSettings tcpConnectionSettings) {
        super(tcpConnectionSettings);
    }

    @Override
    protected SerialParameters getSerialParameters() throws Exception {
        TcpParameters tcpParameter = new TcpParameters();

        tcpParameter.setHost(settings.getIp());
        tcpParameter.setPort(settings.getPort());
        tcpParameter.setKeepAlive(true);

        SerialParameters serialParameter = new SerialParameters();
        serialParameter.setBaudRate(SerialPort.BaudRate.BAUD_RATE_9600);

        SerialUtils.setSerialPortFactory(new SerialPortFactoryTcpClient(tcpParameter));

        return serialParameter;
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
