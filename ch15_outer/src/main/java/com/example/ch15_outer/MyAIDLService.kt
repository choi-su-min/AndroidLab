package com.example.ch15_outer

import android.app.Service
import android.content.Intent
import android.media.MediaPlayer
import android.os.IBinder

class MyAIDLService : Service() {
    lateinit var player: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }
    //stub를 이용하여 외부 앱과 연동
    override fun onBind(intent: Intent): IBinder {
        //myaidl인터페이스에 있는 함수를 상속받아 사용
        return object: MyAIDLInterface.Stub(){
            override fun getMaxDuration(): Int {
                return if (player.isPlaying)
                    player.duration
                else 0
            }

            override fun start() {
                if (!player.isPlaying){
                    player = MediaPlayer.create(this@MyAIDLService, R.raw.music)
                    try {
                        player
                            .start()
                    }catch (e:Exception){
                        e.printStackTrace()
                    }
                }
            }

            override fun stop() {
                if (player.isPlaying)
                    player.stop()
            }
        }
    }
}