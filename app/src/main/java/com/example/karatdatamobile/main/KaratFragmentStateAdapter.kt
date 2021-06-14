package com.example.karatdatamobile.main

import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.karatdatamobile.Models.DeviceSettings
import com.example.karatdatamobile.Models.TabsNames


class KaratFragmentStateAdapter(
    activity: AppCompatActivity,
    private val settings: DeviceSettings?
) : FragmentStateAdapter(activity){

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> {
                if (settings != null)
                    toFragment(ArchivesFragment.newInstance(settings), position)
                else toFragment(ArchivesFragment(), position)
            }
            else -> toFragment(ReportsFragment(), position)
        }
    }

    override fun getItemCount(): Int = TabsNames.names.size

    private fun toFragment(target: Fragment, position: Int) = target.apply {
        arguments = bundleOf(
            "name" to TabsNames.names[position],
            "position" to position
        )
    }
}