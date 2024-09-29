package com.example.foodieapp.presentation.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieapp.R
import com.example.foodieapp.common.AppCommon
import com.example.foodieapp.common.AppSharedPreferences
import com.example.foodieapp.data.api.AppResource
import com.example.foodieapp.presentation.adapter.HistoryAdapter
import com.example.foodieapp.presentation.adapter.HistoryDetailAdapter
import com.example.foodieapp.presentation.viewmodel.HistoryViewModel
import com.example.foodieapp.utils.StringUtils
import com.example.foodieapp.utils.TimeUtils
import com.example.foodieapp.utils.ToastUtils

class HistoryDetailActivity : AppCompatActivity() {

    private var layoutLoading: LinearLayout? = null
    private var toolbar: Toolbar? = null
    private var layoutHistoryDetail: ConstraintLayout? = null
    private var textViewBillTime: TextView? = null
    private var textViewBillTotalPrice: TextView? = null
    private var layoutBillColumnArea: LinearLayout? = null
    private var recyclerViewHistoryDetail: RecyclerView? = null

    private val historyDetailAdapter: HistoryDetailAdapter by lazy {
        HistoryDetailAdapter()
    }

    private val historyViewModel by lazy {
        ViewModelProvider(this)[HistoryViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history_detail)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        mapView()
        observerData()
        event()
    }

    private fun event() {
        val token = AppSharedPreferences.getString(this@HistoryDetailActivity, AppCommon.KEY_TOKEN)
        if (token.isEmpty()) return
        historyViewModel.getHistory(token)
    }

    private fun observerData() {
        // Loading
        historyViewModel.getLoading().observe(this) {
            layoutLoading?.isVisible = it
        }

        // history
        historyViewModel.getHistoryLiveData().observe(this) {
            when (it) {
                is AppResource.Success -> {
                    // set list product for cart adapter
                    val cartNumber = intent.getIntExtra(AppCommon.CART_NUMBER, 0)
                    it.data?.get(cartNumber)?.let { cart ->
                        historyDetailAdapter.setListProduct(cart.listProduct)
                        historyDetailAdapter.setCurrentCart(cart)
                        textViewBillTime?.text = TimeUtils.formatDateTime(cart.dateCreated.toString())
                        textViewBillTotalPrice?.text = String.format(
                            "Tổng tiền: %s VND",
                            StringUtils.formatCurrency(cart.price?.toInt() ?: 0)
                        )
                    }
                    layoutHistoryDetail?.isVisible = true
                    layoutBillColumnArea?.isVisible = true
                    recyclerViewHistoryDetail?.isVisible = true
                }

                is AppResource.Error -> {
                    //show error
                    ToastUtils.showToast(this, it.error)
                }
            }
        }
    }

    private fun mapView() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
        // cho phép hiển thị nút back
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // đổi icon tùy chọn cho nút back
        supportActionBar?.setHomeAsUpIndicator(ContextCompat.getDrawable(this, R.drawable.ic_back))
    }

    private fun initViews() {
        layoutLoading = findViewById(R.id.layout_loading)
        toolbar = findViewById(R.id.toolbar_home)
        layoutHistoryDetail = findViewById(R.id.layout_history_detail)
        textViewBillTime = findViewById(R.id.text_view_bill_time)
        textViewBillTotalPrice = findViewById(R.id.text_view_total_bill_price)
        layoutBillColumnArea = findViewById(R.id.bill_column_area)
        recyclerViewHistoryDetail = findViewById(R.id.recycler_view_history_detail)
        recyclerViewHistoryDetail?.adapter = historyDetailAdapter
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            // back button
            android.R.id.home -> { finish() }
        }
        return super.onOptionsItemSelected(item)
    }
}