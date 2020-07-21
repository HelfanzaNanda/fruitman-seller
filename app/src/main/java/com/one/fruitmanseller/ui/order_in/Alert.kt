package com.one.fruitmanseller.ui.order_in

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun Context.AlertCancel(message: String, token : String, id : String, orderInViewModel: OrderInViewModel){
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton("ya"){dialog, _ ->
            orderInViewModel.cancel(token, id)
            dialog.dismiss()
        }
        setNegativeButton("tidak"){dialog, _ ->dialog.dismiss()  }
    }.show()
}

fun Context.AlertConfirm(message: String, token : String, id : String, orderInViewModel: OrderInViewModel){
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton("ya"){dialog, _ ->
            orderInViewModel.confirm(token, id)
            dialog.dismiss()
        }
        setNegativeButton("tidak"){dialog, _ ->dialog.dismiss()  }
    }.show()
}