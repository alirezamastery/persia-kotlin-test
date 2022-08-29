package com.persia.test.ui.panel.products.variant.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.persia.test.R

class VariantDetailFragment : Fragment() {

    companion object {

        fun newInstance() = VariantDetailFragment()
    }

    private lateinit var viewModel: VariantDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_variant_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(VariantDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}