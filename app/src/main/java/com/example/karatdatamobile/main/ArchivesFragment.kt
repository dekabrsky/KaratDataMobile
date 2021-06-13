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
import com.example.karatdatamobile.App
import com.example.karatdatamobile.Enums.ArchiveType
import com.example.karatdatamobile.Models.DeviceSettings
import com.example.karatdatamobile.Models.Prefs
import com.example.karatdatamobile.R
import com.example.karatdatamobile.databinding.FragmentArchivesBinding
import com.example.karatdatamobile.settingsSetup.SettingDeviceFragment
import com.github.terrakok.cicerone.androidx.FragmentScreen
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_archives.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Toothpick

@SuppressLint("NewApi")
class ArchivesFragment: MvpAppCompatFragment(), ArchivesView{
    private var settings: DeviceSettings? = null

    @InjectPresenter
    lateinit var presenter: ArchivesPresenter

    @ProvidePresenter
    fun providePresenter(): ArchivesPresenter =
        Toothpick.openRootScope().getInstance(ArchivesPresenter::class.java)

    private var mDateSetListener: OnDateSetListener? = null
    private var _binding: FragmentArchivesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            settings = it.getSerializable(Prefs.DEVICE_SETTINGS) as DeviceSettings?
        }
        _binding = FragmentArchivesBinding.inflate(inflater, container, false)
        init()
        return binding.root
    }

    private fun init(){
        if (settings == null)
            presenter.loadSaved()
        else presenter.loadSaved(settings!!)

        binding.editTextSetting.setOnFocusChangeListener { _, hasFocus ->
            if(hasFocus) {
                binding.editTextSetting.clearFocus()
                showSettingsScreen()
            }
        }
        binding.imgSetting.setOnClickListener { showSettingsScreen() }

        binding.editTextDate.setOnFocusChangeListener { _, hasFocus ->
            binding.editTextSetting.clearFocus()
            presenter.onDateClick(hasFocus)
        }
        binding.imgData.setOnClickListener { presenter.onDateClick() }

        mDateSetListener = OnDateSetListener { _, year, month, day ->
            presenter.onDateChanged(year, month, day)
        }

        binding.proceedButton.setOnClickListener {
            presenter.proceed(
                spinner.selectedItem.toString(),
                getArchivesType() as ArrayList<ArchiveType>,
                getDeviceSettings()
            )
        }
    }

    private fun getArchivesType(): List<ArchiveType>{
        val archiveTypes = mutableListOf<ArchiveType>()

        if (binding.hourly.isChecked)
            archiveTypes.add(ArchiveType.HOURLY)
        if (binding.daily.isChecked)
            archiveTypes.add(ArchiveType.DAILY)
        if (binding.monthly.isChecked)
            archiveTypes.add(ArchiveType.MONTHLY)
        if (binding.integral.isChecked)
            archiveTypes.add(ArchiveType.INTEGRAL)

        return archiveTypes
    }

    private fun getDeviceSettings(): DeviceSettings? =
        if (settings != null) settings!!
        else  presenter.getSettingsFromPrefs()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun showSettingsScreen() =
        App.application.getRouter().navigateTo( FragmentScreen{ SettingDeviceFragment.newInstance() })

    override fun showCalendarDialog(){
        val cal = Calendar.getInstance()
        val year = cal[Calendar.YEAR]
        val month = cal[Calendar.MONTH]
        val day = cal[Calendar.DAY_OF_MONTH]

        val dialog = DatePickerDialog(
            requireActivity(),
            R.style.CalendarDatePickerDialog1,
            mDateSetListener, year, month, day
        )
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.WHITE))
        dialog.show()
    }

    override fun updateDateText(date: String) = binding.editTextDate.setText(date)

    override fun showDateError() = Snackbar.make(requireView(), "Дата не выбрана", Snackbar.LENGTH_LONG).show()

    override fun updateConnectionSettingsText(text: String) = binding.editTextSetting.setText(text)

    override fun showArchivesError() =
        Snackbar.make(requireView(), "Архивы не выбраны", Snackbar.LENGTH_LONG).show()

    override fun showSettingsError() {
        Snackbar.make(requireView(), "Настройки не настроены", Snackbar.LENGTH_LONG).show()
    }

    companion object {
        @JvmStatic
        fun newInstance(settings: DeviceSettings) =
            ArchivesFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(Prefs.DEVICE_SETTINGS, settings)
                }
            }
    }
}