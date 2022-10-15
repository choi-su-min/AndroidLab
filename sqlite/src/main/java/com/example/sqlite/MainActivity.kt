package com.example.sqlite

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import java.io.BufferedReader
import java.io.File
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val file = File(filesDir, "mydb.db")
        val writeStream: OutputStreamWriter = file.writer()
        writeStream.write("hello world")
        writeStream.flush()

        val readStream: BufferedReader = file.reader().buffered()
        readStream.forEachLine {
            Log.d("Ssuu","$it")
        }
    }
}