package com.example.karatdatamobile.terminal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.karatdatamobile.Models.DeviceDataQuery
import com.example.karatdatamobile.R
import kotlinx.android.synthetic.main.fragment_terminal.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter

class TerminalFragment : MvpAppCompatFragment(), TerminalView {

    private lateinit var deviceDataQuery: DeviceDataQuery

    @InjectPresenter
    lateinit var presenter: TerminalPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            deviceDataQuery = it.getSerializable(key) as DeviceDataQuery
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

    fun startReadData(){
        presenter.startReadData(deviceDataQuery)
    }

    companion object {

        private const val key = "DeviceDataQuery"

        @JvmStatic
        fun newInstance(param1: DeviceDataQuery) =
            TerminalFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(key, param1)
                }
            }
    }
}