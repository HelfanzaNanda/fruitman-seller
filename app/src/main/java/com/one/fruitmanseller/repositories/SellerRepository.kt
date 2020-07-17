package com.one.fruitmanseller.repositories

import com.google.gson.GsonBuilder
import com.one.fruitmanseller.models.RegisterSeller
import com.one.fruitmanseller.models.Seller
import com.one.fruitmanseller.utils.SingleResponse
import com.one.fruitmanseller.webservices.ApiService
import com.one.fruitmanseller.webservices.WrappedResponse
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface SellerContract{
    fun register(registerSeller: RegisterSeller, listener : SingleResponse<RegisterSeller>)
    fun login(email : String, password : String, listener: SingleResponse<Seller>)
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

    override fun login(email: String, password: String, listener: SingleResponse<Seller>) {
        println(email)
        println(password)
        api.login(email, password).enqueue(object : Callback<WrappedResponse<Seller>>{
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

}