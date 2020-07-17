package com.one.fruitmanseller.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("buyer") var buyer : Buyer,
    @SerializedName("product") var product: Product,
    @SerializedName("offer_price") var offer_price : Int? = 0,
    @SerializedName("status") var status : String? = null,
    @SerializedName("arrive") var arrive : Boolean? = false,
    @SerializedName("complete") var complete : Boolean? = false
) : Parcelable