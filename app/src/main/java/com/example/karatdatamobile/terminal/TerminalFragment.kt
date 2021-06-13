package com.example.karatdatamobile.terminal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.karatdatamobile.Models.DeviceDataQuery
import com.example.karatdatamobile.Models.DeviceSettings
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
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_terminal, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        startReadData()
        recycler.layoutManager = LinearLayoutManager(context)
        recycler.adapter = presenter.getRecyclerAdapter()
    }

    override fun onDataChange(){
        recycler.adapter?.notifyDataSetChanged()
    }

    private fun startReadData() {
        presenter.startReadData(deviceSettings, deviceDataQuery)
    }

    companion object {

        private val key = arrayListOf("DeviceDataQuery", "DeviceSettings")


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