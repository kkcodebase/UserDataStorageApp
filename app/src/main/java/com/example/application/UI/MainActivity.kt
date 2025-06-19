package com.example.application.UI

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.application.R
import com.example.application.Model.UserDataApplication
import com.example.application.Model.UserItem
import com.example.application.Model.UserItemAdapter
import com.example.application.Model.UserItemClickListener
import com.example.application.ViewModel.UserItemModelFactory
import com.example.application.ViewModel.UserViewModel
import com.example.application.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), UserItemClickListener {

    private lateinit var binding: ActivityMainBinding
    private val userViewModel: UserViewModel by viewModels {
        UserItemModelFactory((application as UserDataApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        binding.btnNewUser.setOnClickListener {
            intent = Intent(applicationContext, AddNewUser::class.java)
            startActivity(intent)
        }

        setRecyclerView()

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun setRecyclerView() {
        val mainActivity = this
        userViewModel.userItems.observe(this){
            binding.userlistRecyclerView.apply {
                layoutManager = LinearLayoutManager(applicationContext)
                adapter = UserItemAdapter(it, mainActivity)
            }
        }
    }

    override fun editUserItem(userItem: UserItem) {
        NewUserSheet(userItem).show(supportFragmentManager, "newUserTag")
    }
}