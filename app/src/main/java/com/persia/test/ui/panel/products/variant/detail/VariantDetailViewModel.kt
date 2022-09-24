package com.persia.test.ui.panel.products.variant.detail

import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.persia.test.data.network.PersiaAtlasApiClient
import com.persia.test.domain.models.Variant
import com.persia.test.domain.use_case.validation.ValidateNonEmptyField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject
import kotlinx.serialization.*
import kotlinx.serialization.json.*


@HiltViewModel
class VariantDetailViewModel @Inject constructor(
    val apiClient: PersiaAtlasApiClient,
    val validateNonEmptyField: ValidateNonEmptyField
) : ViewModel() {

    // var state = mutableStateOf(VariantAddEditFormState())
    private var _state = MutableLiveData(VariantAddEditFormState())
    val state: LiveData<VariantAddEditFormState>
        get() = _state

    private val _variantDetail = MutableLiveData<Variant?>()
    val variantDetail: LiveData<Variant?>
        get() = _variantDetail

    private val _apiError = MutableLiveData<String?>()
    val apiError: LiveData<String?>
        get() = _apiError

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
        val priceMinResult = validateNonEmptyField(state.value?.priceMin)
        Timber.i("validate result dkpc: $dkpcResult")
        Timber.i("validate result priceMin: $priceMinResult")
    }

    fun getVariantDetail(incomeId: Long) {
        viewModelScope.launch {
            Timber.i("$incomeId")
            val res = apiClient.getVariantById(incomeId)
            Timber.i("variant detail response: ${res.body}")
            if (!res.isSuccessful) {
                _apiError.postValue("could not get variant data")
            }
            _variantDetail.postValue(res.body.asDomainModel())
        }
    }

    fun createVariant() {

    }

    fun updateVariant() {
        val data = HashMap<String, Any>()
        data["id"] = 456
        data["title"] = "testing"
        viewModelScope.launch {
            val postResponse = apiClient.createVariant(JSONObject(data.toString()))
            Timber.i("post response code: ${postResponse.code}")
            Timber.i("post response body: ${postResponse.bodyNullable}")
            Timber.i("post response data: ${postResponse.data?.errorBody().toString()}")
        }
    }
}