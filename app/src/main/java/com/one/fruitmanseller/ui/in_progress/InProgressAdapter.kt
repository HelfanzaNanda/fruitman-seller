package com.one.fruitmanseller.ui.in_progress

import android.annotation.SuppressLint
import com.one.fruitmanseller.utils.extensions.visible
import kotlinx.android.synthetic.main.list_item_in_progress.view.*
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.one.fruitmanseller.R
import com.one.fruitmanseller.models.Order
import com.one.fruitmanseller.utils.Constants

class InProgressAdapter (private var orders : MutableList<Order>,
                      private var context: Context,
                      private var inProgressViewModel: InProgressViewModel)
    : RecyclerView.Adapter<InProgressAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_in_progress, parent, false))
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(orders[position], context, inProgressViewModel)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(order: Order, context: Context, inProgressViewModel: InProgressViewModel){
            with(itemView){
                if (order.arrive == false){
                    tv_desc.text = "${order.buyer.name} sedang menuju tempat anda, untuk melakukan transaksi ${order.product.name} , silahkan tunggu"
                }else if (order.arrive == true){
                    tv_desc.text = "status transaksi dengan ${order.buyer.name} dengan ${order.product.name} setelah bertemu di tempat"
                    btn_completed.visible()
                    btn_decline.visible()
                    btn_completed.setOnClickListener {
                        val token  = Constants.getToken(context)
                        val mesage  = "apakah transaksi selesai?"
                        context.AlertComplete(mesage, token, order.id.toString(), inProgressViewModel)
                    }
                    btn_decline.setOnClickListener {
                        val message ="apakah penebas tidak jadi membeli produk?"
                        val token = Constants.getToken(context)
                        context.AlertCancel(message, token, order.id.toString(), inProgressViewModel)
                    }
                }
            }
        }
    }

    fun changelist(c : List<Order>){
        orders.clear()
        orders.addAll(c)
        notifyDataSetChanged()
    }
}