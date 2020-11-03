package com.one.fruitmanseller.ui.product

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import coil.api.load
import com.one.fruitmanseller.R
import com.smarteist.autoimageslider.SliderViewAdapter
import kotlinx.android.synthetic.main.item_image_slider.view.*
import java.io.File

class ImgSliderAdapter(private val images : MutableList<String>) :
    SliderViewAdapter<ImgSliderAdapter.SliderAdapterVH>() {

    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup): SliderAdapterVH {
        return SliderAdapterVH(LayoutInflater.from(parent.context).inflate(R.layout.item_image_slider, null))
    }

    override fun getCount(): Int = images.size

    override fun onBindViewHolder(viewHolder: SliderAdapterVH?, position: Int) = viewHolder!!.bind(images[position])

    class SliderAdapterVH(itemView: View) : ViewHolder(itemView){
        fun bind(image : String){
            with(itemView){ iv_product.load(File(image)) }
        }
    }

    fun changelist(c : List<String>){
        images.clear()
        images.addAll(c)
        notifyDataSetChanged()
    }
}