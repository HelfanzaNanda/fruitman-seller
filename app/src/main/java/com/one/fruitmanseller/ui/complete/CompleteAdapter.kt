package com.one.fruitmanseller.ui.complete

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.one.fruitmanseller.R
import com.one.fruitmanseller.models.Order
import kotlinx.android.synthetic.main.list_item_complete.view.*

class CompleteAdapter (private var orders : MutableList<Order>, private var context: Context)
    : RecyclerView.Adapter<CompleteAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_in_progress, parent, false))
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(orders[position], context)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(order: Order, context: Context){
            with(itemView){
                tv_desc.text = "transaksi dengan ${order.buyer.name} dengan ${order.product.name} sudah selesai"
            }
        }
    }

    fun changelist(c : List<Order>){
        orders.clear()
        orders.addAll(c)
        notifyDataSetChanged()
    }
}