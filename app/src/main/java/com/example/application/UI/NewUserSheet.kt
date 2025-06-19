package com.example.application.UI

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.example.application.Model.UserItem
import com.example.application.ViewModel.UserViewModel
import com.example.application.databinding.FragmentNewUserSheetBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NewUserSheet(var userItem: UserItem?) : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNewUserSheetBinding
    private lateinit var userViewModel: UserViewModel
    private var imageUri: Uri? = null


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = requireActivity()
        if(userItem != null){
            binding.userTitle.text = "VIEW USER INFOMATION"
            binding.btnSave.text="EDIT"
            val editable = Editable.Factory.getInstance()

            val profileString = userItem!!.profile
            //val imageUri: Uri = Uri.parse(profileString)
            imageUri = Uri.parse(profileString)

            binding.inpName.text = editable.newEditable(userItem!!.name)
            binding.inpEmail.text = editable.newEditable(userItem!!.email)
            binding.inpPhone.text = editable.newEditable(userItem!!.phone)
            binding.inpAge.text = editable.newEditable(userItem!!.age)
            binding.imageView.setImageURI(imageUri)//= editable.newEditable(userItem!!.profile) string to uri
        }
        else{
            binding.userTitle.text = "ADD USER INFORMATION"
        }
        userViewModel = ViewModelProvider(activity).get(UserViewModel::class.java)
        binding.btnSave.setOnClickListener {
            saveAction()
        }

    binding.imageView.setOnClickListener {
        imageUri?.let { uri ->
            val intent = Intent(activity, ImageViewActivity::class.java).apply {
                putExtra("IMAGE_URI", uri.toString())
            }
            startActivity(intent)
        }
    }

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentNewUserSheetBinding.inflate(inflater,container,false)
        return binding.root
    }

    private fun saveAction() {
        val userName = binding.inpName.text.toString()
        val userEmail = binding.inpEmail.text.toString()
        val userPhone = binding.inpPhone.text.toString()
        val userAge = binding.inpAge.text.toString()
        val userProfile = imageUri?.toString() ?: ""

        if(userItem == null) {
            val newUser = UserItem(userName, userEmail, userPhone,userAge, userProfile)
            userViewModel.addUserItem(newUser)
        }
        else{
            userItem!!.name = userName
            userItem!!.email = userEmail
            userItem!!.phone = userPhone
            userItem!!.age = userAge
            userItem!!.profile = userProfile
            userViewModel.updateUserItem(userItem!!)
        }
        binding.inpName.setText(" ")
        binding.inpEmail.setText(" ")
        binding.inpPhone.setText(" ")
        binding.inpAge.setText(" ")
        dismiss()
    }
}