package com.example.mystoryapp.ui.createstory

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.example.mystoryapp.R
import com.example.mystoryapp.databinding.ActivityCreateStoryBinding
import com.example.mystoryapp.ui.camera.CameraActivity
import com.example.mystoryapp.ui.mainactivity.MainActivity
import com.example.mystoryapp.utils.reduceFileImage
import com.example.mystoryapp.utils.rotateBitmap
import com.example.mystoryapp.utils.uriToFile
import com.example.mystoryapp.viewmodel.ViewModelFactoryRep
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.IOException
import java.util.*

class CreateStoryActivity : AppCompatActivity() {
    private var getFile: File? = null
    private var lat: Double? = null
    private var lon: Double? = null
    private lateinit var binding: ActivityCreateStoryBinding
    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var viewModel: AddStoryViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
        setupView()
        setupAction()
        setMyButtonEnable()
        setCurrentLocation()
        setupViewModel()
        viewModel.message.observe(this) {
            showToast(it)
        }
    }
    private fun showToast(msg: String) {
        Toast.makeText(
            this@CreateStoryActivity,
            StringBuilder(getString(R.string.pesansuksesupload)).append(msg),
            Toast.LENGTH_SHORT
        ).show()

        if (msg == "Story created successfully") {
            startActivity(Intent(this@CreateStoryActivity, MainActivity::class.java))
            finish()
        }
    }
    private fun setupViewModel() {
        val factory = ViewModelFactoryRep.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[AddStoryViewModel::class.java]
    }
    private fun setCurrentLocation() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        //checklocation permission
        val task = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat
                .checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                101
            )
            return
        }
        task.addOnSuccessListener {
            if (it != null) {
                val addressName = getAddressName(it.latitude, it.longitude)
                lat = it.latitude
                lon = it.longitude
                binding.txtLocation.text = addressName
            }
        }
    }

    private fun getAddressName(lat: Double, lon: Double): String? {
        var addressName: String? = null
        val geocoder = Geocoder(this@CreateStoryActivity, Locale.getDefault())
        try {
            val list = geocoder.getFromLocation(lat, lon, 1)
            if (list != null && list.size != 0) {
                addressName = list[0].subAdminArea
                Log.d("x", "getAddressName: $addressName")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return addressName
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun setupAction() {
        binding.edAddDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.edAddDescription.text.toString()
                        .isNotEmpty()
                ) { // using EMAIL_ADREES matcher
                    binding.textFieldDescription.error = null
                    binding.buttonAdd.isEnabled = true
                } else {
                    binding.textFieldDescription.error = resources.getString(R.string.isi_deskripsi)
                    binding.buttonAdd.isEnabled = false
                }
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.edAddDescription.text.toString()
                        .isNotEmpty()
                ) { // using EMAIL_ADREES matcher
                    binding.textFieldDescription.error = null
                    binding.buttonAdd.isEnabled = true
                } else {
                    binding.textFieldDescription.error = resources.getString(R.string.isi_deskripsi)
                    binding.buttonAdd.isEnabled = false
                }
            }

        })
        binding.imgAddPhoto.setOnClickListener { startCameraX() }
        binding.imgGallery.setOnClickListener { startGallery() }
        binding.buttonAdd.setOnClickListener { uploadImage() }
    }

    private fun uploadImage() {
        binding.buttonAdd.isEnabled = false
        showLoading(true)
        when {
            getFile == null -> {
                showLoading(false)
                binding.buttonAdd.isEnabled = true
                Toast.makeText(
                    this@CreateStoryActivity,
                    resources.getString(R.string.warningberkas),
                    Toast.LENGTH_SHORT
                ).show()
            }
            binding.edAddDescription.text == null ->{
                showLoading(false)
                binding.buttonAdd.isEnabled = true
                Toast.makeText(
                    this@CreateStoryActivity,
                    resources.getString(R.string.warningdescription),
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> {
                // masukan data
                val file = reduceFileImage(getFile as File)
                val description =  binding.edAddDescription.text.toString().toRequestBody("text/plain".toMediaType())
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,
                    requestImageFile
                )
                viewModel.upload(
                    imageMultipart,
                    description,
                    lat  ,
                    lon
                )
            }
        }
    }
    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, resources.getString(R.string.pilihphoto))
        launcherIntentGallery.launch(chooser)
    }
    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@CreateStoryActivity)
            getFile = myFile
            binding.previewImageView.setImageURI(selectedImg)
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }
    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
                isBackCamera
            )

            binding.previewImageView.setImageBitmap(result)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.permisioncamera),
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun setMyButtonEnable() {
        val result = binding.edAddDescription.text
        binding.buttonAdd.isEnabled = result != null && result.toString().isNotEmpty()
    }

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

}