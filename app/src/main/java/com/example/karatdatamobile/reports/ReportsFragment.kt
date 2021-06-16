package com.example.karatdatamobile.reports

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.karatdatamobile.databinding.FragmentReportsBinding
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Toothpick

class ReportsFragment : MvpAppCompatFragment(), ReportsView {
    private var _binding: FragmentReportsBinding? = null
    private val binding get() = _binding!!

    @InjectPresenter
    lateinit var presenter: ReportsPresenter

    @ProvidePresenter
    fun providePresenter(): ReportsPresenter =
        Toothpick.openRootScope().getInstance(ReportsPresenter::class.java)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         _binding = FragmentReportsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        binding.recyclerView.layoutManager = LinearLayoutManager(context)
        binding.recyclerView.adapter = presenter.getAdapter()

    }
}