package com.persia.test.ui.panel.products.actualProduct.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.persia.test.R

class ActualProductDetailFragment : Fragment() {

    companion object {

        fun newInstance() = ActualProductDetailFragment()
    }

    private lateinit var viewModel: ActualProductDetailViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_actual_product_detail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ActualProductDetailViewModel::class.java)
        // TODO: Use the ViewModel
    }

}