package com.example.karatdatamobile;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.karatdatamobile.Interfaces.IConnectionProvider;
import com.example.karatdatamobile.Models.AppSettings;
import com.example.karatdatamobile.Models.DataBlock;
import com.example.karatdatamobile.Models.DataBlockInfo;
import com.example.karatdatamobile.Models.DataBlockInfoPresets;
import com.example.karatdatamobile.Models.DeviceDataQuery;
import com.example.karatdatamobile.Services.ConnectionProviderFactory;
import com.example.karatdatamobile.Services.Parser.Parser;
import com.example.karatdatamobile.Services.Reader.DataReader;

import java.util.ArrayList;
import java.util.Arrays;

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
            DataReader dataReader = new DataReader(connectionProvider);

            dataReader.onReadBlock(this::readBlockEventListener);
            dataReader.onErrors(this::errorEventListener);

            readBaseData(dataReader);
            readArchives(dataReader);
        }).start();
    }

    private void readBaseData(DataReader dataReader) {
        DataBlockInfo[] baseDataBlocks = new DataBlockInfo[]{
                DataBlockInfoPresets.Model(),
                DataBlockInfoPresets.DateTime(),
                DataBlockInfoPresets.SerialNumber(),
                DataBlockInfoPresets.ArchivesConfig(),
        };

        dataReader.read(baseDataBlocks);
    }

    private void readArchives(DataReader dataReader) {
        writeToUi("Start read archives");
    }

    private void readBlockEventListener(DataBlock dataBlock) {
        dataBlocks.add(dataBlock);
        String dataString = Parser.parse(dataBlock);

        writeToUi(dataBlock.getDataBlockInfo().getDataBlockName() + " - " + Arrays.toString(dataBlock.getData()));
        writeToUi(dataString);
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
