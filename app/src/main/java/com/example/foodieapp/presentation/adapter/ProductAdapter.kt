package com.example.foodieapp.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.foodieapp.R
import com.example.foodieapp.common.AppCommon
import com.example.foodieapp.data.model.Product
import com.example.foodieapp.utils.StringUtils

class ProductAdapter(
    private var context: Context? = null,
    private var onButtonAddClick: ((String) -> Unit)? = null,
    private var onButtonDetailClick: ((String) -> Unit)? = null,
    private var onProductItemClick: ((Int) -> Unit)? = null
): RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var listProduct: List<Product?> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setListProduct(listProduct: List<Product?>) {
        if (listProduct.isEmpty()) return
        this.listProduct = listProduct
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var imageView: ImageView? = null
        private var textViewName: TextView? = null
        private var textViewAddress: TextView? = null
        private var textViewPrice: TextView? = null
        private var buttonAddProduct: LinearLayout? = null
        private var buttonDetailProduct: LinearLayout? = null

        init {
            imageView = view.findViewById(R.id.image_view_product)
            textViewName = view.findViewById(R.id.text_view_product_name)
            textViewAddress = view.findViewById(R.id.text_view_product_address)
            textViewPrice = view.findViewById(R.id.text_view_product_price)
            buttonAddProduct = view.findViewById(R.id.button_add_product)
            buttonDetailProduct = view.findViewById(R.id.button_detail_product)
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
                textViewAddress?.text = it.address
                textViewPrice?.text = String.format(
                    "Giá: %s VND",
                    StringUtils.formatCurrency(product.price ?: 0)
                )
            }
            buttonAddProduct?.setOnClickListener {
                onButtonAddClick?.invoke(product?.id.toString())
                return@setOnClickListener
            }
            buttonDetailProduct?.setOnClickListener {
                onButtonDetailClick?.invoke(product?.id.toString())
                return@setOnClickListener
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.layout_product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        context?.let { holder.bind(it, listProduct.getOrNull(position)) }
        holder.itemView.setOnClickListener {
            onProductItemClick?.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }

    fun setOnAddClickListener(onButtonAddClick: (String) -> Unit) {
        this.onButtonAddClick = onButtonAddClick
    }

    fun setOnDetailClickListener(onButtonDetailClick: (String) -> Unit) {
        this.onButtonDetailClick = onButtonDetailClick
    }

    fun setOnProductItemClickListener(onProductItemClick: (Int) -> Unit) {
        this.onProductItemClick = onProductItemClick
    }
}