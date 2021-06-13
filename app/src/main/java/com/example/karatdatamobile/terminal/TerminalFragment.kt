package com.example.karatdatamobile.terminal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.karatdatamobile.Models.DeviceDataQuery
import com.example.karatdatamobile.Models.DeviceSettings
import com.example.karatdatamobile.R
import kotlinx.android.synthetic.main.fragment_terminal.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import javax.inject.Inject

class TerminalFragment @Inject constructor(): MvpAppCompatFragment(), TerminalView {

    private lateinit var deviceDataQuery: DeviceDataQuery
    private lateinit var deviceSettings: DeviceSettings

    @InjectPresenter
    lateinit var presenter: TerminalPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            deviceDataQuery = it.getSerializable(key[0]) as DeviceDataQuery
            deviceSettings = it.getSerializable(key[1]) as DeviceSettings
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_terminal, container, false)
        startReadData()
        recycler.adapter = presenter.getRecyclerAdapter()
    }

    private fun startReadData() {
        presenter.startReadData(deviceSettings, deviceDataQuery)
    }

    companion object {

        private val key = arrayListOf<String>("DeviceDataQuery", "DeviceSettings")


        @JvmStatic
        fun newInstance(param1: DeviceDataQuery, param2: DeviceSettings) =
            TerminalFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(key[0], param1)
                    putSerializable(key[1], param2)
                }
            }
    }
}