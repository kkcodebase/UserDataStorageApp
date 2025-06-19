package com.example.application.UI

import android.app.Activity
import android.app.AlertDialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.application.R
import com.example.application.Model.UserDataApplication
import com.example.application.Model.UserItem
import com.example.application.ViewModel.UserItemModelFactory
import com.example.application.ViewModel.UserViewModel
import com.example.application.databinding.UserDataBinding

class AddNewUser : AppCompatActivity() {

    private val CAMERA_PERMISSION_CODE = 1000
    private val READ_PERMISSION_CODE = 1001

    private val IMAGE_CHOOSE = 1002
    private val IMAGE_CAPTURE = 1003

    private var imageUri: Uri? = null

    private lateinit var binding: UserDataBinding
    private val userViewModel: UserViewModel by viewModels {
        UserItemModelFactory((application as UserDataApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = UserDataBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvProfile.setOnClickListener {
            showImagePickDialog()
        }

        binding.btnSave.setOnClickListener {
            val userName = binding.inpName.text.toString()
            val userEmail = binding.inpEmail.text.toString()
            val userPhone = binding.inpPhone.text.toString()
            val userAge = binding.inpAge.text.toString()
            val userProfile = imageUri.toString()

            val newUser = UserItem(userName, userEmail, userPhone, userAge, userProfile)
            userViewModel.addUserItem(newUser)

            binding.inpName.setText(" ")
            binding.inpEmail.setText(" ")
            binding.inpPhone.setText(" ")
            binding.inpAge.setText(" ")
            finish()
        }
    }

    private fun showImagePickDialog() {
        val options = arrayOf("Camera", "Gallery")
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Pick Image From")
        builder.setItems(options) { dialog, which ->
            when (which) {
                0 -> {
                    val permissionGranted = requestCameraPermission()
                    if (permissionGranted) {
                        openCameraInterface()
                    }
                }
                1 -> {
                    val permissionGranted = requestStoragePermission()
                    if (permissionGranted) {
                        chooseImageGallery()
                    }
                }
            }
        }
        builder.show()
    }

    private fun requestStoragePermission(): Boolean {
        var permissionGranted = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
            val storagePermissionNotGranted = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
            if (storagePermissionNotGranted) {
                val permission = arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE)
                requestPermissions(permission, READ_PERMISSION_CODE)
            } else {
                permissionGranted = true
            }
        } else {
            permissionGranted = true
        }
        return permissionGranted
    }

    private fun requestCameraPermission(): Boolean {
        var permissionGranted = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val cameraPermissionNotGranted = ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_DENIED
            if (cameraPermissionNotGranted) {
                val permission = arrayOf(android.Manifest.permission.CAMERA)
                requestPermissions(permission, CAMERA_PERMISSION_CODE)
            } else {
                permissionGranted = true
            }
        } else {
            permissionGranted = true
        }
        return permissionGranted
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openCameraInterface()
            } else {
                showAlert("Camera permission was denied. Unable to take a picture.")
            }
        } else if (requestCode == READ_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                chooseImageGallery()
            } else {
                showAlert("Storage permission was denied.")
            }
        }
    }

    private fun chooseImageGallery() {
        //val intent = Intent(Intent.ACTION_PICK)
        //intent.type = "image/*"
        //startActivityForResult(intent, IMAGE_CHOOSE)

        val intent: Intent =
            Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI
            )
        startActivityForResult(intent, IMAGE_CHOOSE)

    }

    private fun openCameraInterface() {
        val values = ContentValues()
        values.put(MediaStore.Images.Media.TITLE, "New Picture")
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)

        if (imageUri != null) {
            val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
                putExtra(MediaStore.EXTRA_OUTPUT, imageUri)
            }
            startActivityForResult(intent, IMAGE_CAPTURE)
        } else {
            showAlert("Failed to create image file, please try again.")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                IMAGE_CAPTURE -> binding.imageView.setImageURI(imageUri)
                IMAGE_CHOOSE -> {
                    imageUri = data?.data
                    binding.imageView.setImageURI(imageUri)
                }
            }
        }
    }

    private fun showAlert(message: String) {
        val builder = AlertDialog.Builder(this)
        builder.setMessage(message)
        builder.setPositiveButton(R.string.ok_button_title, null)
        builder.create().show()
    }
}
