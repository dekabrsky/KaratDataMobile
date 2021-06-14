package com.example.karatdatamobile.templater

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.karatdatamobile.R
import com.example.karatdatamobile.Services.TemplateProvider
import com.example.karatdatamobile.databinding.FragmentTemplaterBinding
import kotlinx.android.synthetic.main.fragment_templater.*
import moxy.MvpAppCompatFragment
import moxy.presenter.InjectPresenter
import moxy.presenter.ProvidePresenter
import toothpick.Toothpick

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class TemplaterFragment : MvpAppCompatFragment(), TemplaterView {
    private var param1: String? = null
    private var param2: String? = null

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
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
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
    }

    companion object {
        @JvmStatic
        fun newInstance() = TemplaterFragment()
        fun newInstance(param1: String, param2: String) =
            TemplaterFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}