package com.example.gaja

import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.gaja.ui.SearchFragment
import com.example.gaja.ui.bookmark
import com.example.gaja.ui.location
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var isFabOpen = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        bottomNavi.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.item_bookmarkfragment -> {
                    replaceFragment(bookmark())
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.item_mapfragment -> {
                    replaceFragment(location())
                    return@setOnNavigationItemSelectedListener true
                }
                else -> {
                    return@setOnNavigationItemSelectedListener false
                }
            }
        }
        fabMain.setOnClickListener { toggleFab() }

        //모달다이어로그 구현해야함
        fab_search_menu.setOnClickListener {
            val intent = Intent(this, SearchFragment::class.java)
            startActivity(intent)
        }
    }

    private fun toggleFab() {
        Toast.makeText(this, "메인플로팅 테스트 : $isFabOpen", Toast.LENGTH_SHORT).show()
        if(isFabOpen){
            ObjectAnimator.ofFloat(fab_search_menu, "translationY", 0f).apply { start() }
            fabMain.setImageResource(R.drawable.ic_baseline_add_circle_24)
        }
        else {
            ObjectAnimator.ofFloat(fab_search_menu, "translationY", -200f).apply { start() }
            fabMain.setImageResource(R.drawable.ic_baseline_clear_24)
        }
        isFabOpen = !isFabOpen
    }
    private fun replaceFragment(fragment: Fragment) {
        val fragmentTransaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.main_frame, fragment)
        fragmentTransaction.commit()
    }
}