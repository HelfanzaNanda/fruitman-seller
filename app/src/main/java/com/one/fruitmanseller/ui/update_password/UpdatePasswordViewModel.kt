package com.one.fruitmanseller.ui.update_password

import androidx.lifecycle.ViewModel
import com.one.fruitmanseller.models.Seller
import com.one.fruitmanseller.repositories.SellerRepository
import com.one.fruitmanseller.utils.SingleLiveEvent
import com.one.fruitmanseller.utils.SingleResponse

class UpdatePasswordViewModel (private val sellerRepository: SellerRepository) : ViewModel(){
    private val state : SingleLiveEvent<UpdatePasswordState> = SingleLiveEvent()

    private fun isLoading(b : Boolean){ state.value = UpdatePasswordState.Loading(b) }
    private fun toast(m : String){ state.value = UpdatePasswordState.ShowToast(m) }
    private fun success(){ state.value = UpdatePasswordState.Success }

    fun updatePassword(token : String, password : String){
        isLoading(true)
        sellerRepository.updatePassword(token, password, object : SingleResponse<Seller> {
            override fun onSuccess(data: Seller?) {
                isLoading(false)
                data?.let { success() }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
}

sealed class UpdatePasswordState{
    data class Loading(var state : Boolean = false) : UpdatePasswordState()
    data class ShowToast(var message : String) : UpdatePasswordState()
    object Success : UpdatePasswordState()
}