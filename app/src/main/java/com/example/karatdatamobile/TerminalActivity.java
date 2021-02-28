package com.example.karatdatamobile;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.karatdatamobile.Enums.ArchiveType;
import com.example.karatdatamobile.Enums.DataBlockType;
import com.example.karatdatamobile.Interfaces.IConnectionProvider;
import com.example.karatdatamobile.Models.AppSettings;
import com.example.karatdatamobile.Models.ArchivesRegisters;
import com.example.karatdatamobile.Models.DataBlock;
import com.example.karatdatamobile.Models.DataBlockInfo;
import com.example.karatdatamobile.Models.DataBlockInfoPresets;
import com.example.karatdatamobile.Models.DeviceDataQuery;
import com.example.karatdatamobile.Services.BinaryDataProvider;
import com.example.karatdatamobile.Services.ConnectionProviderFactory;
import com.example.karatdatamobile.Services.BinaryDataParser;

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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tcp_terminal);

        dataBlocks = new ArrayList<>();
        messages = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.list_item, messages);

        image = findViewById(R.id.image_load);
        share = findViewById(R.id.share);
        listView = findViewById(R.id.lw);

        listView.setAdapter(adapter);

        AppSettings appSettings = (AppSettings) getIntent().getSerializableExtra("AppSettings");
        DeviceDataQuery deviceDataQuery = (DeviceDataQuery) getIntent().getSerializableExtra("DeviceDataQuery");

        startReadData(appSettings, deviceDataQuery);
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

    private void readArchivesConfig(BinaryDataProvider dataReader) {
        DataBlockInfo[] baseDataBlocks = new DataBlockInfo[]{
                DataBlockInfoPresets.ArchivesConfig(),
        };

        dataReader.read(baseDataBlocks);
    }

    private void readArchives(BinaryDataProvider dataReader, DeviceDataQuery deviceDataQuery) {
        HashMap<ArchiveType, Integer> archiveRegisters = new ArchivesRegisters().getNameToCode();

        dataReader.write(deviceDataQuery.getStartDate()); // todo

        for (ArchiveType archiveType : deviceDataQuery.getArchiveTypes()) {
            readArchiveByType(dataReader, archiveRegisters.get(archiveType));
        }
    }

    private void readArchiveByType(BinaryDataProvider dataReader, int type) {
        int counter = 0;
        int next = type + 0x05;
        while (true) {
            int offset = counter == 0 ? type : next;

            DataBlockInfo dataBlockInfo = new DataBlockInfo(DataBlockType.ARCHIVE, offset, Integer.parseInt("F0", 16) / 2);
            DataBlock dataBlock = dataReader.read(dataBlockInfo);

            if ((String.valueOf(getHexContent(dataBlock.getData()).charAt(0)) + getHexContent(dataBlock.getData()).charAt(1)).equals("ff"))
                break;

            counter++;
        }
    }

    private void readBlockEventListener(DataBlock dataBlock) {
        dataBlocks.add(dataBlock);

        String dataString = BinaryDataParser.parse(dataBlock);

        writeToUi(dataBlock.getDataBlockInfo().getDataBlockName().name());
        writeToUi("[Raw-data]: " + getHexContent(dataBlock.getData()));
        writeToUi("[Parsed-data]: " + dataString);
        writeToUi("------");
    }

    private void errorEventListener(Exception exception) {
        writeToUi(exception.getMessage());
    }

    private void writeToUi(String message) {
        TerminalActivity.this.runOnUiThread(() -> {
            messages.add(message);
            adapter.notifyDataSetChanged();
        });
    }
}
