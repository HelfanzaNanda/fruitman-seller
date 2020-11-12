package com.one.fruitmanseller.ui.main.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanseller.models.Seller
import com.one.fruitmanseller.repositories.SellerRepository
import com.one.fruitmanseller.utils.SingleLiveEvent
import com.one.fruitmanseller.utils.SingleResponse

class ProfileViewModel (private val sellerRepository: SellerRepository) : ViewModel(){
    private val state : SingleLiveEvent<ProfileState> = SingleLiveEvent()
    private val user  = MutableLiveData<Seller>()

    private fun isLoading(b : Boolean) { state.value = ProfileState.Loading(b) }
    private fun toast(message: String){ state.value = ProfileState.ShowToast(message) }

    fun profile(token : String){
        isLoading(true)
        sellerRepository.profile(token, object : SingleResponse<Seller>{
            override fun onSuccess(data: Seller?) {
                isLoading(false)
                data?.let { user.postValue(it) }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToUser() = user

}

sealed class ProfileState{
    data class Loading(var state : Boolean = false) : ProfileState()
    data class ShowToast(var message : String) : ProfileState()
}