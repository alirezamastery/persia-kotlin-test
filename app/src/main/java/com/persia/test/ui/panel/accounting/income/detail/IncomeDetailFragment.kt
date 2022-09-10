package com.persia.test.ui.panel.accounting.income.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.persia.test.R
import com.persia.test.databinding.FragmentIncomeDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class IncomeDetailFragment : Fragment() {

    private val viewModel: IncomeDetailViewModel by viewModels()
    private val safeArgs: IncomeDetailFragmentArgs by navArgs()

    private lateinit var _binding: FragmentIncomeDetailBinding
    private val binding get() = _binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_income_detail, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getIncomeDetail(safeArgs.incomeId)
        viewModel.incomeDetail.observe(viewLifecycleOwner) { income ->
            Timber.i("observe income: $income")
            binding.income = income
        }
    }

}