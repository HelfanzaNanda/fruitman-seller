package com.one.fruitmanseller.ui.order_in

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.one.fruitmanseller.R
import com.one.fruitmanseller.models.Order
import com.one.fruitmanseller.utils.Constants
import kotlinx.android.synthetic.main.list_item_order_in.view.*

class OrderInAdapter (private var orders : MutableList<Order>,
                      private var context: Context,
                      private var orderInViewModel: OrderInViewModel)
    : RecyclerView.Adapter<OrderInAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_order_in, parent, false))
    }

    override fun getItemCount(): Int = orders.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(orders[position], context, orderInViewModel)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        @SuppressLint("SetTextI18n")
        fun bind(order: Order, context: Context, orderInViewModel: OrderInViewModel){
            with(itemView){
                if (order.status.equals("1") && !order.arrive!!) {
                    waiting_desc.text = "${order.buyer.name} menawar dengan harga ${order.offer_price} pada ${order.product.name}"
                    setOnClickListener { Toast.makeText(context, "Ya", Toast.LENGTH_LONG).show() }
                    btn_confirmed.visibility = View.VISIBLE
                    btn_confirmed.setOnClickListener {
                        val message = "apakah anda yakin akan mengkonfirmasi pesanan ini?"
                        val token  = Constants.getToken(context)
                        context.AlertConfirm(message, token, order.id.toString(), orderInViewModel)
                    }
                    btn_decline.setOnClickListener {
                        val message = "apakah anda yakin akan mengkonfirmasi pesanan ini?"
                        val token  = Constants.getToken(context)
                        context.AlertCancel(message, token, order.id.toString(), orderInViewModel)
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