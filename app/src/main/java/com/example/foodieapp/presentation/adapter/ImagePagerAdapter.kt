package com.example.foodieapp.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodieapp.R
import com.example.foodieapp.common.AppCommon
import com.example.foodieapp.data.model.Product

class ImagePagerAdapter() : RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

    private var listImageUrls: List<String?> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setListImageUrls(listImageUrls: List<String?>) {
        if (listImageUrls.isEmpty()) return
        this.listImageUrls = listImageUrls
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.layout_viewpager_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        // Load hình ảnh bằng Glide
        Glide.with(holder.itemView.context)
            .load(AppCommon.BASE_URL + listImageUrls[position])
            .into(holder.imageView)
    }

    override fun getItemCount(): Int = listImageUrls.size

    class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view_product_gallery)
    }
}