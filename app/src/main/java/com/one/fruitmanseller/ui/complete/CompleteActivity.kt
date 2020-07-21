package com.one.fruitmanseller.ui.complete

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

import kotlinx.android.synthetic.main.activity_complete.*
import kotlinx.android.synthetic.main.content_complete.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class CompleteActivity : AppCompatActivity() {

    private val completeViewModel : CompleteViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_complete)
        setSupportActionBar(toolbar)

        setUpUI()
        observer()
        observe()
    }

    private fun setUpUI() {
        recyclerView.apply {
            adapter = CompleteAdapter(mutableListOf(), this@CompleteActivity)
            layoutManager = LinearLayoutManager(this@CompleteActivity)
        }
    }

    private fun observer() = completeViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun observe() = completeViewModel.listenToOrdersComplete().observe(this, Observer { handleOrdersComplete(it) })

    private fun handleOrdersComplete(it: List<Order>) {
        recyclerView.adapter?.let { adapter ->
            if (adapter is CompleteAdapter){
                adapter.changelist(it)
            }
        }
    }

    private fun handleUiState(it: CompleteState) {
        when(it){
            is CompleteState.Loading -> handleLoading(it.state)
            is CompleteState.ShowToast -> showToast(it.message)
        }
    }

    private fun handleLoading(state: Boolean) = if (state) loading.visible() else loading.gone()

    override fun onResume() {
        super.onResume()
        completeViewModel.fetchComplete(Constants.getToken(this@CompleteActivity))
    }
}
