package com.one.fruitmanseller.ui.in_progress

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanseller.models.Order
import com.one.fruitmanseller.repositories.OrderRepository
import com.one.fruitmanseller.utils.ArrayResponse
import com.one.fruitmanseller.utils.SingleLiveEvent
import com.one.fruitmanseller.utils.SingleResponse

class InProgressViewModel (private val orderRepository: OrderRepository) : ViewModel(){
    private val state : SingleLiveEvent<InProgressState> = SingleLiveEvent()
    private val ordersInProgress = MutableLiveData<List<Order>>()
    private fun setLoading(){ state.value = InProgressState.Loading(true) }
    private fun hideLoading(){ state.value = InProgressState.Loading(false) }
    private fun toast(message: String){ state.value = InProgressState.ShowToast(message) }
    private fun successComplete(){ state.value = InProgressState.SuccessComplete }
    private fun successCancel(){ state.value = InProgressState.SuccessCancel }

    fun fetchInProgress(token : String){
        setLoading()
        orderRepository.fetchOrderInProgress(token, object : ArrayResponse<Order>{
            override fun onSuccess(datas: List<Order>?) {
                hideLoading()
                datas?.let { ordersInProgress.postValue(it) }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun complete(token: String, id : String){
        setLoading()
        orderRepository.complete(token, id, object : SingleResponse<Order> {
            override fun onSuccess(data: Order?) {
                hideLoading()
                data?.let { successComplete() }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }

        })
    }

    fun cancel(token: String, id : String){
        println(token)
        println(id)
        setLoading()
        orderRepository.cancel(token, id, object : SingleResponse<Order> {
            override fun onSuccess(data: Order?) {
                hideLoading()
                data?.let { successCancel() }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }

        })
    }

    fun listenToState() = state
    fun listenToOrdersInProgress() = ordersInProgress
}

sealed class InProgressState{
    data class Loading(var state : Boolean = false) : InProgressState()
    data class ShowToast(var message : String) : InProgressState()
    object SuccessComplete : InProgressState()
    object SuccessCancel : InProgressState()
}