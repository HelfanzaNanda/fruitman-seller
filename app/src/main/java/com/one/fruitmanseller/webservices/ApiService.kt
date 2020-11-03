package com.one.fruitmanseller.webservices

import com.google.gson.annotations.SerializedName
import com.one.fruitmanseller.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @FormUrlEncoded
    @POST("api/seller/login")
    fun login(
        @Field("email") email : String,
        @Field("password") password : String,
        @Field("fcm_token") fcmToken : String
    ) : Call<WrappedResponse<Seller>>

    @Headers("Content-Type: application/json")
    @POST("api/seller/register")
    fun regiserPemilik(
        @Body body: RequestBody
    ) : Call<WrappedResponse<RegisterSeller>>

    @GET("api/seller/profile")
    fun profile(
        @Header("Authorization") token : String
    ) : Call<WrappedResponse<Seller>>

    @Headers("Content-Type: application/json")
    @POST("api/seller/profile/update")
    fun updateProfile(
        @Header("Authorization") token : String,
        @Body body: RequestBody
    ) : Call<WrappedResponse<Seller>>

    @Multipart
    @POST("api/seller/profile/update/photo")
    fun updatePhotoProfile(
        @Header("Authorization") token : String,
        @Part image : MultipartBody.Part
    ) :Call<WrappedResponse<Seller>>

    @FormUrlEncoded
    @POST("api/seller/profile/update/password")
    fun updatePassword(
        @Header("Authorization") token : String,
        @Field("password") pass : String
    ) :Call<WrappedResponse<Seller>>

    @FormUrlEncoded
    @POST("api/seller/password/email")
    fun forgotPassword(
        @Field("email") email : String
    ) :Call<WrappedResponse<Seller>>

    @GET("api/seller/subdistrict")
    fun fetchSubDistricts(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<SubDistrict>>

    @GET("api/seller/fruit")
    fun fetchFruits(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Fruit>>


    @GET("api/seller/product/show")
    fun fetchProducts(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Product>>

    @GET("api/product/{name}/search")
    fun searchProducts(
        @Header("Authorization") token : String,
        @Path("name") name : String
    ) : Call<WrappedListResponse<Product>>

    @Multipart
    @POST("api/seller/product/store")
    fun createProduct(
        @Header("Authorization") token : String,
        @PartMap json : HashMap<String, RequestBody>,
        @Part images : Array<MultipartBody.Part?>) : Call<WrappedResponse<Product>>

    @Headers("Content-Type: application/json")
    @POST("api/seller/product/{id}/update")
    fun updateProduct(
        @Header("Authorization") token : String,
        @Path("id") id : Int,
        @Body requestBody: RequestBody
    ) : Call<WrappedResponse<Product>>

    @Multipart
    @POST("api/seller/product/{id}/update/photo")
    fun updatePhotoProduct(
        @Header("Authorization") token : String,
        @Path("id") id : Int,
        @Part image : MultipartBody.Part
    ) :Call<WrappedResponse<Product>>

    @GET("api/seller/product/{id}/delete")
    fun deleteProduct(
        @Header("Authorization") token : String,
        @Path("id") id : Int
    ) :Call<WrappedResponse<Product>>

    @GET("api/seller/order/orderin")
    fun fetchOrderIn(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Order>>

    @GET("api/seller/order/inprogress")
    fun fetchOrderInProgress(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Order>>

    @GET("api/seller/order/complete")
    fun fetchOrderComplete(
        @Header("Authorization") token : String
    ) : Call<WrappedListResponse<Order>>

    @GET("api/seller/order/{id}/completed")
    fun orderComplete(
        @Header("Authorization") token : String,
        @Path("id") id : Int
    ) : Call<WrappedResponse<Order>>

    @GET("api/seller/order/{id}/decline")
    fun orderCancel(
        @Header("Authorization") token : String,
        @Path("id") id : Int
    ) : Call<WrappedResponse<Order>>

    @GET("api/seller/order/{id}/confirmed")
    fun orderConfirmed(
        @Header("Authorization") token : String,
        @Path("id") id : Int
    ) : Call<WrappedResponse<Order>>

}
data class WrappedResponse<T>(
    @SerializedName("message") var message : String?,
    @SerializedName("status") var status : Boolean?,
    @SerializedName("data") var data : T?
)

data class WrappedListResponse<T>(
    @SerializedName("message") var message : String?,
    @SerializedName("status") var status : Boolean?,
    @SerializedName("data") var data : List<T>?
)