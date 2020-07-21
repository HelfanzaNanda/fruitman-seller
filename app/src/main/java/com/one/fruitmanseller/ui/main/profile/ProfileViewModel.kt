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

    private fun setLoading() { state.value = ProfileState.Loading(true) }
    private fun hideLoading() { state.value = ProfileState.Loading(false) }
    private fun toast(message: String){ state.value = ProfileState.ShowToast(message) }

    fun profile(token : String){
        setLoading()
        sellerRepository.profile(token, object : SingleResponse<Seller>{
            override fun onSuccess(data: Seller?) {
                hideLoading()
                data?.let { user.postValue(it) }
            }

            override fun onFailure(err: Error) {
                hideLoading()
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