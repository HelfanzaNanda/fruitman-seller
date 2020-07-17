package com.one.fruitmanseller.ui.main.timeline

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanseller.models.Product
import com.one.fruitmanseller.repositories.ProductRepository
import com.one.fruitmanseller.utils.ArrayResponse
import com.one.fruitmanseller.utils.SingleLiveEvent
import com.one.fruitmanseller.utils.SingleResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.sql.Time

class TimelineViewModel (private val productRepository: ProductRepository) : ViewModel(){
    private val state : SingleLiveEvent<TimelineState> = SingleLiveEvent()
    private val products = MutableLiveData<List<Product>>()

    private fun setLoading(){ state.value = TimelineState.Loading(true) }
    private fun hideLoading(){ state.value = TimelineState.Loading(false) }
    private fun toast(message: String){ state.value = TimelineState.Showtoast(message) }
    private fun success(){ state.value = TimelineState.Success }


    fun fetchPrducts(token: String){
        setLoading()
        productRepository.fetchProducts(token, object : ArrayResponse<Product>{
            override fun onSuccess(datas: List<Product>?) {
                hideLoading()
                products.postValue(datas)
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToProducts() = products
}

sealed class TimelineState{
    data class Loading(var state : Boolean = false) : TimelineState()
    data class Showtoast(var message : String) : TimelineState()
    object Success : TimelineState()
    object Reset : TimelineState()
    data class Validate(
        var name : String? = null,
        var price: String? = null,
        var address: String? = null,
        var desc: String? = null,
        var image: String? = null
    ) : TimelineState()
}