package com.one.fruitmanseller.repositories

import com.google.firebase.iid.FirebaseInstanceId
import com.one.fruitmanseller.utils.SingleResponse

interface FirebaseContract{
    fun generateToken(listener : SingleResponse<String>)
}

class FirebaseRepository : FirebaseContract{
    override fun generateToken(listener: SingleResponse<String>) {
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            when {
                it.isSuccessful -> {
                    it.result?.let {result->
                        listener.onSuccess(result.token)
                    } ?: kotlin.run { listener.onFailure(Error("Failed to get firebase token")) }
                }
                else -> listener.onFailure(Error("Cannot get firebase token"))
            }
        }
    }

}