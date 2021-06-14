package com.example.karatdatamobile.Services;

import com.example.karatdatamobile.Interfaces.IReportBuilder;
import com.example.karatdatamobile.Interfaces.ITemplateProvider;
import com.example.karatdatamobile.Models.XlsxDataCells;
import com.opencsv.CSVWriter;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReportBuilder implements IReportBuilder {

    private static final Pattern userDataPattern = Pattern.compile("^\\$\\(\\w+\\)$");
    private static final Pattern deviceDataPattern = Pattern.compile("^\\$\\[\\w+]$");

    private final String reportsDirectoryPath;
    private final String csvDirectoryPath;

    private final ITemplateProvider templateProvider;

    public ReportBuilder(
            String reportsDirectoryPath,
            String csvDirectoryPath,
            ITemplateProvider templateProvider) {

        this.reportsDirectoryPath = reportsDirectoryPath;
        this.csvDirectoryPath = csvDirectoryPath;
        this.templateProvider = templateProvider;
    }

    @Override
    public void constructCsvReport(
            String fileName,
            HashMap<String, String[][]> parsedDataBlocks) throws IOException {

        String filePath = csvDirectoryPath + "/" + fileName;

        CSVWriter csvWriter = new CSVWriter(new FileWriter(filePath),
                CSVWriter.DEFAULT_SEPARATOR,
                CSVWriter.NO_QUOTE_CHARACTER,
                CSVWriter.DEFAULT_ESCAPE_CHARACTER,
                CSVWriter.DEFAULT_LINE_END);

        for (Map.Entry<String, String[][]> entry : parsedDataBlocks.entrySet()) {
            csvWriter.writeNext(new String[]{entry.getKey()});
            for (int i = 0; i < entry.getValue().length; i++) {
                csvWriter.writeNext(entry.getValue()[i]);
            }
        }

        csvWriter.close();
    }

    @Override
    public void constructXlsxReport(
            String fileName,
            String templateName,
            HashMap<String, String> userData,
            HashMap<String, String[][]> parsedDataBlocks) throws IOException {

        String filePath = reportsDirectoryPath + "/" + fileName;
        InputStream emptyTemplate = templateProvider.createEmptyReport(templateName);

        Workbook workbook = new XSSFWorkbook(emptyTemplate);
        Sheet sheet = workbook.getSheetAt(0);

        XlsxDataCells dataCells = getDataCells(sheet);

        writeUserData(sheet, dataCells.getUserDataCells(), userData);
        writeDeviceData(sheet, dataCells.getDeviceDataCells(), parsedDataBlocks);

        workbook.write(new FileOutputStream(filePath));
        workbook.close();
    }

    private XlsxDataCells getDataCells(Sheet sheet) {
        HashMap<String, CellAddress> userDataCells = new HashMap<>();
        HashMap<String, CellAddress> deviceDataCells = new HashMap<>();

        for (Row row : sheet) {
            for (Cell cell : row) {

                String cellValue = cell.getStringCellValue();

                Matcher userDataMatcher = userDataPattern.matcher(cellValue);
                if (userDataMatcher.find()) {
                    String paramName = userDataMatcher.group()
                            .replace("$", "")
                            .replace("(", "")
                            .replace(")", "");
                    userDataCells.put(paramName, cell.getAddress());
                    continue;
                }

                Matcher deviceDataMatcher = deviceDataPattern.matcher(cellValue);
                if (deviceDataMatcher.find()) {
                    String paramName = deviceDataMatcher.group()
                            .replace("$", "")
                            .replace("[", "")
                            .replace("]", "");
                    deviceDataCells.put(paramName, cell.getAddress());
                }
            }
        }

        return new XlsxDataCells(userDataCells, deviceDataCells);
    }

    private void writeUserData(
            Sheet sheet,
            HashMap<String, CellAddress> userDataCells,
            HashMap<String, String> userData) {

        for (Map.Entry<String, CellAddress> entry : userDataCells.entrySet()) {
            if (userData.containsKey(entry.getKey())) {
                CellAddress cellAddress = entry.getValue();
                sheet
                        .getRow(cellAddress.getRow())
                        .getCell(cellAddress.getColumn())
                        .setCellValue(userData.get(entry.getKey()));
            }
        }
    }

    private void writeDeviceData(
            Sheet sheet,
            HashMap<String, CellAddress> deviceDataCells,
            HashMap<String, String[][]> parsedDataBlocks) {

        for (Map.Entry<String, CellAddress> entry : deviceDataCells.entrySet()) {

            String paramName = entry.getKey();

            if (parsedDataBlocks.containsKey(paramName)) {
                String[][] data = parsedDataBlocks.get(paramName);

                int rowId = entry.getValue().getRow();
                int columnId = entry.getValue().getColumn();

                sheet.shiftRows(rowId, sheet.getLastRowNum() + 1, data.length - 1, true, true);

                for (int i = 0; i < data.length; i++) {
                    Row row = sheet.createRow(i + rowId);
                    for (int j = 0; j < data[i].length; j++) {
                        Cell rowCell = row.createCell(j + columnId);
                        rowCell.setCellValue(data[i][j]);
                    }
                }
            }
        }
    }
}
