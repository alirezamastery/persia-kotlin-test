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
import com.persia.test.tools.ui.CustomArrayAdapter
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
        handleIsEditPage()
        setupListeners()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        handleIsEditPage()
    }

    private fun handleIsEditPage() {
        if (safeArgs.variantId > 0) {
            viewModel.setIsEditPage(true)
        } else {
            viewModel.setIsEditPage(false)
        }
    }

    private fun setupListeners() {
        binding.variantProductInput.doOnTextChanged { text, start, before, count ->
            if (text.toString() != viewModel.variantDetail.value?.product?.title) {
                viewModel.onEvent(VariantAddEditFormEvent.ProductSearchChanged(searchPhrase = text.toString()))
            }
        }
        // binding.variantProductInput.setOnTouchListener { v, event ->
        //     Timber.i("V: ${binding.variantProductInput.compoundDrawablePadding} | event: $event")
        //     Timber.i("x: ${event.rawX}")
        //     if (event.action == MotionEvent.ACTION_UP) {
        //         if (event.rawX >= binding.variantProductInput.right - binding.variantProductInput.compoundDrawablePadding * 2) {
        //             Timber.i("ggggggggggggg")
        //         }
        //     }
        //     v.performClick()
        // }
        binding.variantProductInput.setOnItemClickListener { parent, view, position, id ->
            viewModel.setSkipProductSearch(true)
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
            val pro = CustomArrayAdapter(
                requireContext(),
                R.layout.product_input_dropdown_item,
                products
            )
            Timber.i("new products: $products")
            binding.variantProductInput.setAdapter(pro)
        }
        if (safeArgs.variantId > 0) {
            viewModel.getVariantDetail(safeArgs.variantId)
            viewModel.variantDetail.observe(viewLifecycleOwner) { variant ->
                variant?.let {
                    binding.variantAddEditPageTitle.text = "تغییر تنوع ${variant.dkpc}"
                    binding.variantProductInput.setText(variant.product.title, false)
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