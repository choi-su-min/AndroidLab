package com.example.sharedpreference

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sharedpreference.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        var p = 3

        println("p: $p")

        val int = Plus().plus(3)

        println("int: $int")

        /*var testlist1:ArrayList<CoureType> = arrayListOf(
            CoureType(
                "과목1","영역1",3
            ),
            CoureType(
                "과목2","영역2",3
            ),
            CoureType(
                "과목3","영역3",3
            )
        )

        var testlist2:ArrayList<CoureType> = arrayListOf(
            CoureType(
                "과목3","영역3",3
            )
        )

        /*testlist1.add(CoureType("과목4","영역4",2))
        testlist1.remove(CoureType("과목2","영역2",3))
        testlist1.add(CoureType("과목7","영역7",2))



        for (i in 0 until testlist1.size){
            println("${testlist1[i].Course_NAME}")
        }*/

        if (testlist1.contains(
                CoureType(
                    testlist2[0].Course_NAME,
                    testlist2[0].division,
                    testlist2[0].Course_POINT
                )))
        {
            testlist1.remove(
                CoureType(
                    testlist2[0].Course_NAME,
                    testlist2[0].division,
                    testlist2[0].Course_POINT
                )
            )
            binding.test.text = testlist1.toString() + "list size : " + testlist1.size.toString()
        }*/


    }

}