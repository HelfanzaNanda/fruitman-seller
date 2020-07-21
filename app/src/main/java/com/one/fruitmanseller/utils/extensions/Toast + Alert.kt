package com.one.fruitmanseller.utils.extensions

import android.content.Context
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.one.fruitmanseller.ui.order_in.OrderInViewModel

fun Context.showToast(message : String){
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.AlertRegister(message: String){
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton("ya"){dialog, _ ->
            dialog.dismiss()
        }
    }.show()
}