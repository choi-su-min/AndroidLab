package com.example.ch20_firebase

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ch20_firebase.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    val TAG: String = "LoginActivity"
    lateinit var binding: ActivityLoginBinding
    private lateinit var fStudentInfo: SharedPreferences
    private lateinit var fAutoLoginInfo: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fStudentInfo = getSharedPreferences("student_info", MODE_PRIVATE)
        fAutoLoginInfo = getSharedPreferences("student_auto", MODE_PRIVATE)

        val editor = fStudentInfo.edit()

        val savedUsernum:String? = fStudentInfo.getString("usernum", null)
        val savedPw:String? = fStudentInfo.getString("pw", null)
        val auto = fAutoLoginInfo.edit()

        binding.autoCheck.setOnCheckedChangeListener { _, _ ->
            when(binding.autoCheck.isChecked){
                true -> {
                    auto.putBoolean("autocheck",binding.autoCheck.isChecked)
                    auto.putString("autopw",savedPw)
                    auto.putString("autousernum",savedUsernum)
                    auto.commit()
                }
                false -> {
                    auto.putBoolean("autocheck", binding.autoCheck.isChecked)
                    auto.clear()
                    auto.commit()
                }
            }
        }

        if( fAutoLoginInfo.getBoolean("autocheck", binding.autoCheck.isChecked) == true){
            var autopw:String? = fAutoLoginInfo.getString("autopw", "")
            var autousernum:String? = fAutoLoginInfo.getString("autousernum", "")
            if(autousernum == savedUsernum && autopw == savedPw){
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }

        binding.btnLogin.setOnClickListener {
            val savedPw = fStudentInfo.getString("pw", "")
            val savedUsernum = fStudentInfo.getString("usernum", "")
            var pw:String? = binding.userpassword.text.toString()
            var usernum:String? = binding.usernum.text.toString()
            var year:String? = binding.year.text.toString()
            // 쉐어드로부터 저장된 id, pw 가져오기
            if(usernum == savedUsernum && pw == savedPw){
                // 로그인 성공 다이얼로그 보여주기
                editor.putString("year",year)
                editor.commit()
                dialog("success")
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
            else{
                // 로그인 실패 다이얼로그 보여주기
                dialog("fail")
            }
            binding.userpassword.text = null
            binding.usernum.text = null
            binding.year.text = null
        }

        // 회원가입 버튼
        binding.btnRegister.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        binding.btnRemove.setOnClickListener {
            fStudentInfo = getSharedPreferences("student_info", MODE_PRIVATE)
            val editor = fStudentInfo.edit()
            editor.clear()
            editor.commit()
            Toast.makeText(baseContext,"회원탈퇴",Toast.LENGTH_SHORT).show()
        }

    }

    // 로그인 성공/실패 시 다이얼로그를 띄워주는 메소드
    fun dialog(type: String){
        var dialog = AlertDialog.Builder(this)

        if(type.equals("success")){
            dialog.setTitle("로그인 성공")
            dialog.setMessage("로그인 성공!")
        }
        else if(type.equals("fail")){
            dialog.setTitle("로그인 실패")
            dialog.setMessage("아이디와 비밀번호를 확인해주세요")
        }

        var dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }
}