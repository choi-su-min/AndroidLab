package com.example.ch15_service

import android.annotation.TargetApi
import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.ch15_outer.MyAIDLInterface
import com.example.ch15_service.databinding.ActivityMainBinding
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var connectionMode = "none"

    //1.Messenger방법으로 서비스를 이용하는 방법
    lateinit var messenger: Messenger
    lateinit var  replyMessenger: Messenger
    var messengerJob: Job? = null;

    //2.aidl방법으로 서비스를 이용하는 방법
    var aidlService: MyAIDLInterface? = null
    var aidlJob: Job? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //messenger..........
        onCreateMessengerService()

        //aidl................
        onCreateAIDLService()

        //jobscheduler......................
        onCreateJobScheduler()

    }

    override fun onStop() {
        super.onStop()
        if(connectionMode === "messenger"){
            onStopMessengerService()
        }else if(connectionMode === "aidl"){
            onStopAIDLService()
        }
        connectionMode="none"
        changeViewEnable()
    }

    fun changeViewEnable() = when (connectionMode) {
        "messenger" -> {
            binding.messengerPlay.isEnabled = false
            binding.aidlPlay.isEnabled = false
            binding.messengerStop.isEnabled = true
            binding.aidlStop.isEnabled = false
        }
        "aidl" -> {
            binding.messengerPlay.isEnabled = false
            binding.aidlPlay.isEnabled = false
            binding.messengerStop.isEnabled = false
            binding.aidlStop.isEnabled = true
        }
        else -> {
            //초기상태. stop 상태. 두 play 버튼 활성상태
            binding.messengerPlay.isEnabled = true
            binding.aidlPlay.isEnabled = true
            binding.messengerStop.isEnabled = false
            binding.aidlStop.isEnabled = false

            binding.messengerProgress.progress = 0
            binding.aidlProgress.progress = 0
        }
    }

    //messenger handler ......................
    inner class HandlerReplyMsg: Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                10 ->{
                    //서비스에서 전달한 데이터가 번들이기 때문에 번들로 변경
                    val bundle = msg.obj as Bundle
                    bundle.getInt("dutation")?.let {
                        when{
                            it > 0 ->{
                                //프로그레스 바의 맥스값을 전달받은 음악을 플레이하는 시간 설정.
                                binding.messengerProgress.max = it
                                //장시간으로 음악을 플레이하며 작업하기 때문에 코루틴으로 처리.
                                val backgroundScope = CoroutineScope(Dispatchers.Default + Job())
                                messengerJob = backgroundScope.launch {
                                    while(binding.messengerProgress.progress < binding.messengerProgress.max){
                                        delay(1000)
                                        binding.messengerProgress.incrementProgressBy(1000)
                                    }
                                }
                                changeViewEnable()
                            }
                            else -> {
                                connectionMode="none"
                                unbindService(messengerConnection)
                                //화면 조정함수 호출
                                changeViewEnable()
                            }
                        }
                    }
                }
            }
        }
    }
    //messenger connection ....................
    val messengerConnection: ServiceConnection = object: ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            Log.d("kkang", "onServiceConnected...")
            messenger = Messenger(p1)
            val msg = Message()
            msg.replyTo = replyMessenger
            msg.what = 10
            messenger.send(msg)
            connectionMode = "messenger"
        }

        override fun onServiceDisconnected(p0: ComponentName?) {

        }
    }


    private fun onCreateMessengerService() {
        replyMessenger = Messenger(HandlerReplyMsg())
        binding.messengerPlay.setOnClickListener{
            val intent = Intent("ACTION_SERVICE_Messenger")
            intent.setPackage("com.example.ch15_outer")
            bindService(intent, messengerConnection, Context.BIND_AUTO_CREATE)
        }
        binding.messengerStop.setOnClickListener{
            val msg = Message()
            msg.what = 20
            messenger.send(msg)
            unbindService(messengerConnection)
            messengerJob?.cancel()
            connectionMode="none"
            changeViewEnable()
        }
    }
    private fun onStopMessengerService() {
        val msg = Message()
        msg.what = 20
        messenger.send(msg)
        unbindService(messengerConnection)
    }

    //aidl connection .......................
    val aidlConnertion: ServiceConnection = object: ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            aidlService = MyAIDLInterface.Stub.asInterface(p1)
            aidlService!!.start()
            binding.aidlProgress.max = aidlService!!.maxDuration
            val backgroundScope = CoroutineScope(Dispatchers.Default + Job())
            aidlJob = backgroundScope.launch {
                while(binding.aidlProgress.progress < binding.aidlProgress.max){
                    delay(1000)
                    binding.aidlProgress.incrementProgressBy(1000)
                }
            }
            connectionMode = "aidl"
            changeViewEnable()
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            aidlService = null
        }
    }

    private fun onCreateAIDLService() {
        binding.aidlPlay.setOnClickListener{
            val intent = Intent("ACTION_SERVICE_AIDL")
            intent.setPackage("com.example.ch15_outer")
            bindService(intent, aidlConnertion, Context.BIND_AUTO_CREATE)
        }
        binding.aidlStop.setOnClickListener{
            aidlService!!.stop()
            unbindService(aidlConnertion)
            aidlJob?.cancel()
            connectionMode = "none"
            changeViewEnable()
        }
    }
    private fun onStopAIDLService() {
        unbindService(aidlConnertion)
    }

    //JobScheduler
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun onCreateJobScheduler(){
        var jobScheduler: JobScheduler? = getSystemService(JOB_SCHEDULER_SERVICE) as JobScheduler
        val builder = JobInfo.Builder(1, ComponentName(this,MyJobService::class.java))
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_UNMETERED)
        val jobInfo = builder.build()
        jobScheduler!!.schedule(jobInfo)
    }

}