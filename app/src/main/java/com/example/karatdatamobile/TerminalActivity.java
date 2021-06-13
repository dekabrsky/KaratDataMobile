package com.example.karatdatamobile;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.karatdatamobile.Enums.ArchiveType;
import com.example.karatdatamobile.Enums.ConnectionMode;
import com.example.karatdatamobile.Enums.DataBlockType;
import com.example.karatdatamobile.Interfaces.IConnectionProvider;
import com.example.karatdatamobile.Interfaces.IReportBuilder;
import com.example.karatdatamobile.Interfaces.ITemplateProvider;
import com.example.karatdatamobile.Models.AppSettings;
import com.example.karatdatamobile.Models.ArchivesConfig;
import com.example.karatdatamobile.Models.ArchivesRegisters;
import com.example.karatdatamobile.Models.DataBlock;
import com.example.karatdatamobile.Models.DataBlockInfo;
import com.example.karatdatamobile.Models.DataBlockInfoPresets;
import com.example.karatdatamobile.Models.DeviceDataQuery;
import com.example.karatdatamobile.Services.BinaryDataParser;
import com.example.karatdatamobile.Services.BinaryDataProvider;
import com.example.karatdatamobile.Services.ConnectionProviderFactory;
import com.example.karatdatamobile.Services.ReportBuilder;
import com.example.karatdatamobile.Services.TemplateProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import static com.example.karatdatamobile.Services.BinaryDataParser.getHexContent;

public class TerminalActivity extends AppCompatActivity {

    static ArrayList<DataBlock> dataBlocks;
    static ArrayList<String> messages;
    static ArrayAdapter<String> adapter;

    ImageView image;
    ImageButton share;
    ListView listView;

    SharedPreferences sharedSettings;
    private static final String APP_PREFERENCES = "settingsMemory";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp_terminal_old);

        dataBlocks = new ArrayList<>();
        messages = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.terminal_list_item, messages);

        image = findViewById(R.id.image_load);
        share = findViewById(R.id.share);
        listView = findViewById(R.id.lw);

        listView.setAdapter(adapter);

        sharedSettings = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);
        AppSettings appSettings = getAppSettings();
        DeviceDataQuery deviceDataQuery = (DeviceDataQuery) getIntent().getSerializableExtra("DeviceDataQuery");

        startCreateReport();
//        startReadData(appSettings, deviceDataQuery);
    }

    private void startCreateReport() {
        new Thread(() -> {

            Resources res = getResources();
            ContextWrapper cw = new ContextWrapper(getApplicationContext());

            File directory = cw.getExternalFilesDir("Karat");
//            String reportsDirectoryPath = directory.toString() + "/" + "reports";
//            String csvDirectoryPath = directory.toString() + "/" + "csv";

            HashMap<String, String> userData = new HashMap<>();
            userData.put("Имя", "Andrey");
            userData.put("Улица", "Gachi");
            userData.put("Дом", "100500");

            HashMap<String, String[][]> parsedData = new HashMap<>();
            parsedData.put("DATA", new String[][]{
                    new String[]{"Lol", "KeK", "HaHa", "Oh,no"},
                    new String[]{"100", "200", "300", "400"},
                    new String[]{"300", "200", "500", "400"},
            });

            ITemplateProvider templateProvider = new TemplateProvider(res);
            IReportBuilder reportProvider = new ReportBuilder(directory.toString(), directory.toString(), templateProvider);

            try {
                reportProvider.constructCsvReport("test.csv", parsedData);
                reportProvider.constructXlsxReport("test.xlsx", "test", userData, parsedData);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }).start();
    }

    private void startReadData(AppSettings appSettings, DeviceDataQuery deviceDataQuery) {
        new Thread(() -> {
            IConnectionProvider connectionProvider = ConnectionProviderFactory.Create(appSettings);
            BinaryDataProvider binaryDataProvider = new BinaryDataProvider(connectionProvider);

            binaryDataProvider.onReadBlock(this::readBlockEventListener);
            binaryDataProvider.onErrors(this::errorEventListener);

            readBaseData(binaryDataProvider);
            readArchives(binaryDataProvider, deviceDataQuery);

            writeToUi("Чтение данных завершено");
        }).start();
    }


    private void readBaseData(BinaryDataProvider dataReader) {
        DataBlockInfo[] baseDataBlocks = new DataBlockInfo[]{
                DataBlockInfoPresets.Model(),
                DataBlockInfoPresets.DateTime(),
                DataBlockInfoPresets.SerialNumber(),
        };

        dataReader.read(baseDataBlocks);
    }

    private void readArchives(BinaryDataProvider dataReader, DeviceDataQuery deviceDataQuery) {
        HashMap<ArchiveType, Integer> archiveRegisters = new ArchivesRegisters().getNameToCode();
        ArchivesConfig config = getArchiveConfig(dataReader);

        dataReader.write(deviceDataQuery.getStartDate()); // todo

        HashMap<ArchiveType, ArrayList<DataBlock>> archives = new HashMap<>();
        for (ArchiveType archiveType : deviceDataQuery.getArchiveTypes()) {
            ArrayList<DataBlock> archiveData = readArchiveByType(dataReader, archiveRegisters.get(archiveType));
            archives.put(archiveType, archiveData);
        }

        HashMap<ArchiveType, String> parsedArchives = parseArchives(archives, config);
        writeParsedArchivesToUi(parsedArchives);
    }

    private ArchivesConfig getArchiveConfig(BinaryDataProvider dataReader) {
        DataBlock dataBlock = dataReader.read(DataBlockInfoPresets.ArchivesConfig());
        return new ArchivesConfig(getHexContent(dataBlock.getData()));
    }

    private ArrayList<DataBlock> readArchiveByType(BinaryDataProvider dataReader, int type) {
        ArrayList<DataBlock> result = new ArrayList<>();

        int counter = 0;
        int next = type + 0x05;

        while (true) {
            int offset = counter == 0 ? type : next;

            DataBlockInfo dataBlockInfo = new DataBlockInfo(DataBlockType.ARCHIVE, offset, Integer.parseInt("F0", 16) / 2);
            DataBlock dataBlock = dataReader.read(dataBlockInfo);

            if ((String.valueOf(getHexContent(dataBlock.getData()).charAt(0)) + getHexContent(dataBlock.getData()).charAt(1)).equals("ff"))
                break;

            result.add(dataBlock);

            counter++;
        }

        return result;
    }

    private HashMap<ArchiveType, String> parseArchives(HashMap<ArchiveType, ArrayList<DataBlock>> archives, ArchivesConfig config) {
        HashMap<ArchiveType, String> result = new HashMap<>();

        for (ArchiveType archiveType : archives.keySet()) {
            String parsedArchive = BinaryDataParser.parseArchive(archives.get(archiveType), config);
            result.put(archiveType, parsedArchive);
        }

        return result;
    }

    private void readBlockEventListener(DataBlock dataBlock) {
        dataBlocks.add(dataBlock);

        writeDataBlockToUi(dataBlock);
    }

    private void errorEventListener(Exception exception) {
        writeToUi("[Error]: " + exception.getMessage());
    }

    private void writeToUi(String message) {
        TerminalActivity.this.runOnUiThread(() -> {
            messages.add(message);
            adapter.notifyDataSetChanged();
        });
    }

    private void writeDataBlockToUi(DataBlock dataBlock) {
        StringBuilder sb = new StringBuilder();
        sb.append("[Type]: ").append(dataBlock.getDataBlockInfo().getDataBlockName().name()).append("\n");
        sb.append("[Raw-data]: ").append(getHexContent(dataBlock.getData())).append("\n");

        String parsedData = BinaryDataParser.parse(dataBlock);
        if (parsedData != null)
            sb.append("[Parsed-data]: ").append(parsedData).append("\n");

        writeToUi(sb.toString());
    }

    private void writeParsedArchivesToUi(HashMap<ArchiveType, String> parsedArchives) {
        StringBuilder sb = new StringBuilder();
        sb.append("PARSED ARCHIVES").append("\n");

        for (ArchiveType archiveType : parsedArchives.keySet()) {
            String str = String.format("[%s]: %s", archiveType.name(), parsedArchives.get(archiveType));
            sb.append(str).append("\n");
        }

        writeToUi(sb.toString());
    }

    private AppSettings getAppSettings() {
        ConnectionMode connectionMode = ConnectionMode.valueOf(
                sharedSettings.getString("ConnectionMode", ConnectionMode.TCP.toString()));
        String ip = sharedSettings.getString("Ip", null);
        String port = sharedSettings.getString("Port", null);
        String address = sharedSettings.getString("Address", null);
        int baudrate = sharedSettings.getInt("Baudrate", 19200);

        if (connectionMode.equals(ConnectionMode.TCP))
            return new AppSettings(connectionMode, port, ip, address);
        else {
            UsbManager usbManager = (UsbManager) getSystemService(Context.USB_SERVICE);
            Log.d("Devices", String.valueOf(usbManager.getDeviceList()));
            return new AppSettings(connectionMode, baudrate, usbManager, address);
        }
    }
}
