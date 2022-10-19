package com.example.mystoryapp.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
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
import com.example.mystoryapp.data.model.RegisterEntity
import com.example.mystoryapp.databinding.ActivitySignUpBinding
import com.example.mystoryapp.viewmodel.ViewModelFactoryRep
import com.google.android.material.snackbar.Snackbar

class SignUpActivity : AppCompatActivity() {
    private lateinit var viewModel: RegisterViewModel
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        playAnimation()
        setupViewModel()
        setupAction()
        setMyButtonEnable()
        viewModel.message.observe(this) {
            checkResponseeRegister(it)
        }
    }
    private fun checkResponseeRegister(msg: String) {
        if (msg == "User created") {
            showLoading(false)
            AlertDialog.Builder(this).apply {
                setTitle(resources.getString(R.string.pesandaftar))
                setMessage(resources.getString(R.string.pesanberhasildaftar))
                setPositiveButton(resources.getString(R.string.ok)) { _, _ ->
                    finish()
                }
                create()
                show()
            }
        } else {
            when (msg) {
                "Bad Request" -> {
                    Toast.makeText(this, getString(R.string.email_taken), Toast.LENGTH_SHORT).show()
                    binding.edRegisterEmail.apply {
                        setText("")
                        requestFocus()
                    }
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
            showLoading(false)
        }
    }
    private fun setupViewModel() {
        val factory = ViewModelFactoryRep.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[RegisterViewModel::class.java]
    }

    private fun setupAction() {

        binding.edRegisterEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (Patterns.EMAIL_ADDRESS.matcher(binding.edRegisterEmail.text.toString())
                        .matches()
                ) { // using EMAIL_ADREES matcher
                    binding.textFieldEmail.error = null
                } else {
                    binding.textFieldEmail.error = resources.getString(R.string.formatemail)
                }
            }

        })

        binding.edRegisterName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.edRegisterName.text.toString()
                        .isNotEmpty()
                ) { // using EMAIL_ADREES matcher
                    binding.textFieldNama.error = null
                    binding.myButton.isEnabled =
                        binding.edRegisterPassword.text.toString().length >= 6
                                && Patterns.EMAIL_ADDRESS.matcher(binding.edRegisterEmail.text.toString())
                            .matches()
                } else {
                    binding.textFieldNama.error = resources.getString(R.string.warningnama)
                    binding.myButton.isEnabled = false
                }
            }

        })
        binding.edRegisterPassword.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                setMyButtonEnable()
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
        binding.myButton.setOnClickListener {
            storeDataRegistration()
        }

    }

    private fun storeDataRegistration() {
        binding.myButton.isEnabled = false
        showLoading(true)
        binding.myButton.text = resources.getString(R.string.sendingdata)
        val nama = binding.edRegisterName.text.toString()
        val email = binding.edRegisterEmail.text.toString()
        val password = binding.edRegisterPassword.text.toString()
        when {
            nama.isEmpty() -> {
                binding.textFieldNama.error = resources.getString(R.string.warningnama)
            }
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
            !Patterns.EMAIL_ADDRESS.matcher(binding.edRegisterEmail.text.toString()).matches() -> {

            }
            else -> {
                viewModel.register(RegisterEntity(nama, email, password))
            }
        }

    }

    private fun showSnackbarMessage(msg: String) {
        Snackbar.make(binding.root as ViewGroup, msg, Snackbar.LENGTH_SHORT).show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
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
        ObjectAnimator.ofFloat(binding.imageView2, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()
        val namaLoginEditText =
            ObjectAnimator.ofFloat(binding.edRegisterName, View.ALPHA, 1f).setDuration(500)
        val emailLoginEditText =
            ObjectAnimator.ofFloat(binding.edRegisterEmail, View.ALPHA, 1f).setDuration(500)
        val passwordLoginEditText =
            ObjectAnimator.ofFloat(binding.edRegisterPassword, View.ALPHA, 1f).setDuration(500)
        val myButton = ObjectAnimator.ofFloat(binding.myButton, View.ALPHA, 1f).setDuration(500)
        val textviewName =
            ObjectAnimator.ofFloat(binding.textViewName, View.ALPHA, 1f).setDuration(500)
        val textViewEmail =
            ObjectAnimator.ofFloat(binding.textViewEmail, View.ALPHA, 1f).setDuration(500)
        val textViewPassword =
            ObjectAnimator.ofFloat(binding.textViewPassword, View.ALPHA, 1f).setDuration(500)
        AnimatorSet().apply {
            playSequentially(
                textviewName,
                namaLoginEditText,
                textViewEmail,
                emailLoginEditText,
                textViewPassword,
                passwordLoginEditText,
                myButton
            )
            start()
        }
    }

    private fun setMyButtonEnable() {
        val result = binding.edRegisterPassword.text
        binding.myButton.isEnabled = result != null && result.toString().isNotEmpty()
    }
}