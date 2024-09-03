package com.example.foodieapp.presentation.view.activity

import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import com.example.foodieapp.R
import com.example.foodieapp.data.api.AppResource
import com.example.foodieapp.presentation.viewmodel.LoginViewModel
import com.example.foodieapp.utils.ToastUtils

class LoginActivity : AppCompatActivity() {

    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }

    private lateinit var textViewDemo: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        textViewDemo = findViewById(R.id.text_view_name)

        viewModel.getLoading().observe(this) {

        }

        viewModel.getUser().observe(this) {
            when(it) {
                is AppResource.Success -> {
                    textViewDemo.text = it.data.toString()
                    ToastUtils.showToast(this, "Login Success")

                }

                is AppResource.Error -> {
                    textViewDemo.text = it.error
                }
            }
        }
        viewModel.login("demo1@gmail.com", "123456789")
    }
}