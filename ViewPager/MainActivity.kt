package com.example.viewpager

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val adapter = MyViewPagerAdapter(supportFragmentManager)
        adapter.addFragment(FragmentOne(),"One")
        adapter.addFragment(FragmentTwo(),"Two")
        adapter.addFragment(FragmentThree(),"Three")
        viewPager.adapter = adapter
        tabs.setupWithViewPager(viewPager)

    }

    class MyViewPagerAdapter(manager: FragmentManager): FragmentPagerAdapter(manager){

        private val fragmentList : MutableList<Fragment> = ArrayList()
        private val titleList : MutableList<String> = ArrayList()

        override fun getItem(position: Int): Fragment {
            return fragmentList[position]
        }

        override fun getCount(): Int {
            return fragmentList.size
        }

        fun addFragment(fragment:Fragment, title:String){
            fragmentList.add(fragment)
            titleList.add(title)
        }
        override fun getPageTitle(position: Int):CharSequence?{
            return titleList[position]
        }
    }
}
