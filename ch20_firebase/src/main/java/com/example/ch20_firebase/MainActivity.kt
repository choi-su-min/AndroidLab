package com.example.ch20_firebase

import android.animation.ObjectAnimator
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import com.example.ch20_firebase.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private var fFabOpen = false //이수과목 등록을 위한 플로팅버튼 객체 생성

    private lateinit var fStudentInfo: SharedPreferences //회원가입시 저장해두었던 사용자 id객체 가져오기.

    private lateinit var fMainFragment: Fragment

    //좌측상단에 darawer에 logout기능 구현을 위한 객체 생성
    lateinit var fNavigationView: NavigationView
    lateinit var fDrawerLayout: DrawerLayout

    //뒤로가기 버튼을 눌렀을 때 이전 액티비티로 넘어가는 것을 방지
    private var fBackPressedTime: Long = 0

    override fun onBackPressed() {
        if (fBackPressedTime + 2000 > System.currentTimeMillis()){
            super.onDestroy()
            finish()
        }else{
            makeText(applicationContext,"한 번 더 뒤로가기 버튼을 누르면 종료됩니다.",
                Toast.LENGTH_SHORT).show()
        }
        fBackPressedTime = System.currentTimeMillis()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //drawer열고 닫을 수 있는 버튼을 담고 있는 툴바 객체생성.
        val toolBar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolBar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.navi_menu)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        fDrawerLayout = findViewById(R.id.drawer)

        fNavigationView = binding.drawerMenu
        fNavigationView.setNavigationItemSelectedListener(this)

        //저장된 사용자 id와 입학년도
        fStudentInfo = getSharedPreferences("student_info", MODE_PRIVATE)
        var year = fStudentInfo.getString("year", "")

        val tcvSelect = MainFragment(year.toString()) //입학년도에 따른 메인화면을 결정하기 위한 객체 선언
        fMainFragment = tcvSelect.selectTCV() //입학년도에 해당하는 메인화면 결정
        val fragmentAdapter = MyFragmentPagerAdapter(this, year.toString())

        //선택된 탭에 따른 화면 적용.
        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                //사용자의 학번 정보
                var userNum = fStudentInfo.getString("usernum", "")


                val bundle1 : Bundle = bundleOf()   //졸업 소요 학점 프래그먼트에 전달할 정보를 담는 번들
                val bundle2 : Bundle = bundleOf()   //전공 이수 정보 프래그먼트에 전달할 정보를 담는 번들
                val bundle3 : Bundle = bundleOf()   //교양 이수 정보 프래그먼트에 전달할 정보를 담는 번들
                val bundle4 : Bundle = bundleOf()   //추천 과목 정보 프래그먼트에 전달할 정보를 담는 번들

                val transaction = supportFragmentManager.beginTransaction() //프래그먼트 매니저 선언

                when (tab?.text) {
                    "졸업 소요 학점" -> {
                        val fragment1 = fMainFragment
                        bundle1.putString("usernum", userNum.toString())   //번들에 사용자 데이터 저장
                        fragment1.arguments = bundle1   //프래그먼트에 번들 부착
                        //프래그먼트 실행
                        transaction.replace(R.id.tabContent, fragment1).addToBackStack(null).commit()
                    }

                    "전공 이수 정보" -> {
                        val fragment2 = TwoFragment()
                        bundle2.putString("usernum", userNum.toString())   //번들에 데이터 저장
                        fragment2.arguments = bundle2   //프래그먼트에 번들 부착
                        //프래그먼트 실행
                        transaction.replace(R.id.tabContent, fragment2).addToBackStack(null).commit()
                    }

                    "교양 이수 정보" -> {
                        val fragment3 = ThreeFragment()
                        bundle3.putString("usernum", userNum.toString())   //번들에 데이터 저장
                        fragment3.arguments = bundle3   //프래그먼트에 번들 부착
                        //프래그먼트 실행
                        transaction.replace(R.id.tabContent, fragment3).addToBackStack(null).commit()
                    }

                    "추천 과목 정보" -> {
                        val fragment4 = FourFragment()
                        bundle4.putString("usernum", userNum.toString())   //번들에 데이터 저장
                        bundle4.putString("year",year.toString())   //번들에 데이터 저장
                        fragment4.arguments = bundle4   //프래그먼트에 번들 부착
                        //프래그먼트 실행
                        transaction.replace(R.id.tabContent, fragment4).addToBackStack(null).commit()
                    }
                    "졸업 요건 정보" -> {
                        dialog()
                        val fragment5 = FiveFragment()
                        //프래그먼트 실행
                        transaction.replace(R.id.tabContent, fragment5).addToBackStack(null).commit()
                    }
                }
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {

            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {

            }
        })

        //'교양' 플로팅 버튼의 클릭리스너
        binding.fabLiberalArts.setOnClickListener {
            val intent1 = Intent(this, LiberalArtsEnrollActivity::class.java)   //인텐트 생성
            startActivity(intent1)   //액티비티 전환
            finish()   //메인 액티비티 종료
        }

        //'전공' 플로팅 버튼의 클릭리스너
        binding.fabMajor.setOnClickListener {
            if(year == "2021" || year == "2022"){
                val intent2 = Intent(this, MajorEnrollActivity21::class.java)   //인텐트 생성
                startActivity(intent2)   //액티비티 전환
                finish()   //메인 액티비티 종료
            }else{
                val intent2 = Intent(this, MajorEnrollActivity::class.java)   //인텐트 생성
                startActivity(intent2)   //액티비티 전환
                finish()   //메인 액티비티 종료
            }
        }

        //'등록하기' 플로팅 버튼의 클릭리스너
        binding.fabMain.setOnClickListener {
            toggleFab()
        }

        //뷰페이저에 어댑터 부착
        binding.viewpager.adapter = fragmentAdapter

        //각 탭에 맞는 텍스트 맵핑
        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
            when (position) {
                0 -> tab.text = "졸업 소요 학점"
                1 -> tab.text = "전공 이수 정보"
                2 -> tab.text = "교양 이수 정보"
                3 -> tab.text = "추천 과목 정보"
                4 -> tab.text = "졸업 요건 정보"
            }
        }.attach()


    }

    //드로워를 여는 경우 액션 구현부
    override fun onOptionsItemSelected(item: MenuItem): Boolean{
        when(item!!.itemId){
            android.R.id.home ->{
                fDrawerLayout.openDrawer(GravityCompat.START)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    //드로워 내에 있는 로그아웃 버튼 선택시 액션 구현부
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.logout -> {
                makeText(baseContext,"로그아웃", Toast.LENGTH_SHORT).show()
                var autoLoginInfo = getSharedPreferences("student_auto", MODE_PRIVATE) //자동로그인체크를 바꾸기 위한 sharedPreference객체 생성
                val editor = autoLoginInfo.edit()
                editor.putBoolean("autocheck", false) //로그아웃 항목 선택시 자동로그인체크를 false로 변경
                editor.commit()  //변경사항 저장
                val intent = Intent(this,LoginActivity::class.java)   //인텐트 생성
                startActivity(intent)   //로그인화면으로 전환
                finish()
            }
        }
        return false
    }

    //'등록하기' 플로팅 버튼을 클릭했을 때 나타나는 액션 구현부
    private fun toggleFab() {
        val fabLiberalArts : View = findViewById(R.id.fabLiberalArts)   //'교양' 플로팅 버튼 객체 생성
        val fabMajor : View = findViewById(R.id.fabMajor)   //'전공' 플로팅 버튼 객체 생성

        if (fFabOpen) {   //플로팅 버튼이 펼쳐져있을 경우
            ObjectAnimator.ofFloat(
                fabLiberalArts, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(
                fabMajor, "translationY", 0f).apply { start() }
        } else {   //플로팅 버튼이 합쳐져있을 경우
            ObjectAnimator.ofFloat(
                fabLiberalArts, "translationY", -160f).apply { start() }
            ObjectAnimator.ofFloat(
                fabMajor, "translationY", -320f).apply { start() }
        }
        fFabOpen = !fFabOpen
    }

    //졸업 요건 정보 탭을 선택했을 때 나타나는 alert dialog 구현부
    fun dialog(){
        //다이얼로그 선언과 내용 설정
        var dialog = AlertDialog.Builder(this)

        dialog.setTitle("         졸업 요건 정보입니다.")
        dialog.setMessage(
            """
                  '꿈미래개척 상담', '영어인증제', '개척인증제'
                          각 영역별로 하나 이상 이수하면
                                졸업 교건이 충족됩니다.
            """.trimIndent()
        )

        //다이얼로그에서 '확인' 버튼을 선택했을 때 액션 구현부
        var dialogListener = object: DialogInterface.OnClickListener{
            override fun onClick(dialog: DialogInterface?, which: Int) {

            }
        }

        //다이얼로그에 '확인' 버튼 부착
        dialog.setPositiveButton("확인",dialogListener)
        dialog.show()
    }
}