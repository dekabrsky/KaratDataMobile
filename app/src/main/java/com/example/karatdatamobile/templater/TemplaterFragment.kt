package com.example.karatdatamobile.templater

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.karatdatamobile.models.ParsedData
import com.example.karatdatamobile.databinding.FragmentTemplaterBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_templater.*
import kotlinx.android.synthetic.main.fragment_templater.loadSign
import kotlinx.android.synthetic.main.fragment_terminal.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Toothpick
import java.io.Serializable

private const val ARG_PARAM1 = "param1"

class TemplaterFragment : MvpAppCompatFragment(), TemplaterView {
    private lateinit var dataFromDevice: ParsedData

    private var _binding: FragmentTemplaterBinding? = null
    private val binding get() = _binding!!

    @InjectPresenter
    lateinit var presenter: TemplaterPresenter

    @ProvidePresenter
    fun providePresenter(): TemplaterPresenter =
        Toothpick.openRootScope().getInstance(TemplaterPresenter::class.java)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dataFromDevice = it.getSerializable(ARG_PARAM1) as ParsedData
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentTemplaterBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){
        template_fields.layoutManager = LinearLayoutManager(context)
        template_fields.adapter = presenter.getAdapter()
        template_fields.adapter

        templaterButton.setOnClickListener {
            presenter.prepareData((template_fields.adapter as TemplateFieldsAdapter).getTypeToValue(), dataFromDevice)
        }
    }

    override fun showLoadSign() {
        loadSign.visibility = View.VISIBLE
    }

    override fun hideLoadSign() {
        loadSign.visibility = View.INVISIBLE
    }

    override fun showDialog(){
        val builder = AlertDialog.Builder(requireContext())

        with(builder)
        {
            setTitle("Подтверждение")
            setMessage("Записать отчёт?")
            setPositiveButton("Да") { _, _ -> presenter.writeXLS() }
            setNegativeButton("Править") { _, _ -> }
        }

        builder.show()
    }

    override fun showOnSuccessWrite(msg: String) {
        Snackbar.make(requireView(), msg, Snackbar.LENGTH_LONG).show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: Serializable) =
            TemplaterFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_PARAM1, param1)
                }
            }
    }
}