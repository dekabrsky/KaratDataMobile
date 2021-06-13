package com.example.karatdatamobile.settingsSetup

import android.hardware.usb.UsbDevice
import android.hardware.usb.UsbManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.karatdatamobile.R
import com.example.karatdatamobile.databinding.ActivitySettingDeviceBinding
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Toothpick
import java.util.*


class SettingDeviceFragment: MvpAppCompatFragment(), SettingDeviceView {
    private var _binding: ActivitySettingDeviceBinding? = null
    private val binding get() = _binding!!

    @InjectPresenter
    lateinit var presenter: SettingDevicePresenter

    @ProvidePresenter
    fun providePresenter(): SettingDevicePresenter =
        Toothpick.openRootScope().getInstance(SettingDevicePresenter::class.java)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivitySettingDeviceBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init(){
        presenter.loadSaved()
        binding.tcp.setOnClickListener { presenter.enableTCP() }
        binding.usb.setOnClickListener { presenter.enableUSB() }
        binding.fabClose.setOnClickListener { presenter.close() }
        binding.fabSave.setOnClickListener {
            presenter.save(
                binding.editTextIp.text.toString(),
                binding.editTextPort.text.toString(),
                binding.editTextAddress.text.toString()
            )
        }
        binding.updateDevices.setOnClickListener { presenter.loadDevices() }
    }

    override fun loadDevices(devices: HashMap<String, UsbDevice>, usbManager: UsbManager){
        val adapter = DeviceListAdapter(
            requireContext(),
            R.layout.device_list_item, devices, usbManager
        )
        binding.deviceList.adapter = adapter
        binding.devicesInfo.text = "Найдено устройств: ${devices.size}"
    }

    override fun showUSBSettings() {
        binding.usb.isChecked = true
        binding.editTextIp.visibility = View.GONE
        binding.editTextPort.visibility = View.GONE
        //binding.editTextAddress.visibility = View.GONE
        binding.textFieldIp.visibility = View.GONE
        binding.textFieldPort.visibility = View.GONE
        //binding.textFieldAddress.visibility = View.GONE
        binding.updateDevices.visibility = View.VISIBLE
        binding.deviceList.visibility = View.VISIBLE
        binding.devicesInfo.visibility = View.VISIBLE
        binding.tcp.isChecked = false
    }

    override fun showTCPSettings() {
        binding.tcp.isChecked = true
        binding.editTextIp.visibility = View.VISIBLE
        binding.editTextPort.visibility = View.VISIBLE
        //binding.editTextAddress.visibility = View.VISIBLE
        binding.textFieldIp.visibility = View.VISIBLE
        binding.textFieldPort.visibility = View.VISIBLE
        //binding.textFieldAddress.visibility = View.VISIBLE
        binding.updateDevices.visibility = View.GONE
        binding.deviceList.visibility = View.GONE
        binding.devicesInfo.visibility = View.GONE
        binding.usb.isChecked = false
    }

    override fun setIP(ip: String) = binding.editTextIp.setText(ip)
    override fun setPort(port: String) = binding.editTextPort.setText(port)
    override fun setSlaveID(sid: String) = binding.editTextAddress.setText(sid)

    companion object{
        fun newInstance(): SettingDeviceFragment{
            return SettingDeviceFragment()
        }
    }
}