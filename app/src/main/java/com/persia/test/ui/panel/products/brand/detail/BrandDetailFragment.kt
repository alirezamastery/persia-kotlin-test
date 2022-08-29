package com.persia.test.ui.panel.products.brand.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.persia.test.R

class BrandDetailFragment : Fragment() {

    companion object {

        fun newInstance() = BrandDetailFragment()
    }

    private lateinit var viewModel: BrandDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_brand_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BrandDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}