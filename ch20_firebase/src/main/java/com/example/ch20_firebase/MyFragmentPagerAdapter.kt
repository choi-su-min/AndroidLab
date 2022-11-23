package com.example.ch20_firebase

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class MyFragmentPagerAdapter(activity: FragmentActivity, year: String) :FragmentStateAdapter(activity){
    var fFragments = listOf<Fragment>()
    init {
        when(year){
            "2017" -> {
                fFragments = listOf(
                    OneFragment(),
                    TwoFragment(),
                    ThreeFragment(),
                    FourFragment(),
                    FiveFragment()
                )
            }
            "2018" -> {
                fFragments = listOf(
                    OneFragment(),
                    TwoFragment(),
                    ThreeFragment(),
                    FourFragment(),
                    FiveFragment()
                )
            }
            "2019" -> {
                fFragments = listOf(
                    OneFragment2(),
                    TwoFragment(),
                    ThreeFragment(),
                    FourFragment(),
                    FiveFragment()
                )
            }
            "2020" -> {
                fFragments = listOf(
                    OneFragment3(),
                    TwoFragment(),
                    ThreeFragment(),
                    FourFragment(),
                    FiveFragment()
                )
            }
            "2021" -> {
                fFragments = listOf(
                    OneFragment4(),
                    TwoFragment(),
                    ThreeFragment(),
                    FourFragment(),
                    FiveFragment()
                )
            }
            "2022" -> {
                fFragments = listOf(
                    OneFragment4(),
                    TwoFragment(),
                    ThreeFragment(),
                    FourFragment(),
                    FiveFragment()
                )
            }
            else -> {
                fFragments = listOf(
                    OneFragment(),
                    TwoFragment(),
                    ThreeFragment(),
                    FourFragment(),
                    FiveFragment()
                )
            }
        }
    }

    override fun getItemCount(): Int = fFragments.size
    override fun createFragment(position: Int): Fragment = fFragments[position]
}