package com.one.fruitmanseller.ui.login

import androidx.lifecycle.ViewModel
import com.one.fruitmanseller.models.Seller
import com.one.fruitmanseller.repositories.SellerRepository
import com.one.fruitmanseller.utils.Constants
import com.one.fruitmanseller.utils.SingleLiveEvent
import com.one.fruitmanseller.utils.SingleResponse

class LoginViewModel (private val sellerRepository: SellerRepository) : ViewModel(){
    private val state : SingleLiveEvent<LoginState> = SingleLiveEvent()
    private fun setLoading() { state.value = LoginState.Loading(true) }
    private fun hideLoading() { state.value = LoginState.Loading(false) }
    private fun toast(message: String){ state.value = LoginState.ShowToast(message) }
    private fun success(token: String){ state.value = LoginState.Success(token) }

    fun validate(email: String, password : String) : Boolean{
        state.value = LoginState.Reset
        if (email.isEmpty()){
            state.value = LoginState.Validate(email = "email tidak boleh kosong")
            return false
        }
        if(!Constants.isValidEmail(email)){
            state.value = LoginState.Validate(email = "Email tidak valid")
            return false
        }
        if (password.isEmpty()){
            state.value = LoginState.Validate(password = "password tidak boleh kosong")
            return false
        }
        if(!Constants.isValidPassword(password)){
            state.value = LoginState.Validate(password = "Password setidaknya delapan karakter")
            return false
        }
        return true
    }

    fun login(email: String, password: String){
        setLoading()
        sellerRepository.login(email, password, object : SingleResponse<Seller>{
            override fun onSuccess(data: Seller?) {
                hideLoading()
                success(data!!.token!!)
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }

        })
    }

    fun listenToState() = state
}

sealed class LoginState{
    data class Loading(var state : Boolean = false) : LoginState()
    data class ShowToast(var message : String) : LoginState()
    data class Success(var token : String) : LoginState()
    data class Validate(var email : String? = null, var password : String? = null) : LoginState()
    object Reset : LoginState()
}