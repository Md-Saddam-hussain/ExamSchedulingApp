package com.amier.modernloginregister

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.amier.modernloginregister.fragment.ExamFragment
import com.amier.modernloginregister.fragment.ProfileFragment
import kotlinx.android.synthetic.main.activity_main2.*

class MainActivity2 : AppCompatActivity() {

    private val profileFragment = ProfileFragment()
    private val examFragment = ExamFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        replaceFragment(profileFragment)
        bottom_navigation.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.nav_profile -> replaceFragment(profileFragment)
                R.id.nav_exam -> replaceFragment(examFragment)
            }
            true
        }

    }

    private fun replaceFragment(fragment : Fragment) {
        if(fragment!= null){
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.fragment_container,fragment)
            transaction.commit()
        }
    }

}