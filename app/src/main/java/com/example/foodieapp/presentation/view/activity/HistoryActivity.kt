package com.example.foodieapp.presentation.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
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
import com.example.foodieapp.presentation.adapter.CartAdapter
import com.example.foodieapp.presentation.adapter.HistoryAdapter
import com.example.foodieapp.presentation.viewmodel.CartViewModel
import com.example.foodieapp.presentation.viewmodel.HistoryViewModel
import com.example.foodieapp.utils.StringUtils
import com.example.foodieapp.utils.ToastUtils

class HistoryActivity : AppCompatActivity() {

    private var layoutLoading: LinearLayout? = null
    private var layoutHistoryEmpty: ConstraintLayout? = null
    private var layoutHistoryDetail: ConstraintLayout? = null
    private var buttonViewProductList: LinearLayout? = null

    private var historyRecyclerView: RecyclerView? = null
    private var toolbar: Toolbar? = null

    private val historyAdapter: HistoryAdapter by lazy {
        HistoryAdapter()
    }

    private val historyViewModel by lazy {
        ViewModelProvider(this)[HistoryViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_history)
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
        val token = AppSharedPreferences.getString(this@HistoryActivity, AppCommon.KEY_TOKEN)
        if (token.isEmpty()) return
        historyViewModel.getHistory(token)
        buttonViewProductList?.setOnClickListener {
            finish()
        }
        historyAdapter.setOnHistoryItemClickListener {
            val intent = Intent(this@HistoryActivity, HistoryDetailActivity::class.java)
            intent.putExtra(AppCommon.CART_NUMBER, it)
            startActivity(intent)
        }
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
                    historyAdapter.setListCart(it.data ?: emptyList())

                    val numberOfHistory = it.data?.size ?: 0
                    if (numberOfHistory < 1) {
                        layoutHistoryEmpty?.isVisible = true
                        layoutHistoryDetail?.isVisible = false
                    } else {
                        layoutHistoryEmpty?.isVisible = false
                        layoutHistoryDetail?.isVisible = true
                    }
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
        layoutHistoryEmpty = findViewById(R.id.layout_history_empty)
        layoutHistoryDetail = findViewById(R.id.layout_history_detail)
        buttonViewProductList = findViewById(R.id.button_view_product_list)
        historyRecyclerView = findViewById(R.id.recycler_view_history)
        historyRecyclerView?.adapter = historyAdapter
        toolbar = findViewById(R.id.toolbar_home)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            // back button
            android.R.id.home -> { finish() }
        }
        return super.onOptionsItemSelected(item)
    }
}