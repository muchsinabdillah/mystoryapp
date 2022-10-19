package com.example.mystoryapp.ui.mainactivity

import UserPreference
import android.app.ProgressDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityMainBinding
import com.example.mystoryapp.ui.createstory.CreateStoryActivity
import com.example.mystoryapp.ui.createstory.LoadingStateAdapter
import com.example.mystoryapp.ui.createstory.StoryAdapter
import com.example.mystoryapp.ui.map.MapsActivity
import com.example.mystoryapp.ui.welcome.WelcomeActivity
import com.example.mystoryapp.viewmodel.GetStoryAllViewModel
import com.example.mystoryapp.viewmodel.ViewModelFactoryRep


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var getStoryAllModel: GetStoryAllViewModel
    private lateinit var adapter: StoryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        UserPreference.init(this@MainActivity)
        showLoading(true)
        setupView()
        setupViewModel()
        initialData()
        getAllStory()
        setupAction()
    }

    override fun onResume() {
        super.onResume()
        showLoading(true)
        setupViewModel()
        initialData()
        getAllStory()
    }

    override fun onStart() {
        super.onStart()
        showLoading(true)
        setupViewModel()
        initialData()
        getAllStory()
    }

    private fun initialData() {
        val factory = ViewModelFactoryRep.getInstance(this)
        getStoryAllModel = ViewModelProvider(this, factory)[GetStoryAllViewModel::class.java]
    }

    private fun getAllStory() {
        showLoading(true)
        binding.rvMovie.layoutManager = LinearLayoutManager(this)
        adapter = StoryAdapter()
        binding.rvMovie.adapter = adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        getStoryAllModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
            showLoading(false)
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

    private fun setupViewModel() {
        UserPreference.init(this@MainActivity)
        val token = UserPreference.getString("TOKEN")
        if (token.isEmpty()) {
            startActivity(Intent(this@MainActivity, WelcomeActivity::class.java))
            finish()
        } else {
            binding.textNamaUser.text = UserPreference.getString("NAME")
            initialData()
            getAllStory()
        }
    }

    private fun setupAction() {
        binding.actionLogout.setOnClickListener {
            logout()

        }
        binding.actionAddStory.setOnClickListener {
            startActivity(Intent(this, CreateStoryActivity::class.java))

        }
        binding.imgLanguange.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        binding.imageView6.setOnClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
        }
    }

    private fun logout() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setMessage(resources.getString(R.string.keluarapliksi))

        builder.setPositiveButton(
            resources.getString(R.string.YA)
        ) { _, _ -> //perform any action
            val loadingdata = ProgressDialog(this)
            loadingdata.setMessage(resources.getString(R.string.memuatdata))
            loadingdata.show()
            UserPreference.clearSharedPreference()
            loadingdata.dismiss()
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }

        builder.setNegativeButton(
            resources.getString(R.string.TIDAK)
        ) { _, _ -> //perform any action
            Toast.makeText(
                applicationContext,
                resources.getString(R.string.gagalkeluar),
                Toast.LENGTH_SHORT
            ).show()
        }

        //creating alert dialog
        val alertDialog: AlertDialog = builder.create()
        alertDialog.show()
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}