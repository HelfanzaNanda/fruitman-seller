package com.one.fruitmanseller.ui.main.profile

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.one.fruitmanseller.R
import com.one.fruitmanseller.models.Seller
import com.one.fruitmanseller.ui.login.LoginActivity
import com.one.fruitmanseller.ui.update_profile.UpdateProfilActivity
import com.one.fruitmanseller.utils.Constants
import com.one.fruitmanseller.utils.extensions.gone
import com.one.fruitmanseller.utils.extensions.showToast
import com.one.fruitmanseller.utils.extensions.visible
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ProfileFragment : Fragment(R.layout.fragment_profile){

    private val profileViewModel : ProfileViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observer()
        observe()
        logout()
    }

    private fun observer() = profileViewModel.listenToState().observer(requireActivity(), Observer { handleUiState(it) })

    private fun handleUiState(it: ProfileState) {
        when(it){
            is ProfileState.Loading -> handleLoading(it.state)
            is ProfileState.ShowToast -> requireActivity().showToast(it.message)
        }
    }

    private fun handleLoading(state: Boolean) = if (state) loading.visible() else loading.gone()

    private fun observe() = profileViewModel.listenToUser().observe(requireActivity(), Observer { handleUser(it) })

    private fun handleUser(seller: Seller) {
        txt_name.text = seller.name
        txt_email.text = seller.email
        txt_address.text = seller.address
        txt_no_hp.text = seller.phone

        btn_update_profile.setOnClickListener {
            startActivity(Intent(requireActivity(), UpdateProfilActivity::class.java).apply {
                putExtra("SELLER", seller)
            })
        }
    }

    private fun logout(){
        btn_logout.setOnClickListener {
            popup("apakah anda yakin?")
        }
    }

    private fun popup(message: String) {
        AlertDialog.Builder(requireActivity()).apply {
            setMessage(message)
            setPositiveButton("ya"){dialog, _ ->
                dialog.dismiss()
                Constants.clearToken(requireActivity())
                startActivity(Intent(requireActivity(), LoginActivity::class.java))
                requireActivity().finish()
            }
        }.show()
    }

    override fun onResume() {
        super.onResume()
        profileViewModel.profile(Constants.getToken(requireActivity()))
    }
}