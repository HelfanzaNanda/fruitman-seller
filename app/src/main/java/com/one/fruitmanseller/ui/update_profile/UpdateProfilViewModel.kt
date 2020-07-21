package com.one.fruitmanseller.ui.update_profile

import androidx.lifecycle.ViewModel
import com.one.fruitmanseller.models.Seller
import com.one.fruitmanseller.repositories.SellerRepository
import com.one.fruitmanseller.utils.Constants
import com.one.fruitmanseller.utils.SingleLiveEvent
import com.one.fruitmanseller.utils.SingleResponse

class UpdateProfilViewModel(private val sellerRepository: SellerRepository) : ViewModel() {
    private val state: SingleLiveEvent<UpdateProfilState> = SingleLiveEvent()
    private fun setLoading() { state.value = UpdateProfilState.Loading(true) }
    private fun hideLoading() { state.value = UpdateProfilState.Loading(false) }
    private fun toast(message: String) { state.value = UpdateProfilState.ShowToast(message) }
    private fun success() { state.value = UpdateProfilState.Success }


    fun validate(name : String, password : String, address : String, phone : String) : Boolean{
        if (name.isEmpty()){
            state.value = UpdateProfilState.Validate(name = "tidak boleh ksoosng")
            return false
        }

        if (!Constants.isAlpha(name)){
            state.value = UpdateProfilState.Validate(name = "tidak boleh ada angka")
            return false
        }

        if (password.isEmpty()){
            state.value = UpdateProfilState.Validate(pass = "tidak boleh ksoosng")
            return false
        }

        if (!Constants.isValidPassword(password)){
            state.value = UpdateProfilState.Validate(pass = "minimal 8 karakter")
            return false
        }

        if (address.isEmpty()){
            state.value = UpdateProfilState.Validate(address = "tidak boleh ksoosng")
            return false
        }

        if (phone.isEmpty()){
            state.value = UpdateProfilState.Validate(phone = "tidak boleh ksoosng")
            return false
        }

        return true
    }

    fun updateProfile(token: String, seller: Seller, pathImage: String) {
        setLoading()
        sellerRepository.updateProfile(token, seller, object : SingleResponse<Seller> {
            override fun onSuccess(data: Seller?) {
                hideLoading()
                data?.let {
                    if (pathImage.isNotEmpty()) {
                        updatePhotoProfile(token, pathImage)
                    } else {
                        success()
                    }
                }

            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }

        })
    }

    private fun updatePhotoProfile(token: String, pathImage: String) {
        sellerRepository.updatePhotoProfile(token, pathImage, object : SingleResponse<Seller> {
            override fun onSuccess(data: Seller?) {
                hideLoading()
                success()
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
}

sealed class UpdateProfilState {
    data class Loading(var state: Boolean = false) : UpdateProfilState()
    data class ShowToast(var message: String) : UpdateProfilState()
    object Success : UpdateProfilState()
    data class Validate(
        var name : String? = null,
        var pass : String? = null,
        var address : String? = null,
        var phone : String? = null
    ) : UpdateProfilState()
    object Reset : UpdateProfilState()
}