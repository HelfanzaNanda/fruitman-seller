package com.one.fruitmanseller.repositories

import com.google.gson.GsonBuilder
import com.one.fruitmanseller.models.RegisterSeller
import com.one.fruitmanseller.models.Seller
import com.one.fruitmanseller.utils.SingleResponse
import com.one.fruitmanseller.webservices.ApiService
import com.one.fruitmanseller.webservices.WrappedResponse
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

interface SellerContract{
    fun register(registerSeller: RegisterSeller, listener : SingleResponse<RegisterSeller>)
    fun login(email : String, password : String, fcmToken : String, listener: SingleResponse<Seller>)
    fun profile(token : String, listener: SingleResponse<Seller>)
    fun updateProfile(token : String, seller: Seller, listener: SingleResponse<Seller>)
    fun updatePhotoProfile(token: String, pathImage : String, listener: SingleResponse<Seller>)
    fun forgotPassword(email: String, listener: SingleResponse<Seller>)
    fun updatePassword(token : String, password: String, listener: SingleResponse<Seller>)
    fun premium(token: String, image : String, listener: SingleResponse<Seller>)
}

class SellerRepository (private val api : ApiService) : SellerContract{
    override fun register(registerSeller: RegisterSeller, listener: SingleResponse<RegisterSeller>) {
        val g = GsonBuilder().create()
        val json = g.toJson(registerSeller)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.regiserPemilik(body).enqueue(object : Callback<WrappedResponse<RegisterSeller>>{
            override fun onFailure(call: Call<WrappedResponse<RegisterSeller>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<RegisterSeller>>, response: Response<WrappedResponse<RegisterSeller>>) {
                when {
                    response.isSuccessful -> {
                        val b =  response.body()
                        if (b?.status!!){
                            listener.onSuccess(b.data)
                        }else{
                            listener.onFailure(Error(b.message))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

    override fun login(email: String, password: String, fcmToken : String,listener: SingleResponse<Seller>) {

        api.login(email, password, fcmToken).enqueue(object : Callback<WrappedResponse<Seller>>{
            override fun onFailure(call: Call<WrappedResponse<Seller>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Seller>>, response: Response<WrappedResponse<Seller>>) {
                when{
                    response.isSuccessful ->{
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    ! response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

    override fun profile(token: String, listener: SingleResponse<Seller>) {
        api.profile(token).enqueue(object : Callback<WrappedResponse<Seller>>{
            override fun onFailure(call: Call<WrappedResponse<Seller>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Seller>>, response: Response<WrappedResponse<Seller>>) {
                when {
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

    override fun updateProfile(token: String, seller: Seller, listener: SingleResponse<Seller>) {
        val g = GsonBuilder().create()
        val json = g.toJson(seller)
        val body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json)
        api.updateProfile(token, body).enqueue(object : Callback<WrappedResponse<Seller>>{
            override fun onFailure(call: Call<WrappedResponse<Seller>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Seller>>, response: Response<WrappedResponse<Seller>>) {
                when{
                    response.isSuccessful -> {
                        val b = response.body()
                        if (b?.status!!){
                            listener.onSuccess(b.data)
                        }else{
                            listener.onFailure(Error(b.message))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun updatePhotoProfile(token: String, pathImage: String, listener: SingleResponse<Seller>) {
        val file = File(pathImage)
        val requestBodyForFile = RequestBody.create(MediaType.parse("image/*"), file)
        val image = MultipartBody.Part.createFormData("image", file.name, requestBodyForFile)
        api.updatePhotoProfile(token, image).enqueue(object : Callback<WrappedResponse<Seller>>{
            override fun onFailure(call: Call<WrappedResponse<Seller>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Seller>>, response: Response<WrappedResponse<Seller>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun forgotPassword(email: String, listener: SingleResponse<Seller>) {
        api.forgotPassword(email).enqueue(object : Callback<WrappedResponse<Seller>>{
            override fun onFailure(call: Call<WrappedResponse<Seller>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedResponse<Seller>>,
                response: Response<WrappedResponse<Seller>>
            ) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    !response.isSuccessful -> {
                        listener.onFailure(Error(response.message()))
                    }
                }
            }

        })
    }

    override fun updatePassword(token: String, password: String, listener: SingleResponse<Seller>) {
        api.updatePassword(token, password).enqueue(object : Callback<WrappedResponse<Seller>>{
            override fun onFailure(call: Call<WrappedResponse<Seller>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Seller>>, response: Response<WrappedResponse<Seller>>) {
                when{
                    response.isSuccessful -> {
                        val b = response.body()
                        if (b?.status!!) listener.onSuccess(b.data) else listener.onFailure(Error(b.message))
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun premium(token: String, image: String, listener: SingleResponse<Seller>) {
        val file = File(image)
        val requestBodyForFile = RequestBody.create(MediaType.parse("image/*"), file)
        val img = MultipartBody.Part.createFormData("image", file.name, requestBodyForFile)
        api.premium(token, img).enqueue(object : Callback<WrappedResponse<Seller>>{
            override fun onFailure(call: Call<WrappedResponse<Seller>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Seller>>, response: Response<WrappedResponse<Seller>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!) listener.onSuccess(body.data) else listener.onFailure(
                            Error(body.message)
                        )
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }
}