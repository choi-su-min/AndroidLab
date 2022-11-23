package com.example.ch20_firebase

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ch20_firebase.databinding.FragmentTwoBinding
import com.google.firebase.database.FirebaseDatabase

class TwoFragment : Fragment() {

    //전공 프래그먼트에 사용자가 등록한 전공과목 뿌려주기.
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentTwoBinding.inflate(inflater, container, false)
        var adapter:SuggestListAdapter2?
        var studentMajorList: ArrayList<CourseListType> = arrayListOf()
        val realtimeDB: FirebaseDatabase = FirebaseDatabase.getInstance()

        val usernum = arguments?.getString("usernum")

        val majorRef = realtimeDB.reference.child(usernum.toString()).child("전공")
        majorRef.get().addOnSuccessListener {
            for (courseSnapshot in it.children){
                var getData = courseSnapshot.getValue(CourseListType::class.java)
                studentMajorList.add(getData!!)
            }
            val recyclerView : RecyclerView = binding.majorList
            adapter = SuggestListAdapter2(studentMajorList)
            recyclerView.layoutManager = LinearLayoutManager(context)
            recyclerView.adapter = adapter
        }

        return binding.root
    }
}