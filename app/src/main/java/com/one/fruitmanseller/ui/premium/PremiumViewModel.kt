package com.one.fruitmanseller.ui.premium

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanseller.models.Bank
import com.one.fruitmanseller.models.Seller
import com.one.fruitmanseller.repositories.BankRepository
import com.one.fruitmanseller.repositories.SellerRepository
import com.one.fruitmanseller.utils.SingleLiveEvent
import com.one.fruitmanseller.utils.SingleResponse

class PremiumViewModel (private val bankRepository: BankRepository,
                        private val sellerRepository: SellerRepository) : ViewModel(){
    private val state : SingleLiveEvent<BankState> = SingleLiveEvent()
    private val bank = MutableLiveData<Bank>()

    private fun isLoading(b : Boolean){ state.value = BankState.Loading(b) }
    private fun toast(m : String){ state.value = BankState.ShowToast(m) }
    private fun success(){ state.value = BankState.Success }

    fun fetchBank(){
        isLoading(true)
        bankRepository.fetchBank(object : SingleResponse<Bank>{
            override fun onSuccess(data: Bank?) {
                data?.let {
                    isLoading(false)
                    bank.postValue(it)
                }
            }

            override fun onFailure(err: Error) {
                isLoading(false)
                toast(err.message.toString())
            }

        })
    }

    fun premium(token : String, image : String){
        isLoading(true)
        sellerRepository.premium(token, image, object : SingleResponse<Seller>{
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
    fun listenToBank() = bank
}

sealed class BankState{
    data class ShowToast(var message : String) : BankState()
    data class Loading(var state : Boolean = false) : BankState()
    object Success : BankState()
}