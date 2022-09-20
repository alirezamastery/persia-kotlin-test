package com.persia.test.ui.panel.products.variant.detail

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import com.persia.test.R
import com.persia.test.databinding.FragmentVariantDetailBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class VariantDetailFragment : Fragment() {

    private val viewModel: VariantDetailViewModel by viewModels()

    private lateinit var _binding: FragmentVariantDetailBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_variant_detail, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
    }

    private fun setupListeners() {
        binding.variantDKPCEditText.doOnTextChanged { text, start, before, count ->
            viewModel.onEvent(VariantAddEditFormEvent.DkpcChanged(text.toString().toLong()))
        }
        binding.variantPriceMinEditText.doOnTextChanged { text, start, before, count ->
            viewModel.onEvent(VariantAddEditFormEvent.PriceMinChanged(text.toString().toLong()))
        }
        binding.variantDetailSubmitButton.setOnClickListener {
            viewModel.onEvent(VariantAddEditFormEvent.Submit)
        }
    }

}