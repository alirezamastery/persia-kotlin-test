package com.persia.test.ui.panel.accounting.cost

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.persia.test.R

class CostListFragment : Fragment() {

    companion object {

        fun newInstance() = CostListFragment()
    }

    private lateinit var viewModel: CostListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cost_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CostListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}