package com.example.ch20_firebase

import android.content.ContentValues
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ch20_firebase.databinding.ActivityMajorEnrollBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MajorEnrollActivity : AppCompatActivity() {
    private var fStudentMajorList : ArrayList<CourseListType> = arrayListOf()
    private var fStudentMajorSize = 0
    lateinit var fStudentInfo: SharedPreferences
    private lateinit var adapter: CourseListAdapter
    lateinit var binding: ActivityMajorEnrollBinding

    override fun onBackPressed() {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMajorEnrollBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fStudentInfo = getSharedPreferences("student_info", MODE_PRIVATE)
        var userNum = fStudentInfo.getString("usernum", "")

        val realtimeDB: FirebaseDatabase = FirebaseDatabase.getInstance()
        val userMajorRef = realtimeDB.reference.child(userNum.toString()).child("전공")
        val majorRef = realtimeDB.reference.child("Major")

        userMajorRef.addValueEventListener(object : ValueEventListener{
            val studentInitData: ArrayList<CourseListType> = arrayListOf()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (courseSnapshot in snapshot.children) {
                        var getData = courseSnapshot.getValue(CourseListType::class.java)
                        studentInitData.add(getData!!)
                    }
                }
                fStudentMajorList.addAll(studentInitData)
                fStudentMajorSize = studentInitData.size

                majorRef.get().addOnSuccessListener {
                    val majorData: ArrayList<CourseListType> = arrayListOf()
                    for (courseSnapshot in it.children) {
                        var getData = courseSnapshot.getValue(CourseListType::class.java)
                        majorData.add(getData!!)
                    }

                    adapter = CourseListAdapter(majorData)
                    var recyclerView: RecyclerView = binding.recyclerview
                    recyclerView.layoutManager = LinearLayoutManager(baseContext)
                    recyclerView.adapter = adapter

                    var searchViewTextListener: SearchView.OnQueryTextListener =
                        object : SearchView.OnQueryTextListener {
                            //검색버튼 입력시 호출, 검색버튼이 없으므로 사용하지 않음
                            override fun onQueryTextSubmit(s: String): Boolean {
                                return false
                            }

                            //텍스트 입력/수정시에 호출
                            override fun onQueryTextChange(s: String): Boolean {
                                adapter.getFilter().filter(s)
                                Log.d(ContentValues.TAG, "SearchVies Text is changed : $s")
                                return false
                            }
                        }

                    binding.searchView.setOnQueryTextListener(searchViewTextListener)

                    adapter.setItemClickListener(object : CourseListAdapter.OnItemClickListener {
                        override fun onClick(v: View, position: Int) {
                            //필터된 리스트가 검색시 매번 달라지기 때문에 일관적인 리스트에 순차적으로 저장.
                            if (fStudentMajorList.contains(
                                    CourseListType(
                                        adapter.filteredMajor[position].Course_NAME,
                                        adapter.filteredMajor[position].Course_POINT,
                                        adapter.filteredMajor[position].deep,
                                        adapter.filteredMajor[position].division,
                                        adapter.filteredMajor[position].spec_num
                                    )
                                )
                            ) {
                                Toast.makeText(baseContext, "이미 등록된 과목입니다", Toast.LENGTH_SHORT)
                                    .show()
                            } else {
                                fStudentMajorList.add(
                                    CourseListType(
                                        adapter.filteredMajor[position].Course_NAME,
                                        adapter.filteredMajor[position].Course_POINT,
                                        adapter.filteredMajor[position].deep,
                                        adapter.filteredMajor[position].division,
                                        adapter.filteredMajor[position].spec_num
                                    )
                                )
                                Toast.makeText(
                                    binding.root.context,
                                    "${adapter.filteredMajor[position].Course_NAME}\n 저장 되었습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onDelete(v: View, position: Int) {
                            if (fStudentMajorList.contains(
                                    CourseListType(
                                        adapter.filteredMajor[position].Course_NAME,
                                        adapter.filteredMajor[position].Course_POINT,
                                        adapter.filteredMajor[position].deep,
                                        adapter.filteredMajor[position].division,
                                        adapter.filteredMajor[position].spec_num
                                    )
                                )
                            ) {
                                fStudentMajorList.remove(
                                    CourseListType(
                                        adapter.filteredMajor[position].Course_NAME,
                                        adapter.filteredMajor[position].Course_POINT,
                                        adapter.filteredMajor[position].deep,
                                        adapter.filteredMajor[position].division,
                                        adapter.filteredMajor[position].spec_num
                                    )
                                )
                                Toast.makeText(
                                    binding.root.context,
                                    "${adapter.filteredMajor[position].Course_NAME}\n 삭제 되었습니다.",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                Toast.makeText(baseContext, "등록되지 않은 과목입니다", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    })

                }

            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })

        binding.enroll.setOnClickListener {
            //기존 등록한 과목에서 한 과목이라도 삭제시 리스트사이즈 오류 예방을 위해 사용자 과목정보 초기화
            for (i in 0 until fStudentMajorSize){
                userMajorRef.child(i.toString()).child("Course_NAME").setValue(null)
                userMajorRef.child(i.toString()).child("Course_POINT").setValue(null)
                userMajorRef.child(i.toString()).child("deep").setValue(null)
                userMajorRef.child(i.toString()).child("division").setValue(null)
                userMajorRef.child(i.toString()).child("spec_num").setValue(null)
            }
            for(i in 0 until fStudentMajorList.size){
                userMajorRef.child(i.toString()).child("Course_NAME").setValue(fStudentMajorList[i].Course_NAME)
                userMajorRef.child(i.toString()).child("Course_POINT").setValue(fStudentMajorList[i].Course_POINT)
                userMajorRef.child(i.toString()).child("deep").setValue(fStudentMajorList[i].deep)
                userMajorRef.child(i.toString()).child("division").setValue(fStudentMajorList[i].division)
                userMajorRef.child(i.toString()).child("spec_num").setValue(fStudentMajorList[i].spec_num)
            }
            startActivity(Intent(this,MainActivity::class.java))
            finish()

        }
    }
}