package com.example.ch20_firebase

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Repo {
    fun getData(): LiveData<MutableList<User>> {
        val mutableData = MutableLiveData<MutableList<User>>()
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef = db.getReference("Course")
        myRef.addValueEventListener(object : ValueEventListener {
            val listData: MutableList<User> = mutableListOf<User>()
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    for (courseSnapshot in snapshot.children){
                        var getData = courseSnapshot.getValue(User::class.java)
                        listData.add(getData!!)

                        mutableData.value = listData
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return mutableData
    }
}