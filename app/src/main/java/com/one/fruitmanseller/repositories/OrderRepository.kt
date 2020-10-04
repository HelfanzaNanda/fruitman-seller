package com.one.fruitmanseller.repositories

import com.one.fruitmanseller.models.Order
import com.one.fruitmanseller.utils.ArrayResponse
import com.one.fruitmanseller.utils.SingleResponse
import com.one.fruitmanseller.webservices.ApiService
import com.one.fruitmanseller.webservices.WrappedListResponse
import com.one.fruitmanseller.webservices.WrappedResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface OrderContract{
    fun fetchOrderIn(token : String, listener : ArrayResponse<Order>)
    fun fetchOrderInProgress(token : String, listener : ArrayResponse<Order>)
    fun fetchOrderComplete(token : String, listener : ArrayResponse<Order>)
    fun cancel(token: String, id : String, listener: SingleResponse<Order>)
    fun confirm(token: String, id : String, listener: SingleResponse<Order>)
    fun complete(token: String, id : String, listener: SingleResponse<Order>)
}

class OrderRepository (private val api : ApiService) : OrderContract {
    override fun fetchOrderIn(token: String, listener: ArrayResponse<Order>) {
        api.fetchOrderIn(token).enqueue(object : Callback<WrappedListResponse<Order>>{
            override fun onFailure(call: Call<WrappedListResponse<Order>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Order>>, response: Response<WrappedListResponse<Order>>) {
                when {
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

    override fun fetchOrderInProgress(token: String, listener: ArrayResponse<Order>) {
        api.fetchOrderInProgress(token).enqueue(object : Callback<WrappedListResponse<Order>>{
            override fun onFailure(call: Call<WrappedListResponse<Order>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Order>>, response: Response<WrappedListResponse<Order>>) {
                when {
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

    override fun fetchOrderComplete(token: String, listener: ArrayResponse<Order>) {
        api.fetchOrderComplete(token).enqueue(object : Callback<WrappedListResponse<Order>>{
            override fun onFailure(call: Call<WrappedListResponse<Order>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Order>>, response: Response<WrappedListResponse<Order>>) {
                when {
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

    override fun cancel(token: String, id: String, listener: SingleResponse<Order>) {
        api.orderCancel(token, id.toInt()).enqueue(object : Callback<WrappedResponse<Order>>{
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

    override fun confirm(token: String, id: String, listener: SingleResponse<Order>) {
        api.orderConfirmed(token, id.toInt()).enqueue(object : Callback<WrappedResponse<Order>>{
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

    override fun complete(token: String, id: String, listener: SingleResponse<Order>) {
        api.orderComplete(token, id.toInt()).enqueue(object : Callback<WrappedResponse<Order>>{
            override fun onFailure(call: Call<WrappedResponse<Order>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedResponse<Order>>, response: Response<WrappedResponse<Order>>) {
                when{
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    else -> listener.onFailure(Error(response.message()))
                }
            }
        })
    }

}