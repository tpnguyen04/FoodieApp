package com.example.foodieapp.presentation.view.activity

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
import com.example.foodieapp.presentation.viewmodel.SignUpViewModel
import com.example.foodieapp.utils.SpannedUtils
import com.example.foodieapp.utils.ToastUtils
import com.google.android.material.textfield.TextInputEditText

class SignUpActivity : AppCompatActivity() {

    private lateinit var editTextName: TextInputEditText
    private lateinit var editTextAddress: TextInputEditText
    private lateinit var editTextEmail: TextInputEditText
    private lateinit var editTextPhone: TextInputEditText
    private lateinit var editTextPassword: TextInputEditText
    private lateinit var editTextPasswordReconfirm: TextInputEditText
    private lateinit var buttonSignUp: LinearLayout
    private lateinit var textViewPopUpToLogIn: TextView
    private lateinit var layoutLoading: LinearLayout

    private val viewModel: SignUpViewModel by lazy {
        ViewModelProvider(this)[SignUpViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signup)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        observerData()
        event()
    }

    private fun event() {
        buttonSignUp.setOnClickListener {
            val name = editTextName.text.toString()
            val address = editTextAddress.text.toString()
            val email = editTextEmail.text.toString()
            val phone = editTextPhone.text.toString()
            val password = editTextPassword.text.toString()
            val passwordReconfirm = editTextPasswordReconfirm.text.toString()
            if (name.isBlank() || address.isBlank() || email.isBlank() || phone.isBlank() || password.isBlank() || passwordReconfirm.isBlank()) {
                ToastUtils.showToast(this@SignUpActivity, "Please fill all the information")
                return@setOnClickListener
            }
            if (passwordReconfirm != password) {
                ToastUtils.showToast(this@SignUpActivity, "Reconfirm password does not match")
                return@setOnClickListener
            }
            viewModel.signUp(
                email = email,
                password = password,
                name = name,
                phone = phone,
                address = address
            )
        }
    }

    private fun observerData() {
        viewModel.getLoading().observe(this) {
            layoutLoading.isVisible = it
        }

        viewModel.getUser().observe(this) {
            when(it) {
                is AppResource.Success -> {
                    ToastUtils.showToast(this, "Sign up Success!")
                    finish()
                }
                is AppResource.Error -> {
                    ToastUtils.showToast(this, it.error)
                }
            }
        }
    }

    private fun initViews() {
        editTextName = findViewById(R.id.text_edit_name)
        editTextAddress = findViewById(R.id.text_edit_address)
        editTextEmail = findViewById(R.id.text_edit_email)
        editTextPhone = findViewById(R.id.text_edit_phone)
        editTextPassword = findViewById(R.id.text_edit_password)
        editTextPasswordReconfirm = findViewById(R.id.text_edit_password_reconfirm)
        buttonSignUp = findViewById(R.id.button_sign_up)
        textViewPopUpToLogIn = findViewById(R.id.text_view_pop_to_login_activity)
        layoutLoading = findViewById(R.id.layout_loading)

        displayTextViewLogin()
    }

    private fun displayTextViewLogin() {
        SpannableStringBuilder().apply {
            append("Already have an account? ")
            append(
                SpannedUtils.setClickColorLink(
                text = "Log in here",
                context = this@SignUpActivity,
                onListenClick = { finish() }
            ))
            textViewPopUpToLogIn.text = this
            textViewPopUpToLogIn.highlightColor = Color.TRANSPARENT
            textViewPopUpToLogIn.movementMethod = LinkMovementMethod.getInstance()
        }
    }
}