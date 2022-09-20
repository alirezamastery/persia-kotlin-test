package com.persia.test.ui.panel.products.variant.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.persia.test.domain.use_case.validation.ValidateNonEmptyField
import dagger.hilt.android.lifecycle.HiltViewModel
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class VariantDetailViewModel @Inject constructor(
    val validateNonEmptyField: ValidateNonEmptyField
) : ViewModel() {

    // var state = mutableStateOf(VariantAddEditFormState())
    private var _state = MutableLiveData(VariantAddEditFormState())
    val state: LiveData<VariantAddEditFormState>
        get() = _state

    fun onEvent(event: VariantAddEditFormEvent) {
        when (event) {
            is VariantAddEditFormEvent.DkpcChanged -> {
                Timber.i("dkpc changed")
                _state.postValue(state.value?.copy(dkpc = event.dkpc))
            }
            is VariantAddEditFormEvent.PriceMinChanged -> {
                Timber.i("PriceMin changed")
            }
            VariantAddEditFormEvent.Submit -> {
                submitForm()
            }
        }
    }

    private fun submitForm() {
        Timber.i("submited")
        val dkpcResult = validateNonEmptyField(state.value?.dkpc)
        // val priceMinResult = validateNonEmptyField(state.value?.priceMin)
        Timber.i("validate result: $dkpcResult")
    }
}