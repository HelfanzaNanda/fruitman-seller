package com.one.fruitmanseller.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Buyer(
    @SerializedName("id")var id : Int? = null,
    @SerializedName("name")var name : String? = null,
    @SerializedName("email")var email : String? = null,
    @SerializedName("phone")var phone : String? = null,
    @SerializedName("address")var address : String? = null
) : Parcelable
