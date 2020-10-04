package com.one.fruitmanseller.repositories

import com.one.fruitmanseller.models.Fruit
import com.one.fruitmanseller.utils.ArrayResponse
import com.one.fruitmanseller.webservices.ApiService
import com.one.fruitmanseller.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface FruitContract{
    fun fetchFruits(token : String, listener : ArrayResponse<Fruit>)
}

class FruitRepository(private val api : ApiService) : FruitContract{
    override fun fetchFruits(token: String, listener: ArrayResponse<Fruit>) {
        api.fetchFruits(token).enqueue(object : Callback<WrappedListResponse<Fruit>>{
            override fun onFailure(call: Call<WrappedListResponse<Fruit>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(
                call: Call<WrappedListResponse<Fruit>>,
                response: Response<WrappedListResponse<Fruit>>
            ) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}