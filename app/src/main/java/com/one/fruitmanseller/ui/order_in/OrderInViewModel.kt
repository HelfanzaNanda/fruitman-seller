package com.one.fruitmanseller.ui.order_in

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.one.fruitmanseller.models.Order
import com.one.fruitmanseller.repositories.OrderRepository
import com.one.fruitmanseller.utils.ArrayResponse
import com.one.fruitmanseller.utils.SingleLiveEvent
import com.one.fruitmanseller.utils.SingleResponse

class OrderInViewModel (private val orderRepository: OrderRepository) : ViewModel(){
    private val state : SingleLiveEvent<OrderInState> = SingleLiveEvent()
    private val ordersIn = MutableLiveData<List<Order>>()
    private fun setLoading(){ state.value = OrderInState.Loading(true) }
    private fun hideLoading(){ state.value = OrderInState.Loading(false) }
    private fun toast(message: String){ state.value = OrderInState.ShowToast(message) }
    private fun successCancel(){ state.value = OrderInState.SuccessCancel }
    private fun successConfirm(){ state.value = OrderInState.SuccessConfirm }

    fun fetchOrderIn(token : String){
        setLoading()
        orderRepository.fetchOrderIn(token, object : ArrayResponse<Order>{
            override fun onSuccess(datas: List<Order>?) {
                hideLoading()
                datas?.let { ordersIn.postValue(it) }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun cancel(token: String, id : String){
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

    fun confirm(token: String, id : String){
        setLoading()
        orderRepository.confirm(token, id, object : SingleResponse<Order> {
            override fun onSuccess(data: Order?) {
                hideLoading()
                data?.let { successConfirm() }
            }

            override fun onFailure(err: Error) {
                hideLoading()
                toast(err.message.toString())
            }
        })
    }

    fun listenToState() = state
    fun listenToOrdersIn() = ordersIn
}

sealed class OrderInState{
    data class Loading(var state : Boolean = false) : OrderInState()
    data class ShowToast(var message : String) : OrderInState()
    object SuccessCancel : OrderInState()
    object SuccessConfirm : OrderInState()
}