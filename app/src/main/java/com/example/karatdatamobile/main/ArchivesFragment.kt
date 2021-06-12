package com.example.karatdatamobile.main

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.icu.util.Calendar
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.example.karatdatamobile.App
import com.example.karatdatamobile.R
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
    ): View {
        _binding = FragmentArchivesBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init(){
        binding.editTextSetting.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) showSettingsScreen()
        }
        binding.imgSetting.setOnClickListener { showSettingsScreen() }

        binding.editTextDate.setOnFocusChangeListener { _, hasFocus ->
            presenter.onDateClick(hasFocus)
        }
        binding.imgData.setOnClickListener { presenter.onDateClick() }

        mDateSetListener = OnDateSetListener { _, year, month, day ->
            presenter.onDateChanged(year, month, day)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showSettingsScreen() =
        App.application.getRouter().navigateTo( FragmentScreen{ SettingSetupFragment.newInstance() })

    override fun showCalendarDialog(){
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
        dialog.show()
    }

    override fun updateDateText(date: String) = binding.editTextDate.setText(date)

    override fun updateConnectionSettingsText(text: String) = binding.editTextSetting.setText(text)
}