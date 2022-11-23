package com.example.ch20_firebase

import androidx.fragment.app.Fragment

class MainFragment(year: String) : Fragment() {
    val mainFragment = when(year){
        "2017" -> OneFragment()
        "2018" -> OneFragment()
        "2019" -> OneFragment2()
        "2020" -> OneFragment3()
        "2021" -> OneFragment4()
        "2022" -> OneFragment4()
        else -> OneFragment()
    }

    fun selectTCV(): Fragment{
        return mainFragment
    }
}