package com.one.fruitmanseller.ui.main

import android.content.Intent
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.one.fruitmanseller.R
import com.one.fruitmanseller.ui.login.LoginActivity
import com.one.fruitmanseller.ui.main.timeline.TimelineFragment
import com.one.fruitmanseller.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object{
        var navStatus = -1
        const val CHANNEL_ID = "travelme-customer"
        private const val CHANNEL_NAME= "TravelMe"
        private const val CHANNEL_DESC = "Android FCM"
    }
    private var fragment : Fragment? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportActionBar?.hide()
        nav_view.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)
        if(savedInstanceState == null){ nav_view.selectedItemId = R.id.navigation_home }
        isLoggedIn()
    }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when(item.itemId){
            R.id.navigation_home -> {
                if(navStatus != 0){
                    fragment = TimelineFragment()
                    navStatus = 0
                }
            }
//            R.id.navigation_order -> {
//                if(navStatus != 1){
//                    fragment = OrderFragment()
//                    navStatus = 1
//                }
//            }
//
//            R.id.navigation_profile -> {
//                if(navStatus != 2){
//                    fragment = ProfileFragment()
//                    navStatus = 2
//                }
//            }
        }
        if(fragment == null){
            navStatus = 0
            fragment = TimelineFragment()
        }

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.screen_container, fragment!!)
        fragmentTransaction.commit()
        true
    }

    private fun isLoggedIn(){
        if(Constants.getToken(this@MainActivity).equals("UNDEFINED")){
            startActivity(Intent(this, LoginActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            }).also { finish() }
        }
    }
}
