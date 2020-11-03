package com.one.fruitmanseller.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("name") var name : String? = null,
    //@SerializedName("image") var image : String? = null,
    @SerializedName ("description") var description : String? = null,
    @SerializedName ("address") var address : String? = null,
    @SerializedName ("price") var price : Int? = null,
    @SerializedName("lat") var lat : String? = null,
    @SerializedName("lng") var lng : String? = null,
    @SerializedName ("status") var status : String? = null,
    @SerializedName ("subdistrict_id") var subdistrict_id : String? = null,
    @SerializedName ("fruit_id") var fruit_id : String? = null,
    @SerializedName("images") var images : MutableList<ProductImage> = mutableListOf()
) : Parcelable

@Parcelize
data class ProductImage(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("image") var image : String? = null
) : Parcelable