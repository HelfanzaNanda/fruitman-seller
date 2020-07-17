package com.one.fruitmanseller.ui.main.timeline

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.fxn.pix.Pix
import com.one.fruitmanseller.R
import com.one.fruitmanseller.models.Product
import com.one.fruitmanseller.ui.product.ProductActivity
import com.one.fruitmanseller.utils.Constants
import com.one.fruitmanseller.utils.extensions.gone
import com.one.fruitmanseller.utils.extensions.showToast
import com.one.fruitmanseller.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_timeline.view.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class TimelineFragment : Fragment(R.layout.fragment_timeline){

    private val timelineViewModel : TimelineViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUi()
        requireView().fab_add_product.setOnClickListener {
            startActivity(Intent(requireActivity(), ProductActivity::class.java))
        }
        observer()
        observe()
    }

    private fun setUpUi() {
        requireView().rv_timeline.apply {
            adapter = TimelineAdapter(mutableListOf(), requireActivity())
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun observe() {
        timelineViewModel.listenToProducts().observe(viewLifecycleOwner, Observer { handleProducts(it) })
    }

    private fun observer() {
        timelineViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUiState(it) })
    }

    private fun handleUiState(it: TimelineState) {
        when(it){
            is TimelineState.Loading -> handleLoading(it.state)
            is TimelineState.Showtoast -> requireActivity().showToast(it.message)
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state)requireView().loading.visible() else requireView().loading.gone()
    }

    private fun handleProducts(it: List<Product>) {
        requireView().rv_timeline.adapter?.let { adapter ->
            if (adapter is TimelineAdapter){
                adapter.changelist(it)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        timelineViewModel.fetchPrducts(Constants.getToken(requireActivity()))
    }
}