package com.one.fruitmanseller.repositories

import com.one.fruitmanseller.models.SubDistrict
import com.one.fruitmanseller.utils.ArrayResponse
import com.one.fruitmanseller.webservices.ApiService
import com.one.fruitmanseller.webservices.WrappedListResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface SubDistrictContract{
    fun fetchSubdistricts(token : String, listener : ArrayResponse<SubDistrict>)
}

class SubDistrictRepository (private val api : ApiService) : SubDistrictContract{
    override fun fetchSubdistricts(token: String, listener: ArrayResponse<SubDistrict>) {
        api.fetchSubDistricts(token).enqueue(object : Callback<WrappedListResponse<SubDistrict>> {
            override fun onFailure(call: Call<WrappedListResponse<SubDistrict>>, t: Throwable) {
                listener.onFailure(Error(t.message))
            }

            override fun onResponse(call: Call<WrappedListResponse<SubDistrict>>, response: Response<WrappedListResponse<SubDistrict>>) {
                when{
                    response.isSuccessful -> listener.onSuccess(response.body()!!.data)
                    else -> listener.onFailure(Error(response.message()))
                }
            }

        })
    }

}