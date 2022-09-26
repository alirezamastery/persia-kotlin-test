package com.persia.test.ui.panel.products.variant.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.persia.test.data.network.PersiaAtlasApiClient
import com.persia.test.domain.models.Product
import com.persia.test.domain.models.Variant
import com.persia.test.domain.use_case.validation.ValidateNonEmptyField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class VariantDetailViewModel @Inject constructor(
    private val apiClient: PersiaAtlasApiClient,
    private val validateNonEmptyField: ValidateNonEmptyField
) : ViewModel() {

    private var _state = MutableLiveData(VariantAddEditFormState())
    val state get() = _state

    private val _variantDetail = MutableLiveData<Variant?>()
    val variantDetail get() = _variantDetail

    private val _apiError = MutableLiveData<String?>()
    val apiError get() = _apiError

    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading get() = _isLoading

    private val _productList = MutableLiveData<List<Product>>()
    val productList get() = _productList

    private val _variantLoaded = MutableLiveData(false)
    val variantLoaded get() = _variantLoaded

    private val _isEditPage = MutableLiveData(false)
    val isEditPage get() = _isEditPage

    private val _skipProductSearch = MutableLiveData<Boolean?>(null)
    val skipProductSearch get() = _skipProductSearch

    init {
        getProductList()
    }

    fun onEvent(event: VariantAddEditFormEvent) {
        when (event) {
            is VariantAddEditFormEvent.ProductSearchChanged -> {
                if (_skipProductSearch.value == true) {
                    _skipProductSearch.value = false
                    return
                }
                Timber.i("product changed: ${event.searchPhrase}")
                getProductList(searchPhrase = event.searchPhrase)
            }
            is VariantAddEditFormEvent.ProductSelected -> {
                Timber.i("product selected: ${event.index} ${productList.value?.get(event.index)}")
                val product = productList.value!![event.index]
                _state.postValue(state.value?.copy(productId = product.id))
            }
            is VariantAddEditFormEvent.DkpcChanged -> {
                Timber.i("dkpc changed: ${event.dkpc}")
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
        Timber.i("submitted")
        val dkpcResult = validateNonEmptyField(state.value?.dkpc)
        val priceMinResult = validateNonEmptyField(state.value?.priceMin)
        Timber.i("validate result dkpc: $dkpcResult")
        Timber.i("validate result priceMin: $priceMinResult")
    }

    fun getVariantDetail(incomeId: Long) {    // TODO: get variant detail from repository
        viewModelScope.launch {
            Timber.i("$incomeId")
            _isLoading.postValue(true)
            val res = apiClient.getVariantById(incomeId)
            Timber.i("variant detail response: ${res.body}")
            if (!res.isSuccessful) {
                _apiError.postValue("could not get variant data")
            } else {
                _variantDetail.postValue(res.body.asDomainModel())
                _isLoading.value = false
            }
        }
    }

    private fun getProductList(searchPhrase: String? = null) {
        // if (_isEditPage.value == true) {
        //     _isEditPage.value = false
        //     return
        // }
        viewModelScope.launch {
            val res = apiClient.getProductList(searchPhrase = searchPhrase)
            Timber.i("product list: ${res.body}")
            if (!res.isSuccessful) {
                _apiError.postValue("Could not get product list")
            } else {
                val domainProducts = res.body.items.map { productResponse ->
                    productResponse.asDomainModel()
                }
                _productList.postValue(domainProducts)
            }
        }
    }

    fun createVariant() {

    }

    fun setVariantLoaded() {
        _variantLoaded.value = true
    }

    fun setIsEditPage(value: Boolean) {
        _variantLoaded.value = value
    }

    fun setSkipProductSearch(value: Boolean?) {
        _skipProductSearch.value = value
    }

    fun sendUpdateVariantRequest() {
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