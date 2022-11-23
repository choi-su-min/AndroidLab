package com.example.gmsystem

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.gmsystem.databinding.ActivityLiberalArtsEnrollBinding

class LiberalArtsEnrollActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityLiberalArtsEnrollBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val intent : Intent = Intent(this, MainActivity::class.java)

        binding.enroll.setOnClickListener(){
            startActivity(intent)
        }
    }
}