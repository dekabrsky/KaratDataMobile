package com.example.karatdatamobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.karatdatamobile.Models.DeviceSettings
import com.example.karatdatamobile.utils.Prefs
import com.example.karatdatamobile.Models.TabsNames
import com.example.karatdatamobile.main.KaratFragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.flow_fragment.*
import moxy.MvpAppCompatFragment

class FlowFragment : MvpAppCompatFragment() {
    private var settings: DeviceSettings? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewPager = viewPager2
        viewPager.adapter = KaratFragmentStateAdapter(
            this.activity as AppCompatActivity,
            settings
        )

        val tabLayout = tabs
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
                tab.text = TabsNames.names[position]
        }.attach()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        arguments?.let {
            settings = it.getSerializable(Prefs.DEVICE_SETTINGS) as DeviceSettings?
        }
        return inflater.inflate(R.layout.flow_fragment, container, false)
    }

    companion object {
        @JvmStatic
        fun newInstance(settings: DeviceSettings) =
            FlowFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(Prefs.DEVICE_SETTINGS, settings)
                }
            }
    }
}