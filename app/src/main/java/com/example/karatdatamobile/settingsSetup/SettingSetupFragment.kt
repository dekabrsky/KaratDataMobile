package com.example.karatdatamobile.settingsSetup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.karatdatamobile.databinding.ActivitySettingDeviceBinding
import moxy.MvpAppCompatFragment


class SettingSetupFragment: MvpAppCompatFragment() {

    private var _binding: ActivitySettingDeviceBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = ActivitySettingDeviceBinding.inflate(inflater, container, false)
        return binding.root
    }

    companion object{
        fun newInstance(): SettingSetupFragment{
            return SettingSetupFragment()
        }
    }
}