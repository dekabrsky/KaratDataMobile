package com.example.karatdatamobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.karatdatamobile.models.DeviceSettings
import com.example.karatdatamobile.utils.Prefs
import com.example.karatdatamobile.models.TabsNames
import com.example.karatdatamobile.main.KaratFragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.flow_fragment.*
import moxy.MvpAppCompatFragment

class FlowFragment : MvpAppCompatFragment() {
    private var settings: DeviceSettings? = null
    private var isToReports: Boolean? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val viewPager = viewPager2
        viewPager.adapter = KaratFragmentStateAdapter(
            this.activity as AppCompatActivity,
            settings
        )

        if (isToReports == true){
            viewPager.post{
                viewPager.currentItem = 1
            }
        }

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
            isToReports = it.getBoolean("is_to_reports")
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
        @JvmStatic
        fun newInstance(isToReports: Boolean) =
            FlowFragment().apply {
                arguments = Bundle().apply {
                    putSerializable("is_to_reports", isToReports)
                }
            }
    }
}