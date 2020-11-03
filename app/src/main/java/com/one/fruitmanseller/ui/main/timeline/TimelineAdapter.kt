package com.one.fruitmanseller.ui.main.timeline

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import com.one.fruitmanseller.R
import com.one.fruitmanseller.models.Product
import com.one.fruitmanseller.ui.product.ProductActivity
import com.one.fruitmanseller.utils.Constants
import kotlinx.android.synthetic.main.list_item_timeline.view.*

class TimelineAdapter (private var products : MutableList<Product>, private var context: Context)
    : RecyclerView.Adapter<TimelineAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.list_item_timeline, parent, false))
    }

    override fun getItemCount(): Int = products.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(products[position], context)

    class ViewHolder(itemView : View) : RecyclerView.ViewHolder(itemView){
        fun bind(product: Product, context: Context){
            with(itemView){
                img_product.load(product.images[0].image)
                txt_name.text = product.name
                txt_address.text = product.address
                txt_price.text = Constants.setToIDR(product.price!!)
                setOnClickListener {
                    context.startActivity(Intent(context, ProductActivity::class.java).apply {
                        putExtra("PRODUCT", product)
                        putExtra("IS_INSERT", false)
                    })
                }
            }
        }
    }

    fun changelist(c: List<Product>){
        products.clear()
        products.addAll(c)
        notifyDataSetChanged()
    }
}