package com.example.ch15_outer

import android.app.Service
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.*

class MyMessengerService : Service() {
    //액티비티와 상호 데이터를 주고 받음. 메신저를 이용하기 위해 메신저 객체를 선언
    //1번 메신저 객체 액티비티에서 넘어오는 데이터를 받는 용도
    //2번 액티비티에 데이터를 전달하는 용도
    lateinit var messenger:Messenger
    lateinit var replyMessenger: Messenger
    lateinit var player: MediaPlayer

    override fun onCreate() {
        super.onCreate()
        player = MediaPlayer()
    }

    override fun onDestroy() {
        super.onDestroy()
        player.release()
    }

    //액티비티로부터 데이터가 전달 되었을 때 그 데이터를 받아서 처리할 수 있는 핸들러를 상속받은 클래스를 준비
    //이 클래스내이 핸들메시지 함수가 외부에서 데이터가 메신저 방법으로 전달 되었을 때 콜 되는것. 매개변수는 외부에서 전달한 데이터
    inner class IncomingHandler(
        context: Context,
        private val applicationContext: Context = context.applicationContext
    ): Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            when(msg.what){
                //서비스에 연결되자마자인 상황을 가정
                //액티비티에서 요청이 들어왔을 때
                10 ->{
                    replyMessenger = msg.replyTo
                    if (!player.isPlaying){
                        player = MediaPlayer.create(this@MyMessengerService, R.raw.music)
                        try{
                            //넘길 데이터 선언 메시지 타입.
                            val  replyMsg = Message()
                            replyMsg.what = 10
                            val replyBundle = Bundle()
                            replyBundle.putInt("duration", player.duration)
                            //실제로 넘겨야하는 데이터
                            replyMsg.obj = replyBundle
                            //액티비티에 데이터를 전달
                            replyMessenger.send(replyMsg)
                            player.start()
                        }catch (e:Exception){9
                            e.printStackTrace()
                        }
                    }
                }
                20 ->{
                    if (player.isPlaying)
                        player.stop()
                }
                else -> super.handleMessage(msg)
            }
        }
    }
    //위에 준비된 핸들러를 아래에 등록.
    override fun onBind(intent: Intent): IBinder {
        messenger = Messenger(IncomingHandler(this))
        return messenger.binder
    }
}