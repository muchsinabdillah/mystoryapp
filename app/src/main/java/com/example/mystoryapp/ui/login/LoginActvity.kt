package com.example.mystoryapp.ui.login

import UserPreference
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.view.ViewGroup
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.R
import com.example.mystoryapp.data.model.LoginEntity
import com.example.mystoryapp.databinding.ActivityLoginActvityBinding
import com.example.mystoryapp.ui.mainactivity.MainActivity
import com.example.mystoryapp.viewmodel.MainViewModel
import com.example.mystoryapp.viewmodel.ViewModelFactoryRep
import com.google.android.material.snackbar.Snackbar

class LoginActvity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityLoginActvityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginActvityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        playAnimation()
        setupViewModel()
        setupAction()
        setMyButtonEnable()
        val factory = ViewModelFactoryRep.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        viewModel.message.observe(this) {
            val usr = viewModel.userlogin.value
            checkResponseeLogin(it, usr?.loginResult?.token)
        }
    }
    private fun checkResponseeLogin(
        msg: String,
        tkn: String?
    ) {
        if (msg.contains("Login as")) {
            AlertDialog.Builder(this).apply {
                setTitle(resources.getString(R.string.pesandaftar))
                setMessage(resources.getString(R.string.loginberhasil))
                setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                    val intent = Intent(context, MainActivity::class.java)
                    intent.flags =
                        Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                    finish()
                }
                create()
                show()
            }

            UserPreference.setString("TOKEN", viewModel.userlogin.value?.loginResult?.token)
            UserPreference.setString("NAME", viewModel.userlogin.value?.loginResult?.name)
            UserPreference.setBoolean("LOGIN", true)


        } else {
            when (msg) {
                "Unauthorized" -> {
                    Toast.makeText(this, getString(R.string.unauthorized), Toast.LENGTH_SHORT)
                        .show()
                    binding.edLoginEmail.apply {
                        setText("")
                        requestFocus()
                    }
                    binding.edLoginPassword.setText("")

                }
                "timeout" -> {
                    Toast.makeText(this, getString(R.string.timeout), Toast.LENGTH_SHORT)
                        .show()
                }
                else -> {
                    Toast.makeText(
                        this,
                        "${getString(R.string.error_message)} $msg",
                        Toast.LENGTH_SHORT
                    )
                        .show()

                }
            }
        }
        showLoading(false)
        binding.myButton.isEnabled = true
    }
    private fun setupViewModel() {
        UserPreference.init(this@LoginActvity)
    }

    private fun setupAction() {
        binding.edLoginEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (Patterns.EMAIL_ADDRESS.matcher(binding.edLoginEmail.text.toString())
                        .matches()
                ) { // using EMAIL_ADREES matcher
                    binding.textFieldEmail.error = null
                } else {
                    binding.textFieldEmail.error = resources.getString(R.string.formatemail)
                }
            }

        })
        binding.edLoginPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })
        //  Patterns.EMAIL_ADDRESS.matcher(binding.edLoginEmail.text.toString()).matches()
        binding.myButton.setOnClickListener {
            binding.myButton.isEnabled = false
            binding.myButton.text = resources.getString(R.string.sendingdata)
            showLoading(true)
            val email = binding.edLoginEmail.text.toString()
            val password = binding.edLoginPassword.text.toString()
            when {
                email.isEmpty() -> {
                    binding.textFieldEmail.error = resources.getString(R.string.warningemail)
                }
                password.isEmpty() -> {
                    binding.textFieldPassword.error = resources.getString(R.string.warningpassword)
                }
                password.length < 6 -> {
                    binding.textFieldPassword.error =
                        resources.getString(R.string.warningpasswordkurang)
                }
                else -> {
                    viewModel.login(LoginEntity(email, password))
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun showSnackbarMessage(msg: String) {
        Snackbar.make(binding.root as ViewGroup, msg, Snackbar.LENGTH_SHORT).show()
    }

    private fun setMyButtonEnable() {
        val result = binding.edLoginPassword.text
        binding.myButton.isEnabled = result != null && result.toString().isNotEmpty()
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val emailLoginEditText =
            ObjectAnimator.ofFloat(binding.edLoginEmail, View.ALPHA, 1f).setDuration(500)
        val passwordLoginEditText =
            ObjectAnimator.ofFloat(binding.edLoginPassword, View.ALPHA, 1f).setDuration(500)
        val mybutton = ObjectAnimator.ofFloat(binding.myButton, View.ALPHA, 1f).setDuration(500)
        val textViewEmail =
            ObjectAnimator.ofFloat(binding.textViewEmail, View.ALPHA, 1f).setDuration(500)
        val textViewPassword =
            ObjectAnimator.ofFloat(binding.textViewPassword, View.ALPHA, 1f).setDuration(500)
        AnimatorSet().apply {
            playSequentially(
                textViewEmail,
                emailLoginEditText,
                textViewPassword,
                passwordLoginEditText,
                mybutton
            )
            start()
        }
    }

}