package com.persia.test.ui.panel.accounting.income

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.persia.test.R
import com.persia.test.databinding.FragmentIncomeListBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class IncomeListFragment : Fragment() {

    private val viewModel: IncomeListViewModel by viewModels()

    private lateinit var epoxyController: IncomeListEpoxyController

    private lateinit var _binding: FragmentIncomeListBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_income_list, container, false)
    }

}