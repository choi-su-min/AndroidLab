package com.example.ch20_firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

open class Repo() {
    fun getData(usernum:String): ArrayList<CourseListType> {
        val majorlist = ArrayList<CourseListType>()
        val db: FirebaseDatabase = FirebaseDatabase.getInstance()
        val myRef = db.reference.child(usernum).child("전공")
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                    for (userSnapshot in snapshot.children){
                        val getData = userSnapshot.getValue(CourseListType::class.java)
                        majorlist.add(getData!!)
                    }
            }
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
        return majorlist
    }
}
