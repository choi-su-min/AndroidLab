package com.example.ch13_activity

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ch13_activity.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    var datas: MutableList<String>? = null
    lateinit var adapter: MyAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //메인액티비티에서 add액티비티를 intent를 통해 실행시켜야함
        //사후처리가 필요한 작업 -> 액티비티 런처를 준비
        //intent 처리자 : StartActivityForResult()
        val requestLauncher: ActivityResultLauncher<Intent> = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            //사후처리 내용, add액티비티가 넘긴 데이터
            it.data!!.getStringExtra("result")?.let {
                datas?.add(it)
                adapter.notifyDataSetChanged()
            }
        }
        //유저가 저장 버튼을 눌렀을 때 런처실행
        binding.mainFab.setOnClickListener{
            val intent = Intent(this, AddActivity::class.java)
            requestLauncher.launch(intent)
        }

        //매개변수로 넘어온 bundle객체가 null이 아닌경우 상태값이 있을 때 그것을 실행 했을 때 상태 값으로 쓰겠다는 의미
        //oncreate에서 유지되는 상태값을 화면으로 출력하겠다.
        datas = savedInstanceState?.let {
            it.getStringArrayList("datas")?.toMutableList()
        }?:let {
            mutableListOf<String>()
        }

        val layoutManager = LinearLayoutManager(this)
        binding.mainRecyclerView.layoutManager=layoutManager
        adapter=MyAdapter(datas)
        binding.mainRecyclerView.adapter=adapter
        binding.mainRecyclerView.addItemDecoration(
            DividerItemDecoration(this, LinearLayoutManager.VERTICAL)
        )
    }

    //상태데이터를 저장하는 함수
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putStringArrayList("datas", ArrayList(datas))
    }
    
}