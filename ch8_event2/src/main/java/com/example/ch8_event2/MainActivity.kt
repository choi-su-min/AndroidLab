package com.example.ch8_event2

import android.os.Bundle
import android.os.SystemClock
import androidx.appcompat.app.AppCompatActivity
import com.example.ch8_event2.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    var initTime = 0L
    var pauseTime = 0L
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startbutton.setOnClickListener{
            binding.chronometer.base = SystemClock.elapsedRealtime() + pauseTime
            binding.chronometer.start() //스톱워치 start 동작
            binding.stopbutton.isEnabled = true
            binding.resetbutton.isEnabled = true
            binding.startbutton.isEnabled = false
        }

        binding.stopbutton.setOnClickListener {
            pauseTime = binding.chronometer.base - SystemClock.elapsedRealtime() //멈춘 시각 저장
            binding.chronometer.stop() //스톱워치 stop 동작
            binding.stopbutton.isEnabled = false
            binding.resetbutton.isEnabled = true
            binding.startbutton.isEnabled = true
        }

        binding.resetbutton.setOnClickListener {
            pauseTime = 0L
            binding.chronometer.base = SystemClock.elapsedRealtime()
            binding.chronometer.start() //스톱워치는 계속 stop상태 유지
            binding.stopbutton.isEnabled = true
            binding.resetbutton.isEnabled = false
            binding.startbutton.isEnabled = true
        }
    }
}