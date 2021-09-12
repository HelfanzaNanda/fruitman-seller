package com.one.fruitmanseller.repositories

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.one.fruitmanseller.models.Product
import com.one.fruitmanseller.utils.ArrayResponse
import com.one.fruitmanseller.utils.SingleResponse
import com.one.fruitmanseller.webservices.ApiService
import com.one.fruitmanseller.webservices.WrappedListResponse
import com.one.fruitmanseller.webservices.WrappedResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

interface ProductContract {
    fun addProduct(
        token: String,
        requestBody: HashMap<String, RequestBody>,
        images: Array<MultipartBody.Part?>,
        listener: SingleResponse<Product>
    )

    fun fetchProducts(token: String, listener: ArrayResponse<Product>)
    fun updateProduct(
        token: String,
        id: String,
        product: Product,
        listener: SingleResponse<Product>
    )

    fun updatePhotoProduct(
        token: String,
        id: String,
        images: Array<MultipartBody.Part?>,
        listener: SingleResponse<Product>
    )

    fun deleteProduct(token: String, id: String, listener: SingleResponse<Product>)
    fun findProduct(token: String, id: String, listener: SingleResponse<Product>)
}

class ProductRepository(private val api: ApiService) : ProductContract {
    override fun addProduct(
        token: String,
        requestBody: HashMap<String, RequestBody>,
        images: Array<MultipartBody.Part?>,
        listener: SingleResponse<Product>
    ) {
        api.createProduct(token, requestBody, images)
            .enqueue(object : Callback<WrappedResponse<Product>> {
                override fun onFailure(call: Call<WrappedResponse<Product>>, t: Throwable) {
                    listener.onFailure(Error(t.message))
                }

                override fun onResponse(
                    call: Call<WrappedResponse<Product>>,
                    response: Response<WrappedResponse<Product>>
                ) {
                    when {
                        response.isSuccessful -> {
                            val body = response.body()
                            if (body?.status!!) {
                                listener.onSuccess(body.data)
                                println(body.data)
                            } else {
                                listener.onFailure(Error(body.message))
                                println(Error(body.message))
                            }
                        }
                        !response.isSuccessful -> {
                            listener.onFailure(Error(response.message()))
                            println(Error(response.message()))
                        }
                    }
                }
            })
    }

    override fun fetchProducts(token: String, listener: ArrayResponse<Product>) {
        api.fetchProducts(token).enqueue(object : Callback<WrappedListResponse<Product>> {
            override fun onFailure(call: Call<WrappedListResponse<Product>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedListResponse<Product>>,
                response: Response<WrappedListResponse<Product>>
            ) {
                when {
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!) {
                            listener.onSuccess(body.data)
                        } else {
                            listener.onFailure(Error(body.message))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun updateProduct(token: String, id: String, product: Product, listener: SingleResponse<Product>) {
        val g = GsonBuilder().create()
        val json = g.toJson(product)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.updateProduct(token, id.toInt(), body)
            .enqueue(object : Callback<WrappedResponse<Product>> {
                override fun onFailure(call: Call<WrappedResponse<Product>>, t: Throwable) {
                    listener.onFailure(Error(t.message))
                }

                override fun onResponse(call: Call<WrappedResponse<Product>>, response: Response<WrappedResponse<Product>>) {
                    when {
                        response.isSuccessful -> {
                            val b = response.body()
                            if (b?.status!!) {
                                listener.onSuccess(b.data)
                            } else {
                                listener.onFailure(Error(b.message))
                            }
                        }
                        !response.isSuccessful -> listener.onFailure(Error(response.message()))
                    }
                }
            })
    }

    override fun updatePhotoProduct(token: String, id: String, images: Array<MultipartBody.Part?>, listener: SingleResponse<Product>) {
//        val file = File(pathImage)
//        val requestBodyForFile = RequestBody.create(MediaType.parse("image/*"), file)
//        val image = MultipartBody.Part.createFormData("image", file.name, requestBodyForFile)
        api.updatePhotoProduct(token, id.toInt(), images)
            .enqueue(object : Callback<WrappedResponse<Product>> {
                override fun onFailure(call: Call<WrappedResponse<Product>>, t: Throwable) {
                    listener.onFailure(Error(t.message))
                }

                override fun onResponse(call: Call<WrappedResponse<Product>>, response: Response<WrappedResponse<Product>>) {
                    when {
                        response.isSuccessful -> {
                            val body = response.body()
                            if (body?.status!!) {
                                listener.onSuccess(body.data)
                            } else {
                                listener.onFailure(Error(body.message))
                            }
                        }
                        !response.isSuccessful -> listener.onFailure(Error(response.message()))
                    }
                }

            })
    }

    override fun deleteProduct(token: String, id: String, listener: SingleResponse<Product>) {
        api.deleteProduct(token, id.toInt()).enqueue(object : Callback<WrappedResponse<Product>> {
            override fun onFailure(call: Call<WrappedResponse<Product>>, t: Throwable) = listener.onFailure(Error(t.message))
            override fun onResponse(call: Call<WrappedResponse<Product>>, response: Response<WrappedResponse<Product>>) {
                when {
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

    override fun findProduct(token: String, id: String, listener: SingleResponse<Product>) {
        api.findProduct(token, id.toInt()).enqueue(object : Callback<WrappedResponse<Product>>{
            override fun onFailure(call: Call<WrappedResponse<Product>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<Product>>,
                response: Response<WrappedResponse<Product>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }
}