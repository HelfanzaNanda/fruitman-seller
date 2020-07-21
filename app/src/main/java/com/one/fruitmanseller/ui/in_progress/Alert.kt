package com.one.fruitmanseller.ui.in_progress

import android.content.Context
import androidx.appcompat.app.AlertDialog

fun Context.AlertComplete(message: String, token : String, id : String, inProgressViewModel: InProgressViewModel){
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton("ya"){dialog, _ ->
            inProgressViewModel.complete(token, id)
            dialog.dismiss()
        }
        setNegativeButton("tidak"){dialog, _ ->dialog.dismiss()  }
    }.show()
}

fun Context.AlertCancel(message: String, token : String, id : String, inProgressViewModel: InProgressViewModel){
    AlertDialog.Builder(this).apply {
        setMessage(message)
        setPositiveButton("ya"){dialog, _ ->
            inProgressViewModel.cancel(token, id)
            dialog.dismiss()
        }
        setNegativeButton("tidak"){dialog, _ ->dialog.dismiss()  }
    }.show()
}
