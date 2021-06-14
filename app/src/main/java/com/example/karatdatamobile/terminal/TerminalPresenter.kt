package com.example.karatdatamobile.terminal

import android.app.Activity
import com.example.karatdatamobile.Enums.ArchiveType
import com.example.karatdatamobile.Enums.DataBlockType
import com.example.karatdatamobile.Models.*
import com.example.karatdatamobile.Services.BinaryDataParser
import com.example.karatdatamobile.Services.BinaryDataProvider
import com.example.karatdatamobile.Services.ConnectionProviderFactory
import moxy.MvpPresenter
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList
import kotlin.collections.set

class TerminalPresenter @Inject constructor() : MvpPresenter<TerminalView>() {
    private var dataBlocks = ArrayList<DataBlock>()
    private var messages = ArrayList<String>()
    private var adapter = TerminalAdapter(messages)

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
            val parsedArchiveRows = BinaryDataParser.parseArchive(archives[archiveType], config)
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
        val sb = StringBuilder()
        sb.append("[Type]: ").append(dataBlock.dataBlockInfo.dataBlockName.name).append("\n")
        sb.append("[Raw-data]: ").append(BinaryDataParser.getHexContent(dataBlock.data))
            .append("\n")
        val parsedData = BinaryDataParser.parse(dataBlock)
        if (parsedData != null) sb.append("[Parsed-data]: ").append(parsedData).append("\n")
        writeToUi(sb.toString())
    }
}