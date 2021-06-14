package com.example.karatdatamobile.Interfaces;

import java.io.IOException;
import java.util.HashMap;

public interface IReportBuilder {

    void constructCsvReport(
            String fileName,
            HashMap<String, String[][]> parsedDataBlocks) throws IOException;

    void constructXlsxReport(
            String fileName,
            String templateName,
            HashMap<String, String> userData,
            HashMap<String, String[][]> parsedDataBlocks) throws IOException;
}
