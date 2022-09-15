package com.persia.test.ui.panel.products.variant.list

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import com.persia.test.R
import com.persia.test.data.domain.models.Variant
import com.persia.test.databinding.FragmentVariantListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber


@ExperimentalPagingApi
@AndroidEntryPoint
class VariantListFragment : Fragment() {

    private val viewModel: VariantListViewModel by viewModels()


    private lateinit var epoxyController: VariantListEpoxyController

    private lateinit var _binding: FragmentVariantListBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_variant_list, container, false
        )
        binding.lifecycleOwner = viewLifecycleOwner

        epoxyController = VariantListEpoxyController { id: Long -> Timber.i("ok variant $id") }
        binding.variantListEpoxyRecyclerView.setController(epoxyController)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            viewModel.variantFLow.collectLatest { pagingData: PagingData<Variant> ->
                epoxyController.submitData(pagingData = pagingData)
            }
        }
    }

}