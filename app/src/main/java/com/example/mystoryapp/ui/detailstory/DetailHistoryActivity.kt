package com.example.mystoryapp.ui.detailstory

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityDetailHistoryBinding

class DetailHistoryActivity : AppCompatActivity() {
    companion object {
        const val ID = "x"
    }

    private lateinit var binding: ActivityDetailHistoryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_history)
        binding = ActivityDetailHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupView()
        setupAction()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setupAction() {
        val nama = intent.getStringExtra("name")
        val description = intent.getStringExtra("description")
        val createdAt = intent.getStringExtra("createdAt")
        val photoUrl = intent.getStringExtra("photoUrl")
        val addressName = intent.getStringExtra("addressName")
        binding.tvDetailName.text = nama
        binding.tvDetailDate.text = createdAt
        binding.tvDetailDescription.text = description
        binding.txtLocation.text = addressName
        Glide.with(this)
            .load(photoUrl)
            .centerCrop()
            .apply(
                RequestOptions.placeholderOf(R.drawable.ic_baseline_lock_24)
                    .error(R.drawable.ic_baseline_lock_24)
            )
            .into(binding.ivDetailPhoto)
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
        supportActionBar?.title = resources.getString(R.string.detailstory)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}