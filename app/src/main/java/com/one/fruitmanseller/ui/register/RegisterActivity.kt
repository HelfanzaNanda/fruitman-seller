package com.one.fruitmanseller.ui.register

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import com.one.fruitmanseller.R
import com.one.fruitmanseller.models.RegisterSeller
import com.one.fruitmanseller.utils.extensions.AlertRegister
import com.one.fruitmanseller.utils.extensions.showToast
import kotlinx.android.synthetic.main.activity_register.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegisterActivity : AppCompatActivity() {

    private val registerViewModel : RegisterViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        supportActionBar?.hide()
        observe()
        register()
    }

    private fun register() {
        btn_register.setOnClickListener {
            val name = et_name.text.toString().trim()
            val email = et_email.text.toString().trim()
            val password = et_password.text.toString().trim()
            val confirmPass = et_confirm_password.text.toString().trim()
            val phone = et_phone.text.toString().trim()
            if (registerViewModel.validate(name, email, password, confirmPass, phone)){
                val registerSeller = RegisterSeller(name = name, email = email, phone = phone, password = password)
                registerViewModel.register(registerSeller)
            }
        }
    }

    private fun observe() {
        registerViewModel.listenToState().observer(this, Observer { handleUiState(it) })
    }

    private fun handleUiState(it: RegisterState) {
        when(it){
            is RegisterState.Loading -> handleLoading(it.state)
            is RegisterState.ShowToast -> showToast(it.message)
            is RegisterState.Success -> handleSuccess(it.email)
            is RegisterState.Validate -> handleValidate(it)
            is RegisterState.Reset -> handleReset()
        }
    }

    private fun handleReset() {
        setNameError(null)
        setEmailError(null)
        setPasswordError(null)
        setConfirmPasswordError(null)
        setPhoneError(null)
    }

    private fun handleValidate(validate: RegisterState.Validate) {
        validate.name?.let { setNameError(it) }
        validate.email?.let { setEmailError(it) }
        validate.password?.let { setPasswordError(it) }
        validate.confirmPass?.let { setConfirmPasswordError(it) }
        validate.phone?.let { setPhoneError(it) }
    }

    private fun handleSuccess(email: String) = alert(email)

    private fun alert(message : String){
        AlertDialog.Builder(this).apply {
            setMessage(message)
            setPositiveButton("ya"){dialog, _ ->
                dialog.dismiss()
                finish()
            }
        }.show()
    }

    private fun handleLoading(state: Boolean) {
        btn_register.isEnabled = !state
    }

    private fun setNameError(err : String?){ in_name.error = err }
    private fun setEmailError(err : String?){ in_email.error = err }
    private fun setPasswordError(err : String?){ in_password.error = err }
    private fun setConfirmPasswordError(err : String?){ in_password.error = err }
    private fun setPhoneError(err : String?){ in_phone.error = err }
}
