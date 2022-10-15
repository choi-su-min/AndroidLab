package com.example.ch20_firebase

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ch20_firebase.databinding.ActivityCourseBinding
import com.example.ch20_firebase.databinding.ActivityMainBinding

open class CourseListActivity : AppCompatActivity() {
    private lateinit var adapter: ListAdapter
    var mylist: ArrayList<MyList> = arrayListOf()
    lateinit var binding: ActivityCourseBinding
    lateinit var mainbinding: ActivityMainBinding
    private val viewModel by lazy { ViewModelProvider(this).get(ListViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        mainbinding = ActivityMainBinding.inflate(layoutInflater)
        binding = ActivityCourseBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter = ListAdapter(this)
        //if (mainbinding.ntest.text.equals("18")) {
        val recyclerView : RecyclerView = binding.recyclerview
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

            observerData()
        //}

        var num = 0
        adapter.setItemClickListener(object: ListAdapter.OnItemClickListener{
            override fun onClick(v: View, position: Int) {
                // 클릭 시 이벤트 작성
                mylist.add(
                    MyList(
                        adapter.userList[position].Course_NAME,
                        adapter.userList[position].division,
                        adapter.userList[position].Course_POINT,
                        adapter.userList[position].checked
                    )
                )
                makeText(binding.root.context,
                    "${adapter.userList[position].Course_NAME}가\n 저장 되었습니다.",
                    Toast.LENGTH_SHORT).show()

                binding.test.text = "과목명:" + mylist[num].Course_NAME
                num = num + 1
                //val mlist : String = Gson().toJson(mylist)

            }

            override fun onDelete(v: View, position: Int) {
                makeText(binding.root.context,
                    "${adapter.userList[position].Course_NAME}가\n 삭제 되었습니다.",
                    Toast.LENGTH_SHORT).show()
            }
        })
        binding.coursereg.setOnClickListener {
            val intent = Intent(this,UserAdapter::class.java)
            intent.putExtra("data",mylist)
        }
    }




    fun observerData(){
        viewModel.fetchData().observe(this, Observer {
            adapter.setListData(it)
            adapter.notifyDataSetChanged()
        })
    }
}





