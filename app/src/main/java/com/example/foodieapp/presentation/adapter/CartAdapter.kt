package com.example.foodieapp.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodieapp.R
import com.example.foodieapp.common.AppCommon
import com.example.foodieapp.data.model.Product
import com.example.foodieapp.utils.StringUtils

class CartAdapter(
    private var context: Context? = null,
):RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var listProduct: List<Product?> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setListProduct(listProduct: List<Product?>) {
        if (listProduct.isEmpty()) return
        this.listProduct = listProduct
        notifyDataSetChanged()
    }

    inner class CartViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var imageView: ImageView? = null
        private var textViewName: TextView? = null
        private var textViewPrice: TextView? = null
        private var buttonAdd: LinearLayout? = null
        private var buttonSubtract: LinearLayout? = null
        private var buttonDelete: ImageView? = null
        private var textViewNumberProduct: TextView? = null

        init {
            imageView = view.findViewById(R.id.cart_image_view_product)
            textViewName = view.findViewById(R.id.cart_text_view_product_name)
            textViewPrice = view.findViewById(R.id.cart_text_view_product_price)
            buttonAdd = view.findViewById(R.id.button_add_product)
            buttonSubtract = view.findViewById(R.id.button_subtract_product)
            buttonDelete = view.findViewById(R.id.image_view_icon_delete)
            textViewNumberProduct = view.findViewById(R.id.cart_product_text_view_number)
        }

        fun bind(context: Context, product: Product?) {
            product?.let {
                imageView?.let {
                    Glide.with(context)
                        .load(AppCommon.BASE_URL + product.image)
                        .placeholder(R.drawable.ic_logo)
                        .into(it)
                }
                textViewName?.text = it.name
                textViewPrice?.text = String.format(
                    "Giá: %s VND",
                    StringUtils.formatCurrency(it.price ?: 0)
                )
                textViewNumberProduct?.text = it.quantity.toString()
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.layout_cart_item, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        context?.let { holder.bind(it, listProduct.getOrNull(position)) }
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }
}