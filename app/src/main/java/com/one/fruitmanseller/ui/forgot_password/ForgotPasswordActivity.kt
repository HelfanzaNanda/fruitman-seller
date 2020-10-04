package com.one.fruitmanseller.ui.forgot_password

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.one.fruitmanseller.R
import com.one.fruitmanseller.utils.extensions.showToast
import kotlinx.android.synthetic.main.activity_forgot_password.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class ForgotPasswordActivity : AppCompatActivity() {

    private val forgotPasswordViewModel : ForgotPasswordViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        supportActionBar?.hide()
        observer()
        sendForgotPassword()
    }

    private fun observer() = forgotPasswordViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    private fun setErrorEmail(err : String?) { in_email.error = err }

    private fun handleUiState(it: ForgotPasswordState) {
        when(it){
            is ForgotPasswordState.Loading -> handleLoading(it.state)
            is ForgotPasswordState.ShowToast -> showToast(it.message)
            is ForgotPasswordState.Reset -> handleReset()
            is ForgotPasswordState.Validate -> handleValidate(it)
            is ForgotPasswordState.Success -> handleSuccess(it.email)
        }
    }

    private fun handleValidate(it: ForgotPasswordState.Validate) = setErrorEmail(it.email)
    private fun handleReset() = setErrorEmail(null)
    private fun handleSuccess(email: String) = alert("kami telah mengirimkan ke $email, untuk forgot password")

    private fun handleLoading(state: Boolean) {
        loading.isIndeterminate = !state
        btn_forget_password.isEnabled = !state
    }

    private fun sendForgotPassword(){
        btn_forget_password.setOnClickListener {
            val email = et_email.text.toString().trim()
            if (forgotPasswordViewModel.validate(email)){
                forgotPasswordViewModel.forgotPassword(email)
            }
        }
    }

    private fun alert(message: String) {
        AlertDialog.Builder(this).apply {
            setMessage(message)
            setPositiveButton("ya"){dialog, _ ->
                dialog.dismiss()
                finish()
            }
        }.show()
    }
}
