package com.example.demo

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.demo.databinding.ActivityMainBinding
import com.google.gson.Gson
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val testdata = getJsonData("data.json")

        binding.recyclerview.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = MyRecyclerViewAdapter(testdata!!)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }

    }

    private fun getJsonData(filename: String): Course? {
        val assetManager = resources.assets
        var result: Course? = null
        try {
            val inputStream = assetManager.open(filename)
            val reader = inputStream.bufferedReader()
            val gson = Gson()
            result = gson.fromJson(reader, Course::class.java)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return result
    }
}
