package com.example.ch20_firebase

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ch20_firebase.databinding.ActivityCourseBinding
import com.example.ch20_firebase.databinding.ActivityMainBinding

class UserListActivity : AppCompatActivity() {
    private lateinit var adapter: UserAdapter
    var mylist: MutableList<MyList> = mutableListOf()
    lateinit var binding: ActivityCourseBinding
    lateinit var mainbinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        mainbinding = ActivityMainBinding.inflate(layoutInflater)
        binding = ActivityCourseBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter = UserAdapter(this)
        val recyclerView : RecyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter
    }
}





