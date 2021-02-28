package com.example.karatdatamobile.Providers;

import com.example.karatdatamobile.Models.TcpConnectionSettings;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.msg.request.ReadHoldingRegistersRequest;
import com.intelligt.modbus.jlibmodbus.msg.response.ReadHoldingRegistersResponse;
import com.intelligt.modbus.jlibmodbus.serial.SerialParameters;
import com.intelligt.modbus.jlibmodbus.serial.SerialPort;
import com.intelligt.modbus.jlibmodbus.serial.SerialPortException;
import com.intelligt.modbus.jlibmodbus.serial.SerialPortFactoryTcpClient;
import com.intelligt.modbus.jlibmodbus.serial.SerialPortFactoryTcpServer;
import com.intelligt.modbus.jlibmodbus.serial.SerialUtils;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TcpConnectionProvider extends ModbusConnectionProvider<TcpConnectionSettings> {

    public TcpConnectionProvider(TcpConnectionSettings tcpConnectionSettings) {
        super(tcpConnectionSettings);
    }

    @Override
    protected SerialParameters getSerialParameters() throws Exception {
        TcpParameters tcpParameter = new TcpParameters();

        tcpParameter.setHost(InetAddress.getByName(settings.getIp()));
        tcpParameter.setPort(Integer.parseInt(settings.getPort()));
        tcpParameter.setKeepAlive(true);

        SerialParameters serialParameter = new SerialParameters();
        serialParameter.setBaudRate(SerialPort.BaudRate.BAUD_RATE_9600);

        SerialUtils.setSerialPortFactory(new SerialPortFactoryTcpClient(tcpParameter));

        return serialParameter;
    }

    @Override
    public int[] read(int offset, int quantity) throws Exception {
        ReadHoldingRegistersRequest readRequest = new ReadHoldingRegistersRequest();
        readRequest.setServerAddress(settings.getSlaveId());
        readRequest.setStartAddress(offset);
        readRequest.setQuantity(quantity);

        master.processRequest(readRequest);

        ReadHoldingRegistersResponse response = (ReadHoldingRegistersResponse) readRequest.getResponse();

        return response.getHoldingRegisters().getRegisters();
    }
}
