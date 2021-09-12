package com.one.fruitmanseller.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize


@Parcelize
data class Bank(
    @SerializedName("bank_name") var bank_name : String? = null,
    @SerializedName("account_name") var account_name : String? = null,
    @SerializedName("account_number") var account_number : String? = null
) : Parcelable