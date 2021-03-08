package com.example.karatdatamobile.Services;

import android.util.Log;

import com.example.karatdatamobile.Models.ArchivesConfig;
import com.example.karatdatamobile.Models.DataBlock;
import com.example.karatdatamobile.Models.DataBlockInfo;
import com.example.karatdatamobile.Models.RecordRow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class BinaryDataParser {
    public static String parse(DataBlock dataBlock) {

        DataBlockInfo dataBlockInfo = dataBlock.getDataBlockInfo();

        switch (dataBlockInfo.getDataBlockName()) {
            case MODEL:
                return printModel(dataBlock.getData());
            case DATE_TIME:
                return printDateTime(dataBlock.getData());
            case SERIAL_NUMBER:
                return printSerNumber(dataBlock.getData());
            default:
                return null;
        }
    }

    public static String parseArchive(ArrayList<DataBlock> dataBlocks, ArchivesConfig config) {
        StringBuilder sb = new StringBuilder();

        for (DataBlock dataBlock : dataBlocks) {
            RecordRow recordRow = new RecordRow(config, getHexContent(dataBlock.getData()));
            sb.append(Arrays.asList(recordRow.getRowArray()).toString()).append("\n");
        }

        return sb.toString();
    }

    private static String printSerNumber(int[] data) {
        String hexData = getHexContent(data);
        String[] hdArray = hexData.split(" ");
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < 9; i++) {
            sb.append(Integer.parseInt(hdArray[i]) - 30);
        }
        Log.d("Сер. номер", sb.toString());
        return sb.toString();
    }

    public static String getHexContent(int[] data) {
        StringBuilder sb = new StringBuilder();

        for (int b : data) {
            String hex = Integer.toHexString(b);
            //Log.d("hex data", hex);
            if (hex.length() == 3) {
                sb.append("0").append(hex.charAt(0)).append(" ");
                sb.append(hex.charAt(1)).append(hex.charAt(2)).append(" ");
            } else if (hex.length() == 2) {
                sb.append("0").append("0").append(" ");
                sb.append(hex.charAt(0)).append(hex.charAt(1)).append(" ");
            } else if (hex.length() == 1) {
                sb.append("0").append("0").append(" ");
                sb.append("0").append(hex.charAt(0)).append(" ");
            } else if (hex.length() == 0) {
                sb.append("0").append("0").append(" ");
                sb.append("0").append("0").append(" ");
            } else {
                sb.append(hex.charAt(0)).append(hex.charAt(1)).append(" ");
                sb.append(hex.charAt(2)).append(hex.charAt(3)).append(" ");
            }
        }
        return sb.toString();
    }

    private static String printDateTime(int[] data) {
        int year = getInt(data[3]);
        Log.d("Year", String.valueOf(year));
        int[] weekAndMonth = getTwoInt(data[2]);
        Log.d("Месяц и день недели", weekAndMonth[0] + " " + weekAndMonth[1]);

        int[] dateAndHour = getTwoInt(data[1]);
        Log.d("День месяца и час", dateAndHour[0] + " " + dateAndHour[1]);

        int[] minAndSeconds = getTwoInt(data[0]);
        Log.d("Минуты и секунды", minAndSeconds[0] + " " + minAndSeconds[1]);

        Calendar c = Calendar.getInstance();
        c.set(year, weekAndMonth[0] - 1, dateAndHour[0], dateAndHour[1], minAndSeconds[0], minAndSeconds[1]);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd 'at' HH:mm:ss");
        Log.d("Дата и время", dateFormat.format(c.getTime()));
        return dateFormat.format(c.getTime());
    }

    private static int getInt(int bytes) {
        String str = Integer.toHexString(bytes);
        return Integer.parseInt(String.valueOf(str.charAt(2)) + str.charAt(3) + str.charAt(0) + str.charAt(1), 16);
    }

    private static int[] getTwoInt(int bytes) {
        String str = Integer.toHexString(bytes);
        if (str.length() == 3)
            str = "0" + str;
        int[] result = new int[2];
        result[0] = Integer.parseInt(String.valueOf(str.charAt(2)) + str.charAt(3), 16);
        result[1] = Integer.parseInt(String.valueOf(str.charAt(0)) + str.charAt(1), 16);
        return result;
    }

    private static String printModel(int[] data) {
        String t = Integer.toHexString(data[0]);
        return t.charAt(2) +
                String.valueOf(t.charAt(3)) +
                t.charAt(0) +
                t.charAt(1);
    }
}
