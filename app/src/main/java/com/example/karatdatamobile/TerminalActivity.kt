//package com.example.karatdatamobile
//
//import android.content.ContextWrapper
//import android.content.SharedPreferences
//import android.hardware.usb.UsbManager
//import android.os.Bundle
//import android.util.Log
//import android.widget.ArrayAdapter
//import android.widget.ImageButton
//import android.widget.ImageView
//import android.widget.ListView
//import androidx.appcompat.app.AppCompatActivity
//import com.example.karatdatamobile.Enums.ArchiveType
//import com.example.karatdatamobile.Enums.ConnectionMode
//import com.example.karatdatamobile.Enums.DataBlockType
//import com.example.karatdatamobile.Interfaces.IReportBuilder
//import com.example.karatdatamobile.Interfaces.ITemplateProvider
//import com.example.karatdatamobile.Models.*
//import com.example.karatdatamobile.Services.*
//import java.util.*
//
//class TerminalActivity : AppCompatActivity() {
//    var image: ImageView? = null
//    var share: ImageButton? = null
//    var listView: ListView? = null
//    var sharedSettings: SharedPreferences? = null
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_tcp_terminal_old)
//        dataBlocks = ArrayList()
//        messages = ArrayList()
//        adapter = ArrayAdapter(this, R.layout.terminal_list_item, messages!!)
//        image = findViewById(R.id.image_load)
//        share = findViewById(R.id.share)
//        listView = findViewById(R.id.lw)
//        listView.setAdapter(adapter)
//        sharedSettings = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
//        val appSettings = appSettings
//        val deviceDataQuery = intent.getSerializableExtra("DeviceDataQuery") as DeviceDataQuery?
//        startCreateReport()
//        //        startReadData(appSettings, deviceDataQuery);
//    }
//
//    private fun startCreateReport() {
//        Thread {
//            val res = resources
//            val cw = ContextWrapper(applicationContext)
//            val directory = cw.getExternalFilesDir("Karat")
//            //            String reportsDirectoryPath = directory.toString() + "/" + "reports";
////            String csvDirectoryPath = directory.toString() + "/" + "csv";
//            val userData = HashMap<String, String>()
//            userData["Имя"] = "Andrey"
//            userData["Улица"] = "Gachi"
//            userData["Дом"] = "100500"
//            val parsedData = HashMap<String, Array<Array<String>>>()
//            parsedData["DATA"] = arrayOf(
//                arrayOf("Lol", "KeK", "HaHa", "Oh,no"),
//                arrayOf("100", "200", "300", "400"),
//                arrayOf("300", "200", "500", "400")
//            )
//            val templateProvider: ITemplateProvider = TemplateProvider(res)
//            val reportProvider: IReportBuilder =
//                ReportBuilder(directory.toString(), directory.toString(), templateProvider)
//            try {
//                reportProvider.constructCsvReport("test.csv", parsedData)
//                reportProvider.constructXlsxReport("test.xlsx", "test", userData, parsedData)
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
//        }.start()
//    }
//
//    private fun startReadData(appSettings: DeviceSettings, deviceDataQuery: DeviceDataQuery) {
//        Thread {
//            val connectionProvider = ConnectionProviderFactory.Create(appSettings)
//            val binaryDataProvider = BinaryDataProvider(connectionProvider)
//            binaryDataProvider.onReadBlock { dataBlock: DataBlock ->
//                readBlockEventListener(
//                    dataBlock
//                )
//            }
//            binaryDataProvider.onErrors { exception: Exception -> errorEventListener(exception) }
//            readBaseData(binaryDataProvider)
//            readArchives(binaryDataProvider, deviceDataQuery)
//            writeToUi("Чтение данных завершено")
//        }.start()
//    }
//
//    private fun readBaseData(dataReader: BinaryDataProvider) {
//        val baseDataBlocks = arrayOf(
//            DataBlockInfoPresets.Model(),
//            DataBlockInfoPresets.DateTime(),
//            DataBlockInfoPresets.SerialNumber()
//        )
//        dataReader.read(baseDataBlocks)
//    }
//
//    private fun readArchives(dataReader: BinaryDataProvider, deviceDataQuery: DeviceDataQuery) {
//        val archiveRegisters = ArchivesRegisters().nameToCode
//        val config = getArchiveConfig(dataReader)
//        dataReader.write(deviceDataQuery.startDate) // todo
//        val archives = HashMap<ArchiveType, ArrayList<DataBlock>>()
//        for (archiveType in deviceDataQuery.archiveTypes) {
//            val archiveData = readArchiveByType(dataReader, archiveRegisters[archiveType]!!)
//            archives[archiveType] = archiveData
//        }
//        val parsedArchives = parseArchives(archives, config)
//        writeParsedArchivesToUi(parsedArchives)
//    }
//
//    private fun getArchiveConfig(dataReader: BinaryDataProvider): ArchivesConfig {
//        val dataBlock = dataReader.read(DataBlockInfoPresets.ArchivesConfig())
//        return ArchivesConfig(BinaryDataParser.getHexContent(dataBlock.data))
//    }
//
//    private fun readArchiveByType(dataReader: BinaryDataProvider, type: Int): ArrayList<DataBlock> {
//        val result = ArrayList<DataBlock>()
//        var counter = 0
//        val next = type + 0x05
//        while (true) {
//            val offset = if (counter == 0) type else next
//            val dataBlockInfo = DataBlockInfo(DataBlockType.ARCHIVE, offset, "F0".toInt(16) / 2)
//            val dataBlock = dataReader.read(dataBlockInfo)
//            if (BinaryDataParser.getHexContent(dataBlock.data)[0].toString() + BinaryDataParser.getHexContent(
//                    dataBlock.data
//                )[1] == "ff"
//            ) break
//            result.add(dataBlock)
//            counter++
//        }
//        return result
//    }
//
//    private fun parseArchives(
//        archives: HashMap<ArchiveType, ArrayList<DataBlock>>,
//        config: ArchivesConfig
//    ): HashMap<ArchiveType, String> {
//        val result = HashMap<ArchiveType, String>()
//        for (archiveType in archives.keys) {
//            val parsedArchive: String = BinaryDataParser.parseArchive(archives[archiveType], config)
//            result[archiveType] = parsedArchive
//        }
//        return result
//    }
//
//    private fun readBlockEventListener(dataBlock: DataBlock) {
//        dataBlocks!!.add(dataBlock)
//        writeDataBlockToUi(dataBlock)
//    }
//
//    private fun errorEventListener(exception: Exception) {
//        writeToUi("[Error]: " + exception.message)
//    }
//
//    private fun writeToUi(message: String) {
//        runOnUiThread {
//            messages!!.add(message)
//            adapter!!.notifyDataSetChanged()
//        }
//    }
//
//    private fun writeDataBlockToUi(dataBlock: DataBlock) {
//        val sb = StringBuilder()
//        sb.append("[Type]: ").append(dataBlock.dataBlockInfo.dataBlockName.name).append("\n")
//        sb.append("[Raw-data]: ").append(BinaryDataParser.getHexContent(dataBlock.data))
//            .append("\n")
//        val parsedData = BinaryDataParser.parse(dataBlock)
//        if (parsedData != null) sb.append("[Parsed-data]: ").append(parsedData).append("\n")
//        writeToUi(sb.toString())
//    }
//
//    private fun writeParsedArchivesToUi(parsedArchives: HashMap<ArchiveType, String>) {
//        val sb = StringBuilder()
//        sb.append("PARSED ARCHIVES").append("\n")
//        for (archiveType in parsedArchives.keys) {
//            val str = String.format("[%s]: %s", archiveType.name, parsedArchives[archiveType])
//            sb.append(str).append("\n")
//        }
//        writeToUi(sb.toString())
//    }
//
//    private val appSettings: DeviceSettings
//        private get() {
//            val connectionMode = ConnectionMode.valueOf(
//                sharedSettings!!.getString("ConnectionMode", ConnectionMode.TCP.toString())!!
//            )
//            val ip = sharedSettings!!.getString("Ip", null)
//            val port = sharedSettings!!.getString("Port", null)
//            val address = sharedSettings!!.getString("Address", null)
//            val baudrate = sharedSettings!!.getInt("Baudrate", 19200)
//            return if (connectionMode == ConnectionMode.TCP) DeviceSettings(
//                connectionMode,
//                port,
//                ip,
//                address
//            ) else {
//                val usbManager = getSystemService(USB_SERVICE) as UsbManager
//                Log.d("Devices", usbManager.deviceList.toString())
//                DeviceSettings(connectionMode, baudrate, usbManager, address)
//            }
//        }
//
//    companion object {
//        var dataBlocks: ArrayList<DataBlock>? = null
//        var messages: ArrayList<String>? = null
//        var adapter: ArrayAdapter<String>? = null
//        private const val APP_PREFERENCES = "settingsMemory"
//    }
//}