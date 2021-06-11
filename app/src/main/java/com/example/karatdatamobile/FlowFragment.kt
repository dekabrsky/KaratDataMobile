package com.example.karatdatamobile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import com.example.karatdatamobile.databinding.FlowFragmentBinding
import com.example.karatdatamobile.main.KaratFragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import moxy.MvpAppCompatFragment

class FlowFragment : MvpAppCompatFragment() {

    private  var _binding: FlowFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val viewPager = binding.viewPager2
        viewPager.adapter = KaratFragmentStateAdapter(this.activity as AppCompatActivity)

        val tabLayout = binding.tabs
        TabLayoutMediator(tabLayout, viewPager) { _, position ->
            if(position == 1)
                "Архивы"
            else
                "Отчеты"
        }.attach()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FlowFragmentBinding.inflate(inflater, container, false)
        return inflater.inflate(R.layout.flow_fragment, container, false)
    }
}