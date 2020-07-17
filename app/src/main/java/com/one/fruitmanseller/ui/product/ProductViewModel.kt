package com.one.fruitmanseller.ui.product

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

class ProductViewModel(private val productRepository: ProductRepository) : ViewModel(){
    private val state : SingleLiveEvent<ProductState> = SingleLiveEvent()

    private fun setLoading() { state.value = ProductState.IsLoading(true) }
    private fun hideLoading() { state.value = ProductState.IsLoading(false) }
    private fun toast(message: String) {state.value = ProductState.ShowToast(message)}
    private fun successCreate() { state.value = ProductState.SuccessCreate}
    private fun successUpdate() { state.value = ProductState.SuccessUpdate }
    private fun successDelete() { state.value = ProductState.SuccessDelete }
    private fun createPartFromString(s: String) : RequestBody = RequestBody.create(MultipartBody.FORM, s)

    fun createProduct(token: String, productToSend : Product, imageUrl: String){
        try{
            setLoading()
            val map = HashMap<String, RequestBody>()
            map["name"] = createPartFromString(productToSend.name!!)
            map["price"] = createPartFromString(productToSend.price.toString())
            map["description"] = createPartFromString(productToSend.description!!)
            map["address"] = createPartFromString(productToSend.address!!)
            val file = File(imageUrl)
            val requestBodyForFile = RequestBody.create(MediaType.parse("image/*"), file)
            val image = MultipartBody.Part.createFormData("image", file.name, requestBodyForFile)

            productRepository.addProduct(token, map, image, object : SingleResponse<Product>{
                override fun onSuccess(data: Product?) {
                    hideLoading()
                    successCreate()
                }

                override fun onFailure(err: Error) {
                    hideLoading()
                    toast(err.message.toString())
                }

            })

        }catch (e: Exception){
            println(e.message.toString())
            toast(e.message.toString())
        }

    }

    fun updateProduct(token: String, id : String, productToSend : Product, imageUrl : String){
        setLoading()
        productRepository.updateProduct(token, id, productToSend, object : SingleResponse<Product>{
            override fun onSuccess(data: Product?) {
                hideLoading()
                data?.let {
                    if (imageUrl.isNotEmpty()){
                        updatePhotoProduct(token, id, imageUrl)
                    }else{
                        successUpdate()
                    }
                }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun updatePhotoProduct(token: String, id : String, urlPhoto : String){
        setLoading()
        productRepository.updatePhotoProduct(token, id, urlPhoto, object : SingleResponse<Product>{
            override fun onSuccess(data: Product?) {
                hideLoading()
                data?.let {
                    successUpdate()
                }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun deleteProduct(token: String, id : String){
        setLoading()
        productRepository.deleteProduct(token, id, object : SingleResponse<Product>{
            override fun onSuccess(data: Product?) {
                hideLoading()
                data?.let { successDelete() }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }


    fun validate(name : String, price: String, address: String, desc: String, image: String?) : Boolean{
        state.value = ProductState.Reset
        if (name.isEmpty()){
            state.value = ProductState.Validate(name = "nama produk tidak boleh kosong")
            return false
        }
        if (price.isEmpty()){
            state.value = ProductState.Validate(price = "harga produk tidak boleh kosong")
            return false
        }
        if (address.isEmpty()){
            state.value = ProductState.Validate(address = "alamat produk tidak boleh kosong")
            return false
        }
        if (desc.isEmpty()){
            state.value = ProductState.Validate(desc = "deskripsi produk tidak boleh kosong")
            return false
        }
        if (image != null){
            if (image.isEmpty()){
                toast("foto produk tidak boleh kosong")
                return false
            }
        }
        return true
    }

    fun listenToState() = state
}
sealed class ProductState {
    data class IsLoading(var state : Boolean = false) : ProductState()
    data class ShowToast(var message : String) : ProductState()
    object SuccessCreate : ProductState()
    object SuccessUpdate : ProductState()
    object SuccessDelete : ProductState()
    object Reset : ProductState()
    data class Validate(
        var name : String? = null,
        var price: String? = null,
        var address: String? = null,
        var desc: String? = null,
        var image: String? = null
    ) : ProductState()
}