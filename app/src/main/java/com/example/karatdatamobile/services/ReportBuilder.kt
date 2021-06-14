package com.example.karatdatamobile.services

import com.example.karatdatamobile.interfaces.IReportBuilder
import com.example.karatdatamobile.interfaces.ITemplateProvider
import com.example.karatdatamobile.models.XlsxDataCells
import com.opencsv.CSVWriter
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.util.CellAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.FileOutputStream
import java.io.FileWriter
import java.io.IOException
import java.util.*
import java.util.regex.Pattern

class ReportBuilder(
    private val reportsDirectoryPath: String,
    private val csvDirectoryPath: String,
    private val templateProvider: ITemplateProvider
) : IReportBuilder {
    @Throws(IOException::class)
    override fun constructCsvReport(
        fileName: String,
        parsedDataBlocks: HashMap<String, List<List<String>>>
    ) {
        val filePath = "$csvDirectoryPath/$fileName"
        val csvWriter = CSVWriter(
            FileWriter(filePath),
            CSVWriter.DEFAULT_SEPARATOR,
            CSVWriter.NO_QUOTE_CHARACTER,
            CSVWriter.DEFAULT_ESCAPE_CHARACTER,
            CSVWriter.DEFAULT_LINE_END
        )
        for ((key, value) in parsedDataBlocks) {
            csvWriter.writeNext(arrayOf(key))
            for (i in value.indices) {
                csvWriter.writeNext(value[i].toTypedArray())
            }
        }
        csvWriter.close()
    }

    @Throws(IOException::class)
    override fun constructXlsxReport(
        fileName: String,
        templateName: String,
        userData: HashMap<String, String>,
        parsedDataBlocks: HashMap<String, List<List<String>>>
    ) {
        val filePath = "$reportsDirectoryPath/$fileName"
        val emptyTemplate = templateProvider.createEmptyReport(templateName)
        val workbook: Workbook = XSSFWorkbook(emptyTemplate)
        val sheet = workbook.getSheetAt(0)
        val dataCells = getDataCells(sheet)
        writeUserData(sheet, dataCells.userDataCells, userData)
        writeDeviceData(sheet, dataCells.deviceDataCells, parsedDataBlocks)
        workbook.write(FileOutputStream(filePath))
        workbook.close()
    }

    private fun getDataCells(sheet: Sheet): XlsxDataCells {
        val userDataCells = HashMap<String, CellAddress>()
        val deviceDataCells = HashMap<String, CellAddress>()
        for (row in sheet) {
            for (cell in row) {
                val cellValue = cell.stringCellValue
                val userDataMatcher = userDataPattern.matcher(cellValue)
                if (userDataMatcher.find()) {
                    val paramName = userDataMatcher.group()
                        .replace("$", "")
                        .replace("(", "")
                        .replace(")", "")
                    userDataCells[paramName] = cell.address
                    continue
                }
                val deviceDataMatcher = deviceDataPattern.matcher(cellValue)
                if (deviceDataMatcher.find()) {
                    val paramName = deviceDataMatcher.group()
                        .replace("$", "")
                        .replace("[", "")
                        .replace("]", "")
                    deviceDataCells[paramName] = cell.address
                }
            }
        }
        return XlsxDataCells(userDataCells, deviceDataCells)
    }

    private fun writeUserData(
        sheet: Sheet,
        userDataCells: HashMap<String, CellAddress>,
        userData: HashMap<String, String>
    ) {
        for ((key, cellAddress) in userDataCells) {
            if (userData.containsKey(key)) {
                sheet
                    .getRow(cellAddress.row)
                    .getCell(cellAddress.column)
                    .setCellValue(userData[key])
            }
        }
    }

    private fun writeDeviceData(
        sheet: Sheet,
        deviceDataCells: HashMap<String, CellAddress>,
        parsedDataBlocks: HashMap<String, List<List<String>>>
    ) {
        for ((paramName, value) in deviceDataCells) {
            if (parsedDataBlocks.containsKey(paramName)) {
                val data = parsedDataBlocks[paramName]
                val rowId = value.row
                val columnId = value.column
                assert(data != null)
                if (data!!.size > 1)
                    sheet.shiftRows(rowId, sheet.lastRowNum + 1, data!!.size - 1, true, true)
                for (i in data.indices) {
                    val row = sheet.createRow(i + rowId)
                    for (j in data[i].indices) {
                        val rowCell = row.createCell(j + columnId)
                        rowCell.setCellValue(data[i][j])
                    }
                }
            }
        }
    }

    companion object {
        private val userDataPattern = Pattern.compile("^\\$\\(\\w+\\)$")
        private val deviceDataPattern = Pattern.compile("^\\$\\[\\w+]$")
    }
}