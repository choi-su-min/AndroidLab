package com.example.ch14_receiver

import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.os.BatteryManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.ch14_receiver.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //1.Myreceiver를 발생시키는 것은 버튼을 눌렀을 때 intent로 처리.
        //2.휴대폰의 배터리상태를 파악.

        //registerReceiver는 동적으로 broadcast receiver를 등록하기 위함
        //첫 번째 매개변수에서는 실행시켜야하는 우리가 준비한 객체를 대입.(일반적.)
        //null은 대입을 하지 않고 시스템의 배터리 상황의 획득만 함.
        registerReceiver(null, IntentFilter(Intent.ACTION_BATTERY_CHANGED))!!.apply {
            when(getIntExtra(BatteryManager.EXTRA_STATUS,-1)){
                BatteryManager.BATTERY_STATUS_CHARGING -> {
                    when(getIntExtra(BatteryManager.EXTRA_PLUGGED,-1)){
                        BatteryManager.BATTERY_PLUGGED_USB->{
                            binding.chargingResultView.text = "USB Plugged"
                            binding.chargingImageView.setImageBitmap(BitmapFactory.decodeResource(
                                resources,R.drawable.usb
                            ))
                        }
                        BatteryManager.BATTERY_PLUGGED_AC->{
                            binding.chargingResultView.text="AC Plugged"
                            binding.chargingImageView.setImageBitmap(BitmapFactory.decodeResource(
                                resources, R.drawable.ac
                            ))
                        }
                    }
                }
                else ->{
                    binding.chargingResultView.text="Not Plugged"
                }
            }
            val level = getIntExtra(BatteryManager.EXTRA_LEVEL,-1)
            val scale = getIntExtra(BatteryManager.EXTRA_SCALE,-1)
            val batteryPct = level/scale.toFloat()*100
            binding.percentResultView.text = "$batteryPct %"
        }

        binding.button.setOnClickListener{
            val intent = Intent(this, MyReceiver::class.java)
            sendBroadcast(intent)
        }
    }
}