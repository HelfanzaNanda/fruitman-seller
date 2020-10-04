package com.one.fruitmanseller.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Fruit(
    @SerializedName("id") var id : Int? = null,
    @SerializedName("name") var name : String? = null
) : Parcelable