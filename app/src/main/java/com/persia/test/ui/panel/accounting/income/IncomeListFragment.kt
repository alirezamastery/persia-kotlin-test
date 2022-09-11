package com.persia.test.ui.panel.accounting.income

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.persia.test.R
import com.persia.test.databinding.FragmentIncomeListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
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
    ): View {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_income_list, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner

        epoxyController = IncomeListEpoxyController(::onIncomeSelected)
        binding.incomeListRecyclerView.setController(epoxyController)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // viewModel.fetchIncomes()
        // setupObservers()
        lifecycleScope.launch {
            viewModel.incomeFlow.collectLatest {
                epoxyController.submitData(it)
            }
        }
    }

    // private fun setupObservers() {
    //     viewModel.repository.incomes.observe(viewLifecycleOwner) { incomes ->
    //         incomes?.apply {
    //             Timber.i("incomes: $incomes")
    //             epoxyController.setData(incomes)
    //         }
    //     }
    // }

    private fun onIncomeSelected(incomeId: Long) {
        Timber.i("income clicked: $incomeId")
        val directions =
            IncomeListFragmentDirections.actionIncomeListFragmentToIncomeDetailFragment(
                incomeId = incomeId
            )
        findNavController().navigate(directions)
    }

}