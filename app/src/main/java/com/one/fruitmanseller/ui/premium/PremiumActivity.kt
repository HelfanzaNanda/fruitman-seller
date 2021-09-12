package com.one.fruitmanseller.ui.premium

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import coil.api.load
import com.fxn.pix.Options
import com.fxn.pix.Pix
import com.one.fruitmanseller.R
import com.one.fruitmanseller.models.Bank
import com.one.fruitmanseller.utils.Constants
import com.one.fruitmanseller.utils.extensions.showToast
import kotlinx.android.synthetic.main.activity_premium.*
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File

class PremiumActivity : AppCompatActivity() {

    private val premiumViewModel : PremiumViewModel by viewModel()
    private lateinit var imgUrl : String
    companion object{ private const val CODE_PIX = 101 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_premium)
        supportActionBar?.hide()
        observe()
        openPix()
        upload()
    }

    private fun openPix() {
        btn_payment.setOnClickListener {
            val opt = Options.init()
                .setRequestCode(CODE_PIX)
                .setCount(1)
            Pix.start(this@PremiumActivity, opt)
        }
    }

    private fun observe(){
        observeState()
        observeBank()
    }

    private fun observeState() = premiumViewModel.listenToState().observe(this, Observer { handleUiState(it) })
    private fun observeBank() = premiumViewModel.listenToBank().observe(this, Observer { handleBank(it) })
    private fun fetchBank() = premiumViewModel.fetchBank()

    @SuppressLint("SetTextI18n")
    private fun handleBank(bank: Bank?) {
        bank?.let {
            txt_bank.text = "Bank : ${it.bank_name}"
            txt_account_name.text = "Nama Pemilik Rekening : ${it.account_name}"
            txt_account_number.text = "Nomor Rekening : ${it.account_number}"
        }
    }

    private fun handleUiState(bankState: BankState?) {
        bankState?.let {
            when(it){
                is BankState.Loading -> handleLoading(it.state)
                is BankState.ShowToast -> showToast(it.message)
                is BankState.Success -> handleSuccess()
            }
        }
    }

    private fun handleSuccess() {
        AlertDialog.Builder(this).apply {
            setMessage("berhasil upgrade premium")
            setPositiveButton("oke"){dialog, which ->
                dialog.dismiss()
                Constants.setPremium(this@PremiumActivity, true)
                finish()
            }
        }.show()
    }

    private fun handleLoading(b: Boolean) {
        btn_payment.isEnabled = !b
        btn_upload.isEnabled = !b

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == CODE_PIX && resultCode == Activity.RESULT_OK && data != null){
            val selectedImageUri = data.getStringArrayListExtra(Pix.IMAGE_RESULTS)
            selectedImageUri?.let {
                img_payment.load(File(it[0]))
                imgUrl = it[0]
                btn_upload.isEnabled = true
            }
        }
    }

    private fun upload(){
        btn_upload.setOnClickListener {
            val token = Constants.getToken(this@PremiumActivity)
            premiumViewModel.premium(token, imgUrl)
        }
    }

    override fun onResume() {
        super.onResume()
        fetchBank()
    }
}