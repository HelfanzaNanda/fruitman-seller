package com.one.fruitmanseller.ui.in_progress

import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.one.fruitmanseller.R
import com.one.fruitmanseller.models.Order
import com.one.fruitmanseller.ui.order_in.OrderInState
import com.one.fruitmanseller.utils.Constants
import com.one.fruitmanseller.utils.extensions.gone
import com.one.fruitmanseller.utils.extensions.showToast
import com.one.fruitmanseller.utils.extensions.visible

import kotlinx.android.synthetic.main.activity_in_progress.*
import kotlinx.android.synthetic.main.content_in_progress.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class InProgressActivity : AppCompatActivity() {

    private val inProgressViewModel : InProgressViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_in_progress)
        setSupportActionBar(toolbar)

        setUpUI()
        observer()
        observe()

    }

    private fun setUpUI() {
        recyclerView.apply {
            adapter = InProgressAdapter(mutableListOf(),this@InProgressActivity, inProgressViewModel)
            layoutManager = LinearLayoutManager(this@InProgressActivity)
        }
    }

    private fun observer() = inProgressViewModel.listenToState().observer(this, Observer { HandleUiState(it) })

    private fun HandleUiState(it: InProgressState) {
        when(it){
            is InProgressState.Loading -> handleLoading(it.state)
            is InProgressState.ShowToast ->showToast(it.message)
            is InProgressState.SuccessCancel -> handleSuccessCancel()
            is InProgressState.SuccessComplete -> handleSuccessComplete()
        }
    }

    private fun handleLoading(state: Boolean) = if (state) loading.visible() else loading.gone()

    private fun handleSuccessComplete() {
        showToast("pesanan complete")
        inProgressViewModel.fetchInProgress(Constants.getToken(this@InProgressActivity))
    }

    private fun handleSuccessCancel() {
        showToast("berhasil menolak pesanan")
        inProgressViewModel.fetchInProgress(Constants.getToken(this@InProgressActivity))
    }

    private fun observe() =  inProgressViewModel.listenToOrdersInProgress().observe(this, Observer { HandleOrdersInProgress(it) })

    private fun HandleOrdersInProgress(it: List<Order>) {
        recyclerView.adapter?.let { adapter ->
            if (adapter is InProgressAdapter){
                adapter.changelist(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        inProgressViewModel.fetchInProgress(Constants.getToken(this@InProgressActivity))
    }

}
