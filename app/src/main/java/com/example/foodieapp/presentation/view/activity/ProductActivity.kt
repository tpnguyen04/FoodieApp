package com.example.foodieapp.presentation.view.activity

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieapp.R
import com.example.foodieapp.common.AppCommon
import com.example.foodieapp.common.AppSharedPreferences
import com.example.foodieapp.data.api.AppResource
import com.example.foodieapp.data.model.Cart
import com.example.foodieapp.presentation.adapter.ProductAdapter
import com.example.foodieapp.presentation.viewmodel.CartViewModel
import com.example.foodieapp.presentation.viewmodel.ProductViewModel
import com.example.foodieapp.utils.BadgeUtils
import com.example.foodieapp.utils.ToastUtils

class ProductActivity : AppCompatActivity() {

    private var layoutLoading: LinearLayout? = null
    private var productRecyclerView: RecyclerView? = null
    private var toolbar: Toolbar? = null
    private var cartItemArea: FrameLayout? = null
    private var textBadge: TextView? = null

    private val productAdapter: ProductAdapter by lazy {
        ProductAdapter(this@ProductActivity)
    }

    private val productViewModel by lazy {
        ViewModelProvider(this)[ProductViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.product_activity)) { v, insets ->
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
        val token = AppSharedPreferences.getString(this@ProductActivity, AppCommon.KEY_TOKEN)
        if (token.isEmpty()) return

        productViewModel.getProductList()
        productViewModel.getCart(token)
    }

    private fun observerData() {
        // Loading
        productViewModel.getLoading().observe(this, Observer {
            layoutLoading?.isVisible = it
        })

        // List product
        productViewModel.getProductLiveData().observe(this, Observer {
            when(it) {
                is AppResource.Success -> {
                    productAdapter.setListProduct(it.data?.toList() ?: emptyList())
                }
                is AppResource.Error -> {
                    //show error
                    ToastUtils.showToast(this, it.error)
                }
            }
        })

        productViewModel.getCartLiveData().observe(this) {
            when (it) {
                is AppResource.Success -> {
                    BadgeUtils.updateBadge(textBadge, it.data)
                }

                is AppResource.Error -> {
                    //show error
                    ToastUtils.showToast(this, it.error)
                }
            }
        }
    }

    private fun initViews() {
        productRecyclerView = findViewById(R.id.recycler_view_product)
        toolbar = findViewById(R.id.toolbar_home)
        layoutLoading = findViewById(R.id.layout_loading)
        productRecyclerView?.adapter = productAdapter
        productRecyclerView?.layoutManager = GridLayoutManager(this@ProductActivity, 2)
        // set on click for add button
        productAdapter.setOnAddClickListener {
            val token = AppSharedPreferences.getString(this@ProductActivity, AppCommon.KEY_TOKEN)
            if (token.isEmpty()) return@setOnAddClickListener
            productViewModel.addCart(token, it)
        }
        // set on click for detail button
        productAdapter.setOnDetailClickListener {
            val intent = Intent(this@ProductActivity, ProductDetailActivity::class.java)
            intent.putExtra(AppCommon.PRODUCT_NUMBER, it)
            startActivity(intent)
        }
        // set on click for product item
        productAdapter.setOnProductItemClickListener {
            val intent = Intent(this@ProductActivity, ProductDetailActivity::class.java)
            intent.putExtra(AppCommon.PRODUCT_NUMBER, it)
            startActivity(intent)
        }
    }

    @SuppressLint("UseSupportActionBar")
    private fun mapView() {
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_product, menu)
        val rootView = menu?.findItem(R.id.item_menu_cart)?.actionView
        cartItemArea = rootView?.findViewById(R.id.frame_layout_cart_area)
        textBadge = rootView?.findViewById(R.id.text_cart_badge)

        cartItemArea?.setOnClickListener {
            startActivity(Intent(this@ProductActivity, CartActivity::class.java))
        }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.item_menu_order_history -> startActivity(Intent(this@ProductActivity, HistoryActivity::class.java))
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onRestart() {
        super.onRestart()
        productViewModel.updateCartLiveData(AppSharedPreferences.getString(this@ProductActivity, AppCommon.KEY_TOKEN))
//        productViewModel.updateProductList()
    }
}