package com.example.foodieapp.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieapp.R
import com.example.foodieapp.data.model.Cart
import com.example.foodieapp.data.model.Product
import com.example.foodieapp.utils.StringUtils
import com.example.foodieapp.utils.TimeUtils

class HistoryAdapter(
    private var context: Context? = null,
    private var onHistoryItemClick: ((Int) -> Unit)? = null
): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {
    private var listCart: List<Cart?> = emptyList()

    @SuppressLint("NotifyDataSetChanged")
    fun setListCart(listCart: List<Cart?>) {
        if (listCart.isEmpty()) return
        this.listCart = listCart
        notifyDataSetChanged()
    }

    inner class HistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private var textViewNumber: TextView? = null
        private var textViewTime: TextView? = null
        private var textViewOrderPrice: TextView? = null

        init {
            textViewNumber = view.findViewById(R.id.text_view_number)
            textViewTime = view.findViewById(R.id.text_view_time)
            textViewOrderPrice = view.findViewById(R.id.text_view_order_price)
        }

        fun bind(context: Context?, cart: Cart?) {
            cart?.let {
                val number = adapterPosition + 1
                textViewNumber?.text = number.toString()
                textViewTime?.text = TimeUtils.formatDateTime(it.dateCreated.toString())
                textViewOrderPrice?.text = String.format(
                    "Giá: %s VND",
                    StringUtils.formatCurrency(it.price?.toInt() ?: 0)
                )
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val layoutInflater = LayoutInflater.from(context)
        val view = layoutInflater.inflate(R.layout.layout_history_item, parent, false)
        return HistoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        context?.let { holder.bind(it, listCart.getOrNull(position)) }
        // set on click listener cho item view
        holder.itemView.setOnClickListener {
            onHistoryItemClick?.invoke(position)
        }
    }

    override fun getItemCount(): Int {
        return listCart.size
    }

    fun setOnHistoryItemClickListener(onHistoryItemClick: (Int) -> Unit) {
        this.onHistoryItemClick = onHistoryItemClick
    }
}