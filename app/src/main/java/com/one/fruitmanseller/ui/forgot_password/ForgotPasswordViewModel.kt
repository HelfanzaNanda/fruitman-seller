package com.one.fruitmanseller.ui.forgot_password

import androidx.lifecycle.ViewModel
import com.one.fruitmanseller.models.Seller
import com.one.fruitmanseller.repositories.SellerRepository
import com.one.fruitmanseller.utils.Constants
import com.one.fruitmanseller.utils.SingleLiveEvent
import com.one.fruitmanseller.utils.SingleResponse

class ForgotPasswordViewModel (private val sellerRepository: SellerRepository) : ViewModel(){
    private val state : SingleLiveEvent<ForgotPasswordState> = SingleLiveEvent()

    private fun setLoading(){ state.value = ForgotPasswordState.Loading(true) }
    private fun hideLoading(){ state.value = ForgotPasswordState.Loading(false) }
    private fun toast(message: String){ state.value = ForgotPasswordState.ShowToast(message) }
    private fun success(email: String) { state.value = ForgotPasswordState.Success(email) }
    private fun reset() { state.value = ForgotPasswordState.Reset }

    fun validate(email: String) : Boolean {
        reset()
        if (email.isEmpty()){
            state.value = ForgotPasswordState.Validate(email = "email tidak boleh kosong")
            return false
        }
        if (!Constants.isValidEmail(email)){
            state.value = ForgotPasswordState.Validate(email = "email tidak valid")
            return false
        }
        return true
    }

    fun forgotPassword(email: String){
        setLoading()
        sellerRepository.forgotPassword(email, object : SingleResponse<Seller> {
            override fun onSuccess(data: Seller?) {
                hideLoading()
                data?.let { success(email) }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
}

sealed class ForgotPasswordState{
    data class Loading(var state : Boolean = false) : ForgotPasswordState()
    data class ShowToast(var message : String) : ForgotPasswordState()
    object Reset : ForgotPasswordState()
    data class Success(var email: String) : ForgotPasswordState()
    data class Validate (var email : String?= null) : ForgotPasswordState()
}