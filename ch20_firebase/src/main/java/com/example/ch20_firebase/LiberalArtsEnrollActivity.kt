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
import com.example.ch20_firebase.databinding.ActivityLiberalArtsEnrollBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LiberalArtsEnrollActivity : AppCompatActivity() {
    var fStudentLiberalList: ArrayList<CourseListType> = arrayListOf()
    private var fStudentLiberalSize = 0
    lateinit var fStudentInfo: SharedPreferences
    private lateinit var adapter: CourseListAdapter
    lateinit var binding: ActivityLiberalArtsEnrollBinding

    override fun onBackPressed() {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityLiberalArtsEnrollBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        fStudentInfo = getSharedPreferences("student_info", MODE_PRIVATE)
        var userNum = fStudentInfo.getString("usernum", "")

        val realtimeDB: FirebaseDatabase = FirebaseDatabase.getInstance()
        val userLiberalRef = realtimeDB.reference.child(userNum.toString()).child("교양") //사용자 이름 or 학번 받기
        val liberalRef = realtimeDB.reference.child("LiberalArts")

        userLiberalRef.addValueEventListener(object : ValueEventListener{
            val studentInitData: ArrayList<CourseListType> = arrayListOf()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    for (courseSnapshot in snapshot.children) {
                        var getData = courseSnapshot.getValue(CourseListType::class.java)
                        studentInitData.add(getData!!)
                    }
                }
                fStudentLiberalList.addAll(studentInitData)
                fStudentLiberalSize = studentInitData.size

                liberalRef.get().addOnSuccessListener {
                    val listData: ArrayList<CourseListType> = arrayListOf()
                    for (courseSnapshot in it.children) {
                        var getData = courseSnapshot.getValue(CourseListType::class.java)
                        listData.add(getData!!)
                    }

                    adapter = CourseListAdapter(listData)
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
                            if (fStudentLiberalList.contains(
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
                                fStudentLiberalList.add(
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
                            if (fStudentLiberalList.contains(
                                    CourseListType(
                                        adapter.filteredMajor[position].Course_NAME,
                                        adapter.filteredMajor[position].Course_POINT,
                                        adapter.filteredMajor[position].deep,
                                        adapter.filteredMajor[position].division,
                                        adapter.filteredMajor[position].spec_num
                                    )
                                )
                            ) {
                                fStudentLiberalList.remove(
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

            for (i in 0 until fStudentLiberalSize){
                userLiberalRef.child(i.toString()).child("Course_NAME").setValue(null)
                userLiberalRef.child(i.toString()).child("Course_POINT").setValue(null)
                userLiberalRef.child(i.toString()).child("deep").setValue(null)
                userLiberalRef.child(i.toString()).child("division").setValue(null)
                userLiberalRef.child(i.toString()).child("spec_num").setValue(null)
            }
            for(i in 0 until fStudentLiberalList.size){
                userLiberalRef.child(i.toString()).child("Course_NAME").setValue(fStudentLiberalList[i].Course_NAME)
                userLiberalRef.child(i.toString()).child("Course_POINT").setValue(fStudentLiberalList[i].Course_POINT)
                userLiberalRef.child(i.toString()).child("deep").setValue(fStudentLiberalList[i].deep)
                userLiberalRef.child(i.toString()).child("division").setValue(fStudentLiberalList[i].division)
                userLiberalRef.child(i.toString()).child("spec_num").setValue(fStudentLiberalList[i].spec_num)
            }
            startActivity(Intent(this,MainActivity::class.java))
            finish()

        }
    }
}





