package com.example.androidlab

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.FirebaseFirestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var db: FirebaseFirestore = FirebaseFirestore.getInstance()

        val user = mapOf(
            "course" to "비행역학",
            "Cnum" to "321445",
            "name" to "김병수"
        )

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.d("Success", "${documentReference.id}")
            }
            .addOnFailureListener{e ->
                Log.w("Failure", e)
            }
    }
}