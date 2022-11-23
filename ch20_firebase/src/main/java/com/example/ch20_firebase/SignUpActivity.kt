package com.example.ch20_firebase

import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.ch20_firebase.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {
    val TAG: String = "Register"
    var fExistBlank = false
    lateinit var binding: ActivitySignUpBinding
    lateinit var fStudentInfo: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnRegister.setOnClickListener {
            Log.d(TAG, "회원가입 버튼 클릭")

            val usernum = binding.usernum.text.toString()
            val pw = binding.userpassword.text.toString()

            // 유저가 항목을 다 채우지 않았을 경우
            if(pw.isEmpty() || usernum.isEmpty()){
                fExistBlank = true
            }

            // 회원가입 성공 토스트 메세지 띄우기
            Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()

            // 유저가 입력한 id, pw를 쉐어드에 저장한다.
            fStudentInfo = getSharedPreferences("student_info", Activity.MODE_PRIVATE)
            val editor = fStudentInfo.edit()
            editor.putString("usernum", usernum)
            editor.putString("pw", pw)
            editor.putBoolean("autocheck", false)
            editor.commit()

            // 로그인 화면으로 이동
            val intent = Intent(this, LoginActivity::class.java)
            intent.putExtra("usernum",usernum)
            startActivity(intent)

            // 상태에 따라 다른 다이얼로그 띄워주기
            if(fExistBlank){   // 작성 안한 항목이 있을 경우
                dialog("blank")
            }


        }
    }

    // 회원가입 실패시 다이얼로그를 띄워주는 메소드
    fun dialog(type: String){
        val dialog = AlertDialog.Builder(this)

        // 작성 안한 항목이 있을 경우
        if(type.equals("blank")){
            dialog.setTitle("회원가입 실패")
            dialog.setMessage("입력란을 모두 작성해주세요")
        }

        val dialog_listener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {
                when(which){
                    DialogInterface.BUTTON_POSITIVE ->
                        Log.d(TAG, "다이얼로그")
                }
            }
        }

        dialog.setPositiveButton("확인",dialog_listener)
        dialog.show()
    }
}