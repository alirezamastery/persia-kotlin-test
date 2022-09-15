package com.persia.test.ui.panel.products.variant.list

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import com.persia.test.data.repository.VariantRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@ExperimentalPagingApi
@HiltViewModel
class VariantListViewModel @Inject constructor(
    val repository: VariantRepository
) : ViewModel() {

    val variantFLow = repository.getAllVariants()
}