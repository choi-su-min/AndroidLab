package com.example.develope

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
            "name" to "Ssu",
            "email" to "jcr@a.com",
            "avg" to 10
        )

        db.collection("users")
            .add(user)
            .addOnSuccessListener { documentReference ->
                Log.w("ssu", "added with ID: ${documentReference.id}")
            }
            .addOnFailureListener{e ->
                Log.w("g","error", e)
            }
    }
}