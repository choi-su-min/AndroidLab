package com.example.ch10_notification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.media.RingtoneManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.RemoteInput
import com.example.ch10_notification.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() { //호환성문제를 해결해주는 appcompat라이브러리 -> AppCompatActivity클래스를 상속받아 사용
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState) //부모클래스의 onCreate메서드를 끌어다가 사용
        val binding = ActivityMainBinding.inflate(layoutInflater)//뷰 바인딩을 위한 객체 binding생성 및 inlate()메서드를 이용한 뷰 초기화
        setContentView(binding.root)//초기화된 뷰 객체를 binding을 통해 화면에 출력
        binding.notificationButton.setOnClickListener { //알림 발생 버튼을 눌렀을 때 이벤트 처리 코드
            val manager = getSystemService(NOTIFICATION_SERVICE)
                    as NotificationManager //notificationmanager의 manager객체를 생성.
            val builder: NotificationCompat.Builder //notification객체는 notification 빌더에 의해서 만들어진다.?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) { //notification 빌더를 만드는 방법 차이에 의해 if문으로 채널 개념을 적용하는지 마는지를 정해줌.
                // 26 버전 이상
                val channelId = "one-channel"
                val channelName = "My Channel One"
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    NotificationManager.IMPORTANCE_DEFAULT
                ).apply { //apply블록 내에서 객체에 바로 접근할 수 있도록 해줌.
                    // 채널에 다양한 정보 설정
                    description = "My Channel One EventLogTags.Description"
                    setShowBadge(true)
                    val uri: Uri = RingtoneManager
                        .getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)
                    val audioAttributes = AudioAttributes.Builder()
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .setUsage(AudioAttributes.USAGE_ALARM)
                        .build()
                    setSound(uri, audioAttributes) //채널에 사운드를 적용하는 코드
                    enableVibration(true) //진동 설정하는 코드
                }
                // 채널을 NotificationManager에 등록
                manager.createNotificationChannel(channel) //채널 생성
                // 채널을 이용하여 builder 생성
                builder = NotificationCompat.Builder(this, channelId) //빌더를 만들어 채널 id를 명시하여 알림을 발생할 수 있도록 함.
            } else {
                // 26 버전 이하
                builder = NotificationCompat.Builder(this)
            }
            builder.run { //이미 생성된 객체에 연속적인 잡업을 필요로 할 때 사용하는 함수
                // 알림의 기본 정보
                setSmallIcon(R.drawable.small)
                setWhen(System.currentTimeMillis())
                setContentTitle("홍길동")
                setContentText("안녕하세요")
                setLargeIcon(BitmapFactory.decodeResource(resources, R.drawable.big))
            }
            val KEY_TEXT_REPLY = "key_text_reply"
            var replyLabel: String = "답장"
            var remoteInput: RemoteInput = RemoteInput.Builder(KEY_TEXT_REPLY).run { //브로드캐스트리시버에 전달할 remoteInput을 준비
                setLabel(replyLabel)
                build()
            }
            val replyIntent = Intent(this, ReplyReceiver::class.java)
            val replyPendingIntent = PendingIntent.getBroadcast( //아직 처리되지 않은 intent로 사용자가 답장을 하는 시점에 실행하게 만들어줌
                this, 30, replyIntent, PendingIntent.FLAG_MUTABLE)
            builder.addAction( //action에 의해 발생하게끔 처리함, 사용자가 답장을 보낸 순간에 처리 하게끔 만듬.
                NotificationCompat.Action.Builder(
                    R.drawable.send,
                    "답장",
                    replyPendingIntent
                ).addRemoteInput(remoteInput).build()
            )
            manager.notify(11,builder.build())
        }
    }
}
