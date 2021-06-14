package com.example.karatdatamobile.templater

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.karatdatamobile.models.ParsedData
import com.example.karatdatamobile.databinding.FragmentTemplaterBinding
import kotlinx.android.synthetic.main.fragment_templater.*
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
            presenter.writeXLS((template_fields.adapter as TemplateFieldsAdapter).getTypeToValue(), dataFromDevice)
        }
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