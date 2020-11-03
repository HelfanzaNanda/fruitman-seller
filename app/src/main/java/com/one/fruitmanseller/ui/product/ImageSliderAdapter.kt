package com.one.fruitmanseller.ui.product

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.api.load
import com.one.fruitmanseller.R
import com.one.fruitmanseller.models.ProductImage
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.item_image_slider.view.*
import java.io.File

class ImageSliderAdapter(private val images : MutableList<ProductImage>) :
    SliderViewAdapter<ImageSliderAdapter.SliderAdapterVH>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        return SliderAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.item_image_slider, null))
    }

    override fun getCount(): Int = images.size

    override fun onBindViewHolder(viewHolder: SliderAdapterVH?, position: Int) = viewHolder!!.bind(images[position])

    class SliderAdapterVH(itemView: View) : ViewHolder(itemView){
        fun bind(image : ProductImage){
            with(itemView){ iv_product.load(image.image!!) }
        }
    }

    fun changelist(c : List<ProductImage>){
        images.clear()
        images.addAll(c)
        notifyDataSetChanged()
    }
}