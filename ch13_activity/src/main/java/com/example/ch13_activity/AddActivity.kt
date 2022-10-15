package com.example.ch13_activity

import android.app.Activity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.ch13_activity.databinding.ActivityAddBinding

class AddActivity : AppCompatActivity() {
    lateinit var binding: ActivityAddBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return super.onCreateOptionsMenu(menu)
    }
    //addactivity에서 글을 입력하면 save버튼을 눌렀을 때 (메뉴 이벤트 처리) 유저가 입려한 데이터가 메인 액티비티로 보내줘야함.
    //메뉴 이벤트 처리를 위한 함수를 상속
    override fun onOptionsItemSelected(item: MenuItem): Boolean = when(item.itemId){
        R.id.menu_add_save -> {
            val intent = intent
            //유저가 입력한 글을 Extra로 담아줌.
            intent.putExtra("result", binding.addEditView.text.toString())
            //입력한 데이터값을 잘 처리했다는 것을 알려줌
            setResult(Activity.RESULT_OK, intent)
            //addactivity가 종료함과 동시에 자동으로 메인 액티비티로 넘어감.
            finish()
            true
        }
        else -> true
    }
}