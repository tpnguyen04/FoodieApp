package com.example.foodieapp.presentation.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieapp.R
import com.example.foodieapp.data.model.Cart
import com.example.foodieapp.data.model.Product
import com.example.foodieapp.utils.StringUtils
import kotlin.time.times

class HistoryDetailAdapter (): RecyclerView.Adapter<HistoryDetailAdapter.HistoryDetailViewHolder>(){
    private var listProduct: List<Product?> = emptyList()
    private var currentCart: Cart? = null

    @SuppressLint("NotifyDataSetChanged")
    fun setListProduct(listProduct: List<Product?>) {
        if (listProduct.isEmpty()) return
        this.listProduct = listProduct
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setCurrentCart(currentCart: Cart?) {
        if (currentCart == null) return
        this.currentCart = currentCart
        notifyDataSetChanged()
    }

    inner class HistoryDetailViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var textViewItemProduct: TextView? = null
        private var textViewItemQuantity: TextView? = null
        private var textViewItemUnitPrice: TextView? = null
        private var textViewItemTotalPrice: TextView? = null

        init {
            textViewItemProduct = view.findViewById(R.id.text_view_history_detail_item_product)
            textViewItemQuantity = view.findViewById(R.id.text_view_history_detail_item_quantity)
            textViewItemUnitPrice = view.findViewById(R.id.text_view_history_detail_unit_price)
            textViewItemTotalPrice = view.findViewById(R.id.text_view_history_detail_total_price)
        }

        fun bind(product: Product?) {
            product.let {
                textViewItemProduct?.text = it?.name
                textViewItemQuantity?.text = it?.quantity.toString()
                textViewItemUnitPrice?.text = String.format(
                    "%s",
                    StringUtils.formatCurrency(it?.price ?: 0)
                )
                val totalPrice = it?.quantity?.times(it.price ?: 0) ?: 0
                textViewItemTotalPrice?.text = String.format(
                    "%s",
                    StringUtils.formatCurrency(totalPrice)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryDetailViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.layout_history_detail_item, parent, false)
        return HistoryDetailViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryDetailViewHolder, position: Int) {
        holder.bind(listProduct.getOrNull(position))
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }


}