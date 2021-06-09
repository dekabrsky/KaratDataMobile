package com.example.karatdatamobile.settingsSetup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.karatdatamobile.databinding.ActivityMainBinding
import com.example.karatdatamobile.databinding.ActivitySettingDeviceBinding
import com.example.karatdatamobile.databinding.FragmentArchivesBinding


class SettingSetupFragment: Fragment() {

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