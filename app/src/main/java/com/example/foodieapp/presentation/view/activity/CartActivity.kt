package com.example.foodieapp.presentation.view.activity

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.MenuItem
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
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
import com.example.foodieapp.presentation.adapter.ProductAdapter
import com.example.foodieapp.presentation.viewmodel.CartViewModel
import com.example.foodieapp.presentation.viewmodel.ProductViewModel
import com.example.foodieapp.utils.StringUtils
import com.example.foodieapp.utils.ToastUtils

class CartActivity : AppCompatActivity() {

    private var layoutLoading: LinearLayout? = null
    private var layoutCartEmpty: ConstraintLayout? = null
    private var layoutCartDetail: ConstraintLayout? = null
    private var buttonViewProductList: LinearLayout? = null
    private var textViewTotalNumber: TextView? = null
    private var buttonConfirmOrder: LinearLayout? = null


    private var cartRecyclerView: RecyclerView? = null
    private var toolbar: Toolbar? = null

    private val cartAdapter: CartAdapter by lazy {
        CartAdapter(this@CartActivity)
    }

    private val cartViewModel by lazy {
        ViewModelProvider(this)[CartViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_cart)
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
        val token = AppSharedPreferences.getString(this@CartActivity, AppCommon.KEY_TOKEN)
        if (token.isEmpty()) return
        cartViewModel.getCart(token)
        buttonViewProductList?.setOnClickListener {
            finish()
        }
    }

    private fun observerData() {
        // Loading
        cartViewModel.getLoading().observe(this) {
            layoutLoading?.isVisible = it
        }

        // cart
        cartViewModel.getCartLiveData().observe(this) {
            when (it) {
                is AppResource.Success -> {
                    cartAdapter.setListProduct(it.data?.listProduct ?: emptyList())
                    val numberOfCart = it.data?.listProduct?.size ?: 0
                    if (numberOfCart < 1) {
                        layoutCartEmpty?.isVisible = true
                        layoutCartDetail?.isVisible = false
                    } else {
                        layoutCartEmpty?.isVisible = false
                        layoutCartDetail?.isVisible = true
                        textViewTotalNumber?.text = String.format(
                            "%s VND",
                            StringUtils.formatCurrency(it.data?.price?.toInt() ?: 0)
                        )
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
        cartRecyclerView = findViewById(R.id.recycler_View_cart)
        cartRecyclerView?.adapter = cartAdapter
        layoutCartEmpty = findViewById(R.id.layout_cart_empty)
        layoutCartDetail = findViewById(R.id.layout_cart_detail)
        buttonViewProductList = findViewById(R.id.button_view_product_list)
        textViewTotalNumber = findViewById(R.id.text_view_total_number)
        buttonConfirmOrder = findViewById(R.id.button_confirm_order)
        toolbar = findViewById(R.id.toolbar_home)
        // set on click for add button
        cartAdapter.setOnAddClickListener {
            val token = AppSharedPreferences.getString(this@CartActivity, AppCommon.KEY_TOKEN)
            if (token.isEmpty()) return@setOnAddClickListener
            cartViewModel.addCart(token, it)
            Toast.makeText(this@CartActivity, "Add cart success", Toast.LENGTH_SHORT).show()
        }
        // set on click for subtract button
        cartAdapter.setOnSubtractClickListener {
            Toast.makeText(this@CartActivity, "subtract $it", Toast.LENGTH_SHORT).show()
        }
        // set on click for delete button
        cartAdapter.setOnDeleteClickListener {
            Toast.makeText(this@CartActivity, "delete $it", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            // back button
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }
}