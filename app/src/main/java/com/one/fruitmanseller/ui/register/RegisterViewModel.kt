package com.one.fruitmanseller.ui.register

import androidx.lifecycle.ViewModel
import com.one.fruitmanseller.models.RegisterSeller
import com.one.fruitmanseller.repositories.SellerRepository
import com.one.fruitmanseller.utils.Constants
import com.one.fruitmanseller.utils.SingleLiveEvent
import com.one.fruitmanseller.utils.SingleResponse

class RegisterViewModel(private val sellerRepository: SellerRepository) : ViewModel(){
    private val state : SingleLiveEvent<RegisterState> = SingleLiveEvent()

    private fun setLoading() { state.value = RegisterState.Loading(true) }
    private fun hideLoading() { state.value = RegisterState.Loading(false) }
    private fun toast(message: String){ state.value = RegisterState.ShowToast(message) }
    private fun success(email: String) { state.value = RegisterState.Success(email) }

    fun validate(name: String, email: String, password: String, confirmPass: String, phone: String) : Boolean {
        state.value = RegisterState.Reset
        if (name.isEmpty()){
            state.value = RegisterState.Validate(name = "nama tidak boleh kosong")
            return false
        }

        if (name.length < 5){
            state.value = RegisterState.Validate(name= "nama setidaknya 5 karakter")
            return false
        }

        if (email.isEmpty()){
            state.value = RegisterState.Validate(email = "email tidak boleh kosong")
            return false
        }

        if (!Constants.isValidEmail(email)){
            state.value = RegisterState.Validate(email = "email tidak valid")
            return false
        }

        if (phone.isEmpty()){
            state.value = RegisterState.Validate(phone = "no telepon tidak boleh kosong")
            return false
        }

        if (phone.length < 10 || phone.length > 13){
            state.value = RegisterState.Validate(phone = "no telepon setidaknya 10 sampai 13 karakter")
            return false
        }

        if (password.isEmpty()){
            state.value = RegisterState.Validate(password = "password tidak boleh kosong")
            return false
        }

        if (!Constants.isValidPassword(password)){
            state.value = RegisterState.Validate(password = "password minimal 8 karakter")
            return false
        }

        if (confirmPass.isEmpty()){
            state.value = RegisterState.Validate(confirmPass = "konfirmasi password tidak boleh kosong")
            return false
        }

        if(!confirmPass.equals(password)){
            state.value = RegisterState.Validate(confirmPass = "konfirmasi password tidak cocok")
            return false
        }
        return true
    }

    fun register(registerSeller: RegisterSeller){
        setLoading()
        sellerRepository.register(registerSeller, object : SingleResponse<RegisterSeller>{
            override fun onSuccess(data: RegisterSeller?) {
                hideLoading()
                success(data!!.email!!)
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }

        })
    }

    fun listenToState() = state

}

sealed class RegisterState{
    data class Loading(var state : Boolean = false) : RegisterState()
    data class ShowToast(var message : String) : RegisterState()
    data class Success(var email : String) : RegisterState()
    data class Validate(
        var name : String? = null,
        var email : String? = null,
        var password : String? = null,
        var confirmPass : String? = null,
        var phone : String? = null
    ) : RegisterState()
    object Reset : RegisterState()
}