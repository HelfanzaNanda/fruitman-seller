package com.one.fruitmanseller.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Seller(
    @SerializedName("id")var id : Int? = null,
    @SerializedName("name")var name : String? = null,
    @SerializedName("email")var email : String? = null,
    @SerializedName("phone")var phone : String? = null,
    @SerializedName("api_token")var token : String? = null,
    @SerializedName("address")var address : String? = null
) : Parcelable

@Parcelize
data class RegisterSeller(
    @SerializedName("id")var id : Int? = null,
    @SerializedName("name")var name : String? = null,
    @SerializedName("email")var email : String? = null,
    @SerializedName("password")var password : String? = null,
    @SerializedName("phone")var phone : String? = null
    //@SerializedName("address")var address : String? = null
) : Parcelable