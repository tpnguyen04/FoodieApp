package com.example.foodieapp.presentation.view.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.LinearLayout
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.foodieapp.R
import com.example.foodieapp.data.api.AppResource
import com.example.foodieapp.presentation.adapter.ProductAdapter
import com.example.foodieapp.presentation.viewmodel.ProductViewModel
import com.example.foodieapp.utils.ToastUtils

class ProductActivity : AppCompatActivity() {

    private var layoutLoading: LinearLayout? = null
    private var productRecyclerView: RecyclerView? = null
    private var toolbar: Toolbar? = null

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
        productViewModel.getProductList()
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
    }

    private fun initViews() {
        productRecyclerView = findViewById(R.id.recycler_view_product)
        toolbar = findViewById(R.id.toolbar_home)
        layoutLoading = findViewById(R.id.layout_loading)
        productRecyclerView?.adapter = productAdapter
    }

    @SuppressLint("UseSupportActionBar")
    private fun mapView() {
        setSupportActionBar(toolbar)
    }
}