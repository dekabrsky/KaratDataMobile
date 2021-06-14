package com.example.karatdatamobile.terminal

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import com.example.karatdatamobile.Enums.ArchiveType
import com.example.karatdatamobile.Enums.DataBlockType
import com.example.karatdatamobile.Interfaces.IReportBuilder
import com.example.karatdatamobile.Interfaces.ITemplateProvider
import com.example.karatdatamobile.Models.*
import com.example.karatdatamobile.Services.*
import com.example.karatdatamobile.utils.Lists.addToBegin
import moxy.MvpPresenter
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.set

class TerminalPresenter @Inject constructor(
    private val context: Context
) : MvpPresenter<TerminalView>() {
    private var dataBlocks = ArrayList<DataBlock>()
    private var messages = ArrayList<String>()
    private var adapter = TerminalAdapter(messages)
    private var parsedDataModel = ParsedData()

    @Inject
    lateinit var activity: Activity

    fun getRecyclerAdapter(): TerminalAdapter {
        return adapter
    }

    fun startReadData(appSettings: DeviceSettings, deviceDataQuery: DeviceDataQuery) {
        Thread {
            val connectionProvider = ConnectionProviderFactory.Create(appSettings)
            val binaryDataProvider = BinaryDataProvider(connectionProvider)
            binaryDataProvider.onReadBlock { dataBlock: DataBlock? ->
                readBlockEventListener(
                    dataBlock!!
                )
            }
            binaryDataProvider.onErrors { exception: Exception? ->
                if (exception != null) {
                    errorEventListener(
                        exception
                    )
                }
            }
            readBaseData(binaryDataProvider)
            readArchives(binaryDataProvider, deviceDataQuery)
            writeToUi("Чтение данных завершено")
            startCreateReport()
        }.start()
    }

    private fun errorEventListener(exception: Exception) {
        writeToUi("[Error]: " + exception.message)
    }


    private fun readBaseData(dataReader: BinaryDataProvider) {
        val baseDataBlocks = arrayOf(
            DataBlockInfoPresets.Model(),
            DataBlockInfoPresets.DateTime(),
            DataBlockInfoPresets.SerialNumber()
        )
        dataReader.read(baseDataBlocks)
    }

    private fun readArchives(dataReader: BinaryDataProvider, deviceDataQuery: DeviceDataQuery) {
        val archiveRegisters = ArchivesRegisters().nameToCode
        val config: ArchivesConfig = getArchiveConfig(dataReader)
        config.titles.addToBegin("Дата")
        parsedDataModel.setHeaders(config.titles)
        dataReader.write(deviceDataQuery.startDate) // todo
        val archives = HashMap<ArchiveType, java.util.ArrayList<DataBlock>>()
        for (archiveType in deviceDataQuery.archiveTypes) {
            val archiveData: ArrayList<DataBlock> = readArchiveByType(
                dataReader,
                archiveRegisters[archiveType] ?: 0
            )
            archives[archiveType] = archiveData
        }
        val parsedArchives: HashMap<ArchiveType, String> = parseArchives(archives, config)
        writeParsedArchivesToUi(parsedArchives)
    }

    private fun writeParsedArchivesToUi(parsedArchives: HashMap<ArchiveType, String>) {
        val sb = java.lang.StringBuilder()
        sb.append("PARSED ARCHIVES").append("\n")
        for (archiveType in parsedArchives.keys) {
            val str = String.format("[%s]: %s", archiveType.name, parsedArchives[archiveType])
            sb.append(str).append("\n")
        }
        writeToUi(sb.toString())
    }


    private fun getArchiveConfig(dataReader: BinaryDataProvider): ArchivesConfig {
        val dataBlock = dataReader.read(DataBlockInfoPresets.ArchivesConfig())
        return ArchivesConfig(BinaryDataParser.getHexContent(dataBlock.data))
    }

    private fun readArchiveByType(
        dataReader: BinaryDataProvider,
        type: Int
    ): ArrayList<DataBlock> {
        val result = java.util.ArrayList<DataBlock>()
        var counter = 0
        val next = type + 0x05
        while (true) {
            val offset = if (counter == 0) type else next
            val dataBlockInfo = DataBlockInfo(DataBlockType.ARCHIVE, offset, "F0".toInt(16) / 2)
            val dataBlock = dataReader.read(dataBlockInfo)
            if (BinaryDataParser.getHexContent(dataBlock.data)[0].toString() + BinaryDataParser.getHexContent(
                    dataBlock.data
                )[1] == "ff"
            ) break
            result.add(dataBlock)
            counter++
        }
        return result
    }

    private fun parseArchives(
        archives: HashMap<ArchiveType, java.util.ArrayList<DataBlock>>,
        config: ArchivesConfig
    ): HashMap<ArchiveType, String> {
        val result = HashMap<ArchiveType, String>()
        for (archiveType in archives.keys) {
            parsedDataModel.addArchiveHeader(archiveType.name)
            val parsedArchiveRows = BinaryDataParser.parseArchive(archives[archiveType], config)
            parsedDataModel.addArchiveRows(recordsToMatrix(parsedArchiveRows))
            result[archiveType] = recordsToString(parsedArchiveRows)
        }
        return result
    }

    private fun recordsToString(parsedArchiveRows: ArrayList<RecordRow>): String {
        val sb = StringBuilder()
        for (row in parsedArchiveRows) {
            for (data in row.rowArray)
                sb.append(data).append(", ")
            sb.append(";\n")
        }
        return sb.toString()
    }

    private fun recordsToMatrix(parsedArchiveRows: ArrayList<RecordRow>): List<List<String>> {
        val res: MutableList<List<String>> = mutableListOf()
        for (row in parsedArchiveRows) {
            val listRow = mutableListOf<String>()
            for (data in row.rowArray)
                listRow.add(data)
            res.add(listRow)
        }
        return res.toList()
    }

    private fun readBlockEventListener(dataBlock: DataBlock) {
        dataBlocks.add(dataBlock)
        writeDataBlockToUi(dataBlock)
    }

    private fun writeToUi(message: String) {
        activity.runOnUiThread(Runnable {
            messages.add(message)
            viewState.onDataChange()
        })
    }

    private fun writeDataBlockToUi(dataBlock: DataBlock) {
        val type = dataBlock.dataBlockInfo.dataBlockName
        val sb = StringBuilder()
        sb.append("[Type]: ").append(type.name).append("\n")
        sb.append("[Raw-data]: ").append(BinaryDataParser.getHexContent(dataBlock.data))
            .append("\n")
        val parsedData = BinaryDataParser.parse(dataBlock)
        when (type) {
            DataBlockType.MODEL -> parsedDataModel.setModel(parsedData)
            DataBlockType.SERIAL_NUMBER -> parsedDataModel.setSerNumber(parsedData)
            DataBlockType.DATE_TIME -> parsedDataModel.setSystemDate(parsedData)
        }
        if (parsedData != null) sb.append("[Parsed-data]: ").append(parsedData).append("\n")
        writeToUi(sb.toString())
    }

   fun startCreateReport() {
        Thread {
            val res = context.resources
            val cw = ContextWrapper(context)
            val directory = cw.getExternalFilesDir("Karat")
            //            String reportsDirectoryPath = directory.toString() + "/" + "reports";
//            String csvDirectoryPath = directory.toString() + "/" + "csv";
            val userData = HashMap<String, String>()
            userData["Имя"] = "Andrey"
            userData["Улица"] = "Gachi"
            userData["Дом"] = "100500"
            val parsedData = HashMap<String, List<List<String>>>()
            parsedData["DATA"] = parsedDataModel.archives
            val templateProvider: ITemplateProvider = TemplateProvider(res)
            val reportProvider: IReportBuilder =
                ReportBuilder(directory.toString(), directory.toString(), templateProvider)
            try {
                reportProvider.constructCsvReport("test.csv", parsedData)
                reportProvider.constructXlsxReport("test.xlsx", "test", userData, parsedData)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }.start()
    }
}