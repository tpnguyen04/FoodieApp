package com.example.foodieapp.presentation.view.activity

import android.os.Bundle
import android.view.MenuItem
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
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
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.foodieapp.R
import com.example.foodieapp.common.AppCommon
import com.example.foodieapp.common.AppSharedPreferences
import com.example.foodieapp.data.api.AppResource
import com.example.foodieapp.presentation.adapter.HistoryDetailAdapter
import com.example.foodieapp.presentation.adapter.ImagePagerAdapter
import com.example.foodieapp.presentation.viewmodel.HistoryViewModel
import com.example.foodieapp.presentation.viewmodel.ProductViewModel
import com.example.foodieapp.utils.StringUtils
import com.example.foodieapp.utils.ToastUtils
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ProductDetailActivity : AppCompatActivity() {

    private var layoutLoading: LinearLayout? = null
    private var toolbar: Toolbar? = null
    private var layoutProductDetail: LinearLayout? = null
    private var imageViewProductDetailImage: ImageView? = null
    private var textViewProductDetailName: TextView? = null
    private var textViewProductDetailPrice: TextView? = null
    private var textViewProductDetailAddress: TextView? = null
    private var buttonAddToCart: LinearLayout? = null
    private lateinit var viewPager: ViewPager2
    private lateinit var tabLayout: TabLayout

    private val imagePagerAdapter: ImagePagerAdapter by lazy {
        ImagePagerAdapter()
    }

    private val productViewModel by lazy {
        ViewModelProvider(this)[ProductViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_product_detail)
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
        productViewModel.getProductList()
        // set adapter cho view pager
        viewPager.adapter = imagePagerAdapter
        // Kết nối TabLayout với ViewPager2
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            // Bạn có thể tùy chỉnh tab tại đây (ví dụ: thêm icon)
            // tab.setIcon(R.drawable.ic_dot) // Đặt icon cho các dot
        }.attach()
        buttonAddToCart?.setOnClickListener {
            // get token
            val token = AppSharedPreferences.getString(this@ProductDetailActivity, AppCommon.KEY_TOKEN)
            if (token.isEmpty()) return@setOnClickListener

            // get product id
            val productId = AppSharedPreferences.getString(this@ProductDetailActivity, AppCommon.PRODUCT_ID)

            productViewModel.addCart(token, productId)
            finish()
        }
    }

    private fun observerData() {
        // Loading
        productViewModel.getLoading().observe(this) {
            layoutLoading?.isVisible = it
        }

        // product
        productViewModel.getProductLiveData().observe(this) {
            when(it) {
                is AppResource.Success -> {
                    val productNumber = intent.getIntExtra(AppCommon.PRODUCT_NUMBER, 0)
                    it.data?.get(productNumber).let { product ->
                        product?.gallery?.let { listImg -> imagePagerAdapter.setListImageUrls(listImg) }
                        imageViewProductDetailImage?.let {
                            Glide.with(this@ProductDetailActivity)
                                .load(AppCommon.BASE_URL + product?.image)
                                .placeholder(R.drawable.ic_logo)
                                .into(it)
                        }
                        textViewProductDetailName?.text = product?.name.toString()
                        textViewProductDetailAddress?.text = "Địa chỉ: " + product?.address.toString()
                        textViewProductDetailPrice?.text = String.format(
                            "Giá: %s VND",
                            StringUtils.formatCurrency(product?.price ?: 0)
                        )
                        layoutProductDetail?.isVisible = true
                        // get product id
                        AppSharedPreferences.saveData(this@ProductDetailActivity,AppCommon.PRODUCT_ID, product?.id.toString())
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
        toolbar = findViewById(R.id.toolbar_home)
        layoutProductDetail = findViewById(R.id.layout_product_detail)
        imageViewProductDetailImage = findViewById(R.id.image_view_product_img)
        textViewProductDetailName = findViewById(R.id.text_view_product_detail_name)
        textViewProductDetailPrice = findViewById(R.id.text_view_product_detail_price)
        textViewProductDetailAddress = findViewById(R.id.text_view_product_detail_address)
        buttonAddToCart = findViewById(R.id.button_add_to_cart)
        viewPager = findViewById(R.id.viewPager)
        tabLayout = findViewById(R.id.tabLayout)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            // back button
            android.R.id.home -> { finish() }
        }
        return super.onOptionsItemSelected(item)
    }
}