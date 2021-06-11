package com.example.karatdatamobile.main

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.karatdatamobile.App
import com.example.karatdatamobile.R
import com.example.karatdatamobile.settingsSetup.SettingDeviceActivity
import com.example.karatdatamobile.databinding.FragmentArchivesBinding
import com.example.karatdatamobile.settingsSetup.SettingSetupFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Toothpick

@SuppressLint("NewApi")
class ArchivesFragment: MvpAppCompatFragment(), ArchivesView{

    @InjectPresenter
    lateinit var presenter: ArchivesPresenter

    @ProvidePresenter
    fun providePresenter(): ArchivesPresenter =
        Toothpick.openRootScope().getInstance(ArchivesPresenter::class.java)

    private var mDateSetListener: OnDateSetListener? = null
    private  var _binding: FragmentArchivesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentArchivesBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init(){
        binding.editTextSetting.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                App.application.getRouter().navigateTo( FragmentScreen{ SettingSetupFragment.newInstance() })
            }
        }

        binding.editTextDate.setOnFocusChangeListener { v, hasFocus ->
            showCalendarDialog(hasFocus)
        }

        mDateSetListener =
            OnDateSetListener { _, year, month, day ->
                var month = month
                month += 1
                val date = "$day/$month/$year"
                binding.editTextDate.setText(date)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun showCalendarDialog(hasFocus: Boolean){
        if(hasFocus){
            val cal = Calendar.getInstance()
            val year = cal[Calendar.YEAR]
            val month = cal[Calendar.MONTH]
            val day = cal[Calendar.DAY_OF_MONTH]

            val dialog = DatePickerDialog(
                activity!!,
                R.style.CalendarDatePickerDialog1,
                mDateSetListener, year, month, day
            )
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
            dialog.show()}
    }
}