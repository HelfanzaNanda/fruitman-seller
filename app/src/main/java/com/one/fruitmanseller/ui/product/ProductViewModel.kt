package com.one.fruitmanseller.ui.product

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import android.provider.MediaStore
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.one.fruitmanseller.models.Fruit
import com.one.fruitmanseller.models.Product
import com.one.fruitmanseller.models.ProductImage
import com.one.fruitmanseller.models.SubDistrict
import com.one.fruitmanseller.repositories.FruitRepository
import com.one.fruitmanseller.repositories.ProductRepository
import com.one.fruitmanseller.repositories.SubDistrictRepository
import com.one.fruitmanseller.utils.ArrayResponse
import com.one.fruitmanseller.utils.SingleLiveEvent
import com.one.fruitmanseller.utils.SingleResponse
import com.one.fruitmanseller.webservices.ApiClient
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class ProductViewModel(private val productRepository: ProductRepository,
                       private val subDistrictRepository: SubDistrictRepository,
                       private val fruitRepository: FruitRepository) : ViewModel(){
    private val state : SingleLiveEvent<ProductState> = SingleLiveEvent()
    private val subDistricts = MutableLiveData<List<SubDistrict>>()
    private val idSubDistrict = MutableLiveData<String>()
    private val idFruit = MutableLiveData<String>()
    private val product = MutableLiveData<Product>()
    private val fruits = MutableLiveData<List<Fruit>>()
    private val latitude = MutableLiveData<String>()
    private val longitude = MutableLiveData<String>()
    private var images = MutableLiveData<MutableMap<String, String>>()

    init {
        images.value = mutableMapOf()
    }

    private fun setLoading() { state.value = ProductState.IsLoading(true) }
    private fun hideLoading() { state.value = ProductState.IsLoading(false) }
    private fun toast(message: String) {state.value = ProductState.ShowToast(message)}
    private fun successCreate() { state.value = ProductState.SuccessCreate}
    private fun successUpdate() { state.value = ProductState.SuccessUpdate }
    private fun successUpdatePhoto() { state.value = ProductState.SuccessUpdatePhoto }
    private fun successDelete() { state.value = ProductState.SuccessDelete }
    private fun createPartFromString(s: String) : RequestBody = RequestBody.create(MultipartBody.FORM, s)

    fun addImage(key : String, v : String){
        images.value!!.put(key, v)
        println("all ${images.value}")
    }

    fun createProduct(token: String, productToSend : Product) {
        try{
            setLoading()
            val map = HashMap<String, RequestBody>()
            map["price"] = createPartFromString(productToSend.price.toString())
            map["description"] = createPartFromString(productToSend.description!!)
            map["address"] = createPartFromString(productToSend.address!!)
            map["sub_district_id"] = createPartFromString(productToSend.subdistrict_id!!)
            map["fruit_id"] = createPartFromString(productToSend.fruit_id!!)
            map["lat"] = createPartFromString(productToSend.lat!!)
            map["lng"] = createPartFromString(productToSend.lng!!)

            val multipartTypedOutput = arrayOfNulls<MultipartBody.Part>(images.value!!.size)
            var i = 0
            images.value!!.forEach{ (_, value) ->
                val file = File(value)
                val body: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
                multipartTypedOutput[i] = MultipartBody.Part.createFormData("images[$i]", file.name, body)
                i++
            }

            productRepository.addProduct(token, map, multipartTypedOutput, object : SingleResponse<Product>{
                override fun onSuccess(data: Product?) {
                    hideLoading()
                    successCreate()
                }

                override fun onFailure(err: Error) {
                    hideLoading()
                    println(err.message.toString())
                    toast(err.message.toString())
                }

            })

        }catch (e: Exception){
            toast(e.message.toString())
        }

    }

    fun updateProduct(token: String, id : String, productToSend : Product){
        setLoading()
        productRepository.updateProduct(token, id, productToSend, object : SingleResponse<Product>{
            override fun onSuccess(data: Product?) {
                hideLoading()
                data?.let {
                    successUpdate()
//
//                    if (imageUrl.isNotEmpty()){
//                        updatePhotoProduct(token, id, imageUrl)
//                    }else{
//                        successUpdate()
//                    }
                }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun updatePhotoProduct(token: String, id : String, urlImages : ArrayList<String>){
        setLoading()
        val multipartTypedOutput = arrayOfNulls<MultipartBody.Part>(urlImages.size)
        var i = 0
        urlImages.forEach { url ->
            val file = File(url)
            val body: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
            multipartTypedOutput[i] = MultipartBody.Part.createFormData("images[$i]", file.name, body)
            i++
        }
        productRepository.updatePhotoProduct(token, id, multipartTypedOutput, object : SingleResponse<Product>{
            override fun onSuccess(data: Product?) {
                hideLoading()
                data?.let {  successUpdatePhoto() }
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

    fun fetchSubdistricts(token: String){
        setLoading()
        subDistrictRepository.fetchSubdistricts(token, object : ArrayResponse<SubDistrict>{
            override fun onSuccess(datas: List<SubDistrict>?) {
                hideLoading()
                datas?.let { subDistricts.postValue(it) }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }

        })
    }

    fun fetchFruits(token: String){
        setLoading()
        fruitRepository.fetchFruits(token, object : ArrayResponse<Fruit>{
            override fun onSuccess(datas: List<Fruit>?) {
                hideLoading()
                datas?.let { fruits.postValue(it) }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }

        })
    }

    fun findProduct(token: String, id: String){
        setLoading()
        productRepository.findProduct(token, id, object : SingleResponse<Product>{
            override fun onSuccess(data: Product?) {
                hideLoading()
                data?.let { product.postValue(it) }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }

        })
    }

    fun validate(price: String, address: String, desc: String, image: String?) : Boolean{
        state.value = ProductState.Reset
/*        if (name.isEmpty()){
            state.value = ProductState.Validate(name = "nama produk tidak boleh kosong")
            return false
        }*/
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

    fun setLatLng(lat : String, lng: String){
        latitude.postValue(lat)
        longitude.postValue(lng)
    }

    fun setIdSubDistrict(id : String) = idSubDistrict.postValue(id)
    fun setIdFruit(id : String) = idFruit.postValue(id)

    fun listenToState() = state
    fun getLat() = latitude
    fun getLng() = longitude
    fun listenToSubDistricts() = subDistricts
    fun listenToFruits() = fruits
    fun listenToProduct() = product
    fun getIdFruit() = idFruit
    fun getIdSubDistrict() = idSubDistrict
    fun listenToPathImages() = images
}
sealed class ProductState {
    data class IsLoading(var state : Boolean = false) : ProductState()
    data class ShowToast(var message : String) : ProductState()
    object SuccessCreate : ProductState()
    object SuccessUpdate : ProductState()
    object SuccessUpdatePhoto : ProductState()
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

//class UploadWorker(var token: String, var product : Product, var images : Array<String>, workerParams : WorkerParameters) : Worker(context, workerParams) {
//    override fun doWork(): Result {
//        val api = ApiClient.instance()
//        val productViewModel = ProductViewModel
//        val multipartTypedOutput = arrayOfNulls<MultipartBody.Part>(images.size)
//
//        for((i, j) in images.withIndex()) {
//            val data = inputData.getString(j)!!
//            val file = File(data)
//            val body: RequestBody = RequestBody.create(MediaType.parse("image/*"), file)
//            multipartTypedOutput[i] = MultipartBody.Part.createFormData("images[$i]", file.name, body)
//        }
//
//        println(multipartTypedOutput)
//        productViewModel.createProduct(token, product)
//        return Result.success()
//    }
//}
