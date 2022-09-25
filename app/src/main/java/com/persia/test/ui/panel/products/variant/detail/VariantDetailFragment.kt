package com.persia.test.ui.panel.products.variant.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.persia.test.R
import com.persia.test.databinding.FragmentVariantDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class VariantDetailFragment : Fragment() {

    private val viewModel: VariantDetailViewModel by viewModels()
    private val safeArgs: VariantDetailFragmentArgs by navArgs()

    private lateinit var _binding: FragmentVariantDetailBinding
    private val binding get() = _binding

    private var products: List<String> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_variant_detail, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner

        // val productArrayAdapter = ArrayAdapter(
        //     requireContext(), R.layout.product_input_dropdown_item, products
        // )
        // binding.variantProductInput.setAdapter(productArrayAdapter)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupListeners()
        setupObservers()
    }

    private fun setupListeners() {
        binding.variantProductInput.doOnTextChanged { text, start, before, count ->
            // Timber.i("value : ${viewModel.variantLoaded.value}")
            // Timber.i("value : ${viewModel.productList.value}")
            // if (viewModel.variantLoaded.value == false) {
            //     Timber.i("7".repeat(100))
            //     viewModel.setVariantLoaded()
            // } else {
            //     Timber.i("8".repeat(120))
            //     viewModel.onEvent(VariantAddEditFormEvent.ProductSearchChanged(searchPhrase = text.toString()))
            // }
            viewModel.onEvent(VariantAddEditFormEvent.ProductSearchChanged(searchPhrase = text.toString()))
        }
        binding.variantProductInput.setOnItemClickListener { parent, view, position, id ->
            binding.variantProductInput.showDropDown()
        }
        binding.variantProductInput.setOnItemClickListener { parent, view, position, id ->
            Timber.i("product clicked: position: $position | id: $id")
            viewModel.onEvent(VariantAddEditFormEvent.ProductSelected(index = position))
        }

        binding.variantDKPCInput.doOnTextChanged { text, start, before, count ->
            viewModel.onEvent(
                VariantAddEditFormEvent.DkpcChanged(dkpc = text.toString().toLong())
            )
        }
        binding.variantPriceMinInput.doOnTextChanged { text, start, before, count ->
            viewModel.onEvent(
                VariantAddEditFormEvent.PriceMinChanged(
                    priceMin = text.toString().toLong()
                )
            )
        }
        binding.variantDetailSubmitButton.setOnClickListener {
            viewModel.onEvent(VariantAddEditFormEvent.Submit)
        }
    }

    private fun setupObservers() {
        viewModel.apiError.observe(viewLifecycleOwner) { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
        }
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            isLoading?.let {
                if (isLoading == true) {
                    binding.variantDetailForm.visibility = View.GONE
                    binding.variantDetailLoading.visibility = View.VISIBLE
                } else {
                    binding.variantDetailForm.visibility = View.VISIBLE
                    binding.variantDetailLoading.visibility = View.GONE
                }
            }
        }
        viewModel.productList.observe(viewLifecycleOwner) { productList ->
            products = productList.map { product -> product.title }
            val productArrayAdapter = ArrayAdapter(
                requireContext(), R.layout.product_input_dropdown_item, products
            )
            binding.variantProductInput.setAdapter(productArrayAdapter)
        }
        if (safeArgs.variantId > 0) {
            viewModel.getVariantDetail(safeArgs.variantId)
            viewModel.variantDetail.observe(viewLifecycleOwner) { variant ->
                variant?.let {
                    binding.variantAddEditPageTitle.text = "تغییر تنوع ${variant.dkpc}"
                    binding.variantProductInput.setText(variant.product.title)
                    binding.variantActualProductInput.setText(variant.actualProduct?.title)
                    binding.variantSelectorInput.setText(variant.selector.value)
                    binding.variantDKPCInput.setText(variant.dkpc.toString())
                    binding.variantPriceMinInput.setText(variant.priceMin.toString())
                    binding.variantDetailIsActive.isChecked = variant.isActive
                }
            }
        }

    }

}