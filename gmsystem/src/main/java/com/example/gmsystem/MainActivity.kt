package com.example.gmsystem

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.gms.*
import com.example.gmsystem.databinding.ActivityMainBinding
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    private var isFabOpen = false
    lateinit var mainFragment : Fragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        toggle = ActionBarDrawerToggle(
            this, binding.drawer,
            R.string.drawer_opened, R.string.drawer_closed
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        val year = intent.getStringExtra("year").toString()

        mainFragment = when(year){
            "2017" -> OneFragment()
            "2018" -> OneFragment()
            "2019" -> OneFragment2()
            "2020" -> OneFragment3()
            "2021" -> OneFragment4()
            else -> OneFragment()
        }

        binding.tabs.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab?) {
                val transaction = supportFragmentManager.beginTransaction()
                when (tab?.text) {
                    "졸업 소요 학점" -> transaction.replace(R.id.tabContent, mainFragment)
                    "전공 세부 사항" -> transaction.replace(R.id.tabContent, TwoFragment())
                    "교양 세부 사항" -> transaction.replace(R.id.tabContent, ThreeFragment())
                    "추천 과목" -> transaction.replace(R.id.tabContent, FourFragment())
                    "졸업 요건" -> transaction.replace(R.id.tabContent, FiveFragment())
                }
            }
            override fun onTabReselected(tab: TabLayout.Tab?) {
            }
            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }
        })

        val adapter = when(year){
            "2017" -> MyFragmentPagerAdapter1(this)
            "2018" -> MyFragmentPagerAdapter1(this)
            "2019" -> MyFragmentPagerAdapter2(this)
            "2020" -> MyFragmentPagerAdapter3(this)
            "2021" -> MyFragmentPagerAdapter4(this)
            else -> MyFragmentPagerAdapter1(this)
        }
        binding.viewpager.adapter = adapter

        TabLayoutMediator(binding.tabs, binding.viewpager) { tab, position ->
            when (position) {
                0 -> tab.text = "졸업 소요 학점"
                1 -> tab.text = "전공 세부 사항"
                2 -> tab.text = "교양 세부 사항"
                3 -> tab.text = "추천 과목"
                4 -> tab.text = "졸업 요건"
            }
        }.attach()

        val intent1: Intent = Intent(this, MajorEnrollActivity::class.java)
        val intent2: Intent = Intent(this, LiberalArtsEnrollActivity::class.java)

        binding.fabMain.setOnClickListener {
            toggleFab()
        }
        binding.fabLiberalArts.setOnClickListener {
            startActivity(intent2)
        }
        binding.fabMajor.setOnClickListener {
            startActivity(intent1)
        }
    }

    private fun toggleFab() {
        val fabLiberalArts : View = findViewById(R.id.fabLiberalArts)
        val fabMajor : View = findViewById(R.id.fabMajor)

        if (isFabOpen) {
            ObjectAnimator.ofFloat(
                fabLiberalArts, "translationY", 0f).apply { start() }
            ObjectAnimator.ofFloat(
                fabMajor, "translationY", 0f).apply { start() }
        } else {
            ObjectAnimator.ofFloat(
                fabLiberalArts, "translationY", -160f).apply { start() }
            ObjectAnimator.ofFloat(
                fabMajor, "translationY", -320f).apply { start() }
        }
        isFabOpen = !isFabOpen
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    class MyFragmentPagerAdapter1(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        val fragments: List<Fragment>
        init {
            fragments = listOf(
                OneFragment(),
                TwoFragment(),
                ThreeFragment(),
                FourFragment(),
                FiveFragment()
            )
        }
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]
    }
    class MyFragmentPagerAdapter2(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        val fragments: List<Fragment>
        init {
            fragments = listOf(
                OneFragment2(),
                TwoFragment(),
                ThreeFragment(),
                FourFragment(),
                FiveFragment()
            )
        }
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]
    }
    class MyFragmentPagerAdapter3(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        val fragments: List<Fragment>
        init {
            fragments = listOf(
                OneFragment3(),
                TwoFragment(),
                ThreeFragment(),
                FourFragment(),
                FiveFragment()
            )
        }
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]
    }
    class MyFragmentPagerAdapter4(activity: FragmentActivity) : FragmentStateAdapter(activity) {
        val fragments: List<Fragment>
        init {
            fragments = listOf(
                OneFragment4(),
                TwoFragment(),
                ThreeFragment(),
                FourFragment(),
                FiveFragment()
            )
        }
        override fun getItemCount(): Int = fragments.size
        override fun createFragment(position: Int): Fragment = fragments[position]
    }
}
