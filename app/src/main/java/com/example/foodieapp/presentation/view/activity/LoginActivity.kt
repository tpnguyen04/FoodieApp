package com.example.foodieapp.presentation.view.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableStringBuilder
import android.text.method.LinkMovementMethod
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.foodieapp.R
import com.example.foodieapp.data.api.AppResource
import com.example.foodieapp.presentation.viewmodel.LoginViewModel
import com.example.foodieapp.utils.SpannedUtils
import com.example.foodieapp.utils.ToastUtils
import com.google.android.material.textfield.TextInputEditText

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var buttonSignIn: LinearLayout
    private lateinit var textViewRegister: TextView
    private lateinit var layoutLoading: LinearLayout


    private val viewModel: LoginViewModel by lazy {
        ViewModelProvider(this)[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        observeData()
        events()

    }

    private fun events() {
        buttonSignIn.setOnClickListener {
            val email = editTextEmail.text.toString()
            val password = editTextPassword.text.toString()
            if (email.isBlank() || password.isBlank()) {
                ToastUtils.showToast(this, "Please fill in email & password")
                return@setOnClickListener
            }
            viewModel.login(this@LoginActivity,email, password)
        }

    }

    private fun observeData() {
        viewModel.getLoading().observe(this) {
            layoutLoading.isVisible = it
        }

        viewModel.getUser().observe(this) {
            when(it) {
                is AppResource.Success -> {
                    ToastUtils.showToast(this, "Login Success!")
                    startActivity(Intent(this@LoginActivity, ProductActivity::class.java))
                    finish()
                }
                is AppResource.Error -> {
                    ToastUtils.showToast(this, it.error)
                }
            }
        }
    }

    private fun initViews() {
        editTextEmail = findViewById(R.id.text_edit_email)
        editTextPassword = findViewById(R.id.text_edit_password)
        buttonSignIn = findViewById(R.id.button_sign_in)
        textViewRegister = findViewById(R.id.text_view_register)
        layoutLoading = findViewById(R.id.layout_loading)

        displayTextViewSignUp()
    }

    private fun displayTextViewSignUp() {
        SpannableStringBuilder().apply {
            append("Don't have an account? ")
            append(SpannedUtils.setClickColorLink(
                text = "Sign up here",
                context = this@LoginActivity,
                onListenClick = onClickSignUp
            ))
            textViewRegister.text = this
            textViewRegister.highlightColor = Color.TRANSPARENT
            textViewRegister.movementMethod = LinkMovementMethod.getInstance()
        }
    }

    private var onClickSignUp =  {
        startActivity(Intent(this@LoginActivity, SignUpActivity::class.java))
    }
}