package com.one.fruitmanseller.ui.order_in

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.one.fruitmanseller.R
import com.one.fruitmanseller.models.Order
import com.one.fruitmanseller.utils.Constants
import com.one.fruitmanseller.utils.extensions.gone
import com.one.fruitmanseller.utils.extensions.showToast
import com.one.fruitmanseller.utils.extensions.visible

import kotlinx.android.synthetic.main.activity_order_in.*
import kotlinx.android.synthetic.main.content_order_in.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class OrderInActivity : AppCompatActivity() {

    private val orderInViewModel : OrderInViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_order_in)
        setSupportActionBar(toolbar)

        setUpUI()
        observer()
        observe()
    }

    private fun setUpUI() {
        recyclerView.apply {
            adapter = OrderInAdapter(mutableListOf(), this@OrderInActivity, orderInViewModel)
            layoutManager = LinearLayoutManager(this@OrderInActivity)
        }
    }

    private fun observer() =  orderInViewModel.listenToState().observer(this, Observer { handleUiState(it) })

    private fun handleUiState(it: OrderInState) {
        when(it){
            is OrderInState.Loading -> handleLoading(it.state)
            is OrderInState.ShowToast -> showToast(it.message)
            is OrderInState.SuccessCancel -> handleSuccessCancel()
            is OrderInState.SuccessConfirm -> handleSuccessConfirm()
        }
    }

    private fun handleSuccessConfirm() {
        showToast("berhasil mengkonfirmasi pesanan")
        orderInViewModel.fetchOrderIn(Constants.getToken(this@OrderInActivity))
    }

    private fun handleSuccessCancel() {
        showToast("berhasil menolak pesanan")
        orderInViewModel.fetchOrderIn(Constants.getToken(this@OrderInActivity))
    }

    private fun handleLoading(state: Boolean) = if (state) loading.visible() else loading.gone()

    private fun observe() = orderInViewModel.listenToOrdersIn().observe(this, Observer { handleOrdersIn(it) })

    private fun handleOrdersIn(it: List<Order>) {
        recyclerView.adapter?.let { adapter ->
            if (adapter is OrderInAdapter){
                adapter.changelist(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        orderInViewModel.fetchOrderIn(Constants.getToken(this@OrderInActivity))
    }

}
