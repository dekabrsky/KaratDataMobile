package com.example.karatdatamobile.main

import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter


class KaratFragmentStateAdapter(
    activity: AppCompatActivity
) : FragmentStateAdapter(activity){

    private val names = arrayOf("Поиск", "Сохраненки")


    override fun createFragment(position: Int): Fragment {

        return when (position) {
            0 -> toFragment(ArchivesFragment(), position)
            else -> toFragment(ReportsFragment(), position)
        }
    }

    override fun getItemCount(): Int = names.size

    private fun toFragment(target: Fragment, position: Int) = target.apply {
        arguments = bundleOf(
            "name" to names[position],
            "position" to position
        )
    }
}