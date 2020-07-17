package com.one.fruitmanseller.repositories

import com.one.fruitmanseller.models.Order
import com.one.fruitmanseller.utils.ArrayResponse
import com.one.fruitmanseller.webservices.ApiService
import com.one.fruitmanseller.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface OrderContract{
    fun fetchOrderIn(token : String, listener : ArrayResponse<Order>)
}
class OrderRepository (private val api : ApiService) : OrderContract {
    override fun fetchOrderIn(token: String, listener: ArrayResponse<Order>) {
        api.fetchOrderIn(token).enqueue(object : Callback<WrappedListResponse<Order>>{
            override fun onFailure(call: Call<WrappedListResponse<Order>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<Order>>, response: Response<WrappedListResponse<Order>>) {
                when {
                    response.isSuccessful -> {
                        val body = response.body()
                        if (body?.status!!){
                            listener.onSuccess(body.data)
                        }else{
                            listener.onFailure(Error(body.message))
                        }
                    }
                    !response.isSuccessful -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}