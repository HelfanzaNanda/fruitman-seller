package com.one.fruitmanseller.ui.update_profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import coil.api.load
import com.fxn.pix.Pix
import com.one.fruitmanseller.R
import com.one.fruitmanseller.models.Seller
import com.one.fruitmanseller.utils.Constants
import com.one.fruitmanseller.utils.extensions.showToast

import kotlinx.android.synthetic.main.activity_update_profil.*
import kotlinx.android.synthetic.main.content_update_profil.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class UpdateProfilActivity : AppCompatActivity() {

    private val updateProfilViewModel : UpdateProfilViewModel by viewModel()
    private var imgUrl = ""
    private val REQ_CODE_PIX = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_profil)
        setSupportActionBar(toolbar)
        openPick()
        observe()
        updateProfil()
    }

    private fun openPick() = btn_add_image.setOnClickListener { Pix.start(this, REQ_CODE_PIX) }

    private fun updateProfil() {
        btn_update.setOnClickListener {
            val token = Constants.getToken(this@UpdateProfilActivity)
            val name = et_name.text.toString().trim()
            val address = et_address.text.toString().trim()
            //val password = et_password.text.toString().trim()
            val phone = et_phone.text.toString().trim()
            val seller = Seller(name = name, address = address, phone = phone)
            updateProfilViewModel.updateProfile(token, seller, imgUrl)
        }
    }

    private fun observe() {
        observeState()
        observeSeller()
    }

    private fun observeState() = updateProfilViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun observeSeller() = updateProfilViewModel.listenCurrentUser().observe(this, Observer { handleSeller(it) })

    private fun handleSeller(seller: Seller?) {
        seller?.let {
            et_name.setText(it.name)
            et_email.setText(it.email)
            et_address.setText(it.address)
            et_phone.setText(it.phone)
            iv_seller.load(it.image)
        }
    }

    private fun handleUiState(it: UpdateProfilState) {
        when(it){
            is UpdateProfilState.Loading -> handleLoading(it.state)
            is UpdateProfilState.ShowToast -> showToast(it.message)
            is UpdateProfilState.Success -> handleSuccess()
        }
    }

    private fun handleLoading(state: Boolean) {
        btn_update.isEnabled = !state
    }

    private fun handleSuccess() {
        finish()
        showToast("berhasil mengupdate data")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == REQ_CODE_PIX && resultCode == Activity.RESULT_OK && data != null){
            val selectedImageUri = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
            selectedImageUri?.let {
                iv_seller.load(File(it[0]))
                imgUrl = it[0]
            }
        }
    }

    private fun fetchCurrentUser() = updateProfilViewModel.currentUser(Constants.getToken(this@UpdateProfilActivity))

    override fun onResume() {
        super.onResume()
        fetchCurrentUser()
    }
}
