package com.one.fruitmanseller.ui.main.order

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.one.fruitmanseller.R
import com.one.fruitmanseller.ui.complete.CompleteActivity
import com.one.fruitmanseller.ui.in_progress.InProgressActivity
import com.one.fruitmanseller.ui.order_in.OrderInActivity
import kotlinx.android.synthetic.main.fragment_order.*

class OrderFragment  : Fragment(R.layout.fragment_order){
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_seller_completed.setOnClickListener { startActivity(Intent(requireActivity(), CompleteActivity::class.java)) }
        btn_seller_in_progress.setOnClickListener { startActivity(Intent(requireActivity(), InProgressActivity::class.java)) }
        btn_seller_order_in.setOnClickListener { startActivity(Intent(requireActivity(), OrderInActivity::class.java)) }
    }
}