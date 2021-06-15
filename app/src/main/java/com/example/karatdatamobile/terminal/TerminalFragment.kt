package com.example.karatdatamobile.terminal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.karatdatamobile.models.DeviceDataQuery
import com.example.karatdatamobile.models.DeviceSettings
import com.example.karatdatamobile.R
import kotlinx.android.synthetic.main.fragment_terminal.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Toothpick
import javax.inject.Inject

class TerminalFragment @Inject constructor(): MvpAppCompatFragment(), TerminalView {

    private lateinit var deviceDataQuery: DeviceDataQuery
    private lateinit var deviceSettings: DeviceSettings
    private lateinit var fileName: String

    @InjectPresenter
    lateinit var presenter: TerminalPresenter

    @ProvidePresenter
    fun providePresenter(): TerminalPresenter =
        Toothpick.openRootScope().getInstance(TerminalPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            deviceDataQuery = it.getSerializable(key[0]) as DeviceDataQuery
            deviceSettings = it.getSerializable(key[1]) as DeviceSettings
            fileName = it.getSerializable(key[2]).toString()
        }
        System.setProperty("org.apache.poi.javax.xml.stream.XMLInputFactory",
            "com.fasterxml.aalto.stax.InputFactoryImpl")
        System.setProperty("org.apache.poi.javax.xml.stream.XMLOutputFactory",
            "com.fasterxml.aalto.stax.OutputFactoryImpl")
        System.setProperty("org.apache.poi.javax.xml.stream.XMLEventFactory",
            "com.fasterxml.aalto.stax.EventFactoryImpl")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_terminal, container, false)
    }

    private fun init(){
        fab_save.setOnClickListener { presenter.makeXLS() }
        fab_see.setOnClickListener { presenter.toReportsList() }
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = presenter.getRecyclerAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        startReadData()
    }

    override fun onDataChange(){
        recycler.adapter?.notifyDataSetChanged()
        recycler.adapter?.let {
            recycler.smoothScrollToPosition(it.itemCount)
        }
    }

    private fun startReadData() {
        deactivateButtons()
        showLoadSign()
        presenter.startReadData(
            deviceSettings,
            deviceDataQuery,
            fileName
        )
    }

    private fun deactivateButtons(){
        fab_save.isEnabled = false
        fab_see.isEnabled = false
    }

    override fun activateButtons() {
        fab_save.isEnabled = true
        fab_see.isEnabled = true
    }

    override fun showLoadSign() {
        loadSign.visibility = View.VISIBLE
    }

    override fun hideLoadSign() {
        loadSign.visibility = View.INVISIBLE
    }

    companion object {
        private val key = arrayListOf("DeviceDataQuery", "DeviceSettings", "Filename")

        @JvmStatic
        fun newInstance(query: DeviceDataQuery, settings: DeviceSettings, fileName: String) =
            TerminalFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(key[0], query)
                    putSerializable(key[1], settings)
                    putString(key[2], fileName)
            }
        }
    }
}