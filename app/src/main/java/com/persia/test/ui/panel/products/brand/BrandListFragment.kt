package com.persia.test.ui.panel.products.brand

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.persia.test.R

class BrandListFragment : Fragment() {

    companion object {

        fun newInstance() = BrandListFragment()
    }

    private lateinit var viewModel: BrandListViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_brand_list, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(BrandListViewModel::class.java)
        // TODO: Use the ViewModel
    }

}