package com.one.fruitmanseller.ui.update_password

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.one.fruitmanseller.R
import com.one.fruitmanseller.utils.Constants
import com.one.fruitmanseller.utils.extensions.gone
import com.one.fruitmanseller.utils.extensions.showToast
import com.one.fruitmanseller.utils.extensions.visible
import kotlinx.android.synthetic.main.content_update_password.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class UpdatePasswordActivity : AppCompatActivity() {

    private val updatePasswordViewModel : UpdatePasswordViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_password)
        supportActionBar?.hide()
        observe()
        updatePassword()
    }



    private fun observe() {
        observeState()
    }

    private fun observeState() = updatePasswordViewModel.listenToState().observe(this, Observer { handleUiState(it) })

    private fun handleUiState(state: UpdatePasswordState?) {
        state?.let {
            when(it){
                is UpdatePasswordState.Loading -> handleLoading(it.state)
                is UpdatePasswordState.ShowToast -> showToast(it.message)
                is UpdatePasswordState.Success -> handleSuccess()
            }
        }
    }

    private fun handleSuccess() {
        AlertDialog.Builder(this).apply {
            setMessage("berhasil")
            setPositiveButton("ya"){dialog, which ->
                dialog.dismiss()
                finish()
            }
        }.show()
    }


    private fun handleLoading(state: Boolean) {
        if (state) btn_update_password.visible() else btn_update_password.gone()
    }

    private fun updatePassword(){
        btn_update_password.setOnClickListener {
            val token = Constants.getToken(this@UpdatePasswordActivity)
            val pass = et_password.text.toString()
            updatePasswordViewModel.updatePassword(token, pass)
        }
    }
}
