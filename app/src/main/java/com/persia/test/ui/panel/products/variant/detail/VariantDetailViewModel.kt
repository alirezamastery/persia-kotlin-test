package com.persia.test.ui.panel.products.variant.detail

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.persia.test.data.network.PersiaAtlasApiClient
import com.persia.test.domain.models.ActualProduct
import com.persia.test.domain.models.Product
import com.persia.test.domain.models.Variant
import com.persia.test.domain.models.VariantSelector
import com.persia.test.domain.use_case.validation.ValidateNonEmptyField
import com.persia.test.domain.use_case.validation.ValidateNonEmptyNumberField
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class VariantDetailViewModel @Inject constructor(
    private val apiClient: PersiaAtlasApiClient,
    private val validateNonEmptyField: ValidateNonEmptyField,
    private val validateNonEmptyNumberField: ValidateNonEmptyNumberField
) : ViewModel() {

    private var _state = MutableLiveData(VariantAddEditFormState())
    val state get() = _state

    private val _variantId = MutableLiveData<Long>()
    val variantId get() = _variantId

    private val _variantDetail = MutableLiveData<Variant?>()
    val variantDetail get() = _variantDetail

    private val _apiError = MutableLiveData<String?>()
    val apiError get() = _apiError

    private val _isLoading = MutableLiveData<Boolean?>()
    val isLoading get() = _isLoading

    private val _productList = MutableLiveData<List<Product>>()
    val productList get() = _productList

    private val _actualProductList = MutableLiveData<List<ActualProduct>>()
    val actualProductList get() = _actualProductList

    private val _variantSelectorList = MutableLiveData<List<VariantSelector>>()
    val variantSelectorList get() = _variantSelectorList

    private val _variantLoaded = MutableLiveData(false)
    private val _isEditPage = MutableLiveData(false)
    private val _skipProductSearch = MutableLiveData<Boolean?>(null)
    private val _skipActualProductSearch = MutableLiveData<Boolean?>(null)
    private val _skipSelectorSearch = MutableLiveData<Boolean?>(null)


    private val validationEventChannel = Channel<ValidationEvent>()
    val validationEvents = validationEventChannel.receiveAsFlow() // can be used in ui

    init {
        getProductList()
        getActualProductList()
        getVariantSelectorList()
    }

    fun onEvent(event: VariantAddEditFormEvent) {
        when (event) {
            // Product:
            is VariantAddEditFormEvent.ProductSearchChanged -> {
                if (_skipProductSearch.value == true) {
                    _skipProductSearch.value = false
                    return
                }
                getProductList(searchPhrase = event.searchPhrase)
            }
            is VariantAddEditFormEvent.ProductSelected -> {
                val product = _productList.value!![event.index]
                _state.postValue(state.value?.copy(productId = product.id))
            }
            // Actual Product:
            is VariantAddEditFormEvent.ActualProductSearchChanged -> {
                if (_skipActualProductSearch.value == true) {
                    _skipActualProductSearch.value = false
                    return
                }
                getActualProductList(searchPhrase = event.searchPhrase)
            }
            is VariantAddEditFormEvent.ActualProductSelected -> {
                val actualProduct = _actualProductList.value!![event.index]
                _state.postValue(state.value?.copy(actualProductId = actualProduct.id))
            }
            // Variant Selector:
            is VariantAddEditFormEvent.SelectorSearchChanged -> {
                if (_skipSelectorSearch.value == true) {
                    _skipSelectorSearch.value = false
                    return
                }
                getVariantSelectorList(searchPhrase = event.searchPhrase)
            }
            is VariantAddEditFormEvent.SelectorSelected -> {
                val selector = _variantSelectorList.value!![event.index]
                _state.postValue(state.value?.copy(variantSelectorId = selector.id))
            }
            // DKPC:
            is VariantAddEditFormEvent.DkpcChanged -> {
                _state.postValue(state.value?.copy(dkpc = event.dkpc))
            }
            // Price Min:
            is VariantAddEditFormEvent.PriceMinChanged -> {
                _state.postValue(state.value?.copy(priceMin = event.priceMin))
            }
            // Submit:
            VariantAddEditFormEvent.Submit -> {
                submitForm()
            }
        }
    }

    private fun submitForm() {
        Timber.i("submitted")
        val productIdResult = validateNonEmptyField(state.value?.productId)
        val actualProductIdResult = validateNonEmptyField(state.value?.actualProductId)
        val selectorIdResult = validateNonEmptyField(state.value?.variantSelectorId)
        val dkpcResult = validateNonEmptyNumberField(state.value?.dkpc)
        val priceMinResult = validateNonEmptyNumberField(state.value?.priceMin)
        Timber.i("validate result dkpc: $dkpcResult")
        Timber.i("validate result priceMin: $priceMinResult")
        val hasError = listOf(
            productIdResult,
            actualProductIdResult,
            selectorIdResult,
            dkpcResult,
            priceMinResult
        ).any { !it.successful }

        if (hasError) {
            _state.postValue(
                state.value?.copy(
                    productIdError = productIdResult.errorMessage,
                    actualProductIdError = actualProductIdResult.errorMessage,
                    variantSelectorIdError = selectorIdResult.errorMessage,
                    dkpcError = dkpcResult.errorMessage,
                    priceMinError = priceMinResult.errorMessage
                )
            )
            // return
        }
        Timber.i("is: ${_isEditPage.value}")
        if (_isEditPage.value == true) {
            sendUpdateVariantRequest()
            viewModelScope.launch {
                validationEventChannel.send(ValidationEvent.Success) // to show a Toast for example
            }
        } else {
            viewModelScope.launch {
                sendCreateVariantRequest()
            }
        }
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

    private fun getActualProductList(searchPhrase: String? = null) {
        viewModelScope.launch {
            val res = apiClient.getActualProductList(searchPhrase = searchPhrase)
            if (!res.isSuccessful) {
                _apiError.postValue("Could not get actual product list")
            } else {
                val actualProducts = res.body.items.map { responseItem ->
                    responseItem.asDomainModel()
                }
                _actualProductList.postValue(actualProducts)
            }
        }
    }

    private fun getVariantSelectorList(searchPhrase: String? = null) {
        viewModelScope.launch {
            val res = apiClient.getVariantSelectorList(searchPhrase = searchPhrase)
            if (!res.isSuccessful) {
                _apiError.postValue("Could not get product list")
            } else {
                val selectors = res.body.items.map { responseItem ->
                    responseItem.asDomainModel()
                }
                _variantSelectorList.postValue(selectors)
            }
        }
    }

    fun setVariantId(value: Long) {
        _variantId.value = value
    }

    fun setVariantLoaded() {
        _variantLoaded.value = true
    }

    fun updateFormState() {
        _state.postValue(
            _state.value?.copy(
                productId = variantDetail.value?.product?.id,
                actualProductId = variantDetail.value?.actualProduct?.id,
                variantSelectorId = variantDetail.value?.selector?.id,
                dkpc = variantDetail.value?.dkpc.toString(),
                priceMin = variantDetail.value?.priceMin.toString()
            )
        )
    }

    fun setIsEditPage(value: Boolean) {
        _isEditPage.value = value
    }

    fun setSkipProductSearch(value: Boolean?) {
        _skipProductSearch.value = value
    }

    fun setSkipActualProductSearch(value: Boolean?) {
        _skipActualProductSearch.value = value
    }

    fun setSkipSelectorSearch(value: Boolean?) {
        _skipSelectorSearch.value = value
    }

    private fun sendCreateVariantRequest() {

    }

    private fun sendUpdateVariantRequest() {
        val data = HashMap<String, Any>()
        Timber.i("state: ${state.value}")
        data["product"] = state.value?.productId!!.toLong()
        data["actual_product"] = state.value?.actualProductId!!.toLong()
        data["selector"] = state.value?.variantSelectorId!!.toLong()
        data["dkpc"] = state.value?.dkpc!!.toLong()
        data["price_min"] = state.value?.priceMin!!.toLong()
        println("payload: $data")
        // viewModelScope.launch {
        //     val postResponse = apiClient.updateVariant(JSONObject(data.toString()))
        //     Timber.i("post response code: ${postResponse.code}")
        //     Timber.i("post response body: ${postResponse.bodyNullable}")
        //     Timber.i("post response data: ${postResponse.data?.errorBody().toString()}")
        // }
    }


    sealed class ValidationEvent {
        object Success : ValidationEvent()
    }
}