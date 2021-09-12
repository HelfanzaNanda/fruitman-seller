package com.one.fruitmanseller.ui.main.timeline

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.one.fruitmanseller.R
import com.one.fruitmanseller.models.Product
import com.one.fruitmanseller.models.Seller
import com.one.fruitmanseller.ui.premium.PremiumActivity
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
        setURecyclerView()
        goToAddProductActivity()
        observe()
    }

    private fun goToAddProductActivity(){
        requireView().fab_add_product.setOnClickListener {
            if (!isPremium()){
                if(isOverload()){
                    alertOverload()
                }else{
                    startActivity(Intent(requireActivity(), ProductActivity::class.java))
                }
            }else{
                startActivity(Intent(requireActivity(), ProductActivity::class.java))
            }
        }
    }

    private fun alertOverload(){
        AlertDialog.Builder(requireActivity()).apply {
            setMessage("maaf untuk free hanya bisa menambahkan 2 produk saja, jika ingin lebih silahkan upgrade premium")
            setPositiveButton("premium"){dialog, which ->
                dialog.dismiss()
                startActivity(Intent(requireActivity(), PremiumActivity::class.java))
            }
            setNegativeButton("tidak"){dialog, which ->
                dialog.dismiss()
            }
        }.show()
    }


    private fun setURecyclerView() {
        requireView().rv_timeline.apply {
            adapter = TimelineAdapter(mutableListOf(), requireActivity())
            layoutManager = GridLayoutManager(requireActivity(), 2)
        }
    }

    private fun observe() {
        observeState()
        observeProducts()
        observeCurrentUser()
    }

    private fun observeState() = timelineViewModel.listenToState().observer(viewLifecycleOwner, Observer { handleUiState(it) })
    private fun observeProducts() = timelineViewModel.listenToProducts().observe(viewLifecycleOwner, Observer { handleProducts(it) })

    private fun handleProducts(list: List<Product>?) {
        list?.let {
            if (it.size == 2) Constants.setOverload(requireActivity(), true)
            println("total ${it.size}")
            requireView().rv_timeline.adapter?.let { adapter ->
                if (adapter is TimelineAdapter){
                    adapter.changelist(it)
                }
            }
        }
    }

    private fun observeCurrentUser() = timelineViewModel.listenToCurrentUser().observe(viewLifecycleOwner, Observer { handleCurrentUser(it) })

    private fun handleCurrentUser(it: Seller?) {
        it?.let { user->
            requireView().textUsername.text = user.name
        }
    }


    private fun handleUiState(state: TimelineState?) {
        state?.let {
            when(it){
                is TimelineState.Loading -> handleLoading(it.state)
                is TimelineState.Showtoast -> requireActivity().showToast(it.message)
            }
        }
    }

    private fun handleLoading(state: Boolean) {
        if (state)requireView().loading.visible() else requireView().loading.gone()
    }

    private fun fetchProducts() = timelineViewModel.fetchPrducts(Constants.getToken(requireActivity()))
    private fun getCurrentUser() = timelineViewModel.getCurrentUser(Constants.getToken(requireActivity()))
    private fun isOverload() = Constants.getOverload(requireActivity())
    private fun isPremium() = Constants.getPremium(requireActivity())

    override fun onResume() {
        super.onResume()
        fetchProducts()
        getCurrentUser()
    }
}