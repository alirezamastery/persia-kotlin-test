package com.persia.test.ui.panel.accounting.cost.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.persia.test.R

class CostDetailFragment : Fragment() {

    companion object {

        fun newInstance() = CostDetailFragment()
    }

    private lateinit var viewModel: CostDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_cost_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(CostDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}