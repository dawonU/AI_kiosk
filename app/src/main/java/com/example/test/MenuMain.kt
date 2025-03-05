package com.example.test

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView

class MenuMain : AppCompatActivity() {

    private lateinit var rightLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_main)

        rightLayout = findViewById(R.id.rightLayout)

        // 사이드 메뉴 버튼 클릭 리스너 설정
        setupSideMenuListeners()
        // 카드 클릭 리스너 설정
        setupCardMenuListeners()
    }

    private fun setupSideMenuListeners() {
        val homeButton: Button = findViewById(R.id.sideMenu_홈)
        val specialtyButton: Button = findViewById(R.id.sideMenu_추천메뉴)
        val burgerButton: Button = findViewById(R.id.sideMenu_버거)
        val sideButton: Button = findViewById(R.id.sideMenu_사이드)
        val coffeeButton: Button = findViewById(R.id.sideMenu_커피)
        val dessertButton: Button = findViewById(R.id.sideMenu_디저트)
        val beverageButton: Button = findViewById(R.id.sideMenu_음료)

        // 홈 버튼 클릭 시 Intro 액티비티로 이동
        homeButton.setOnClickListener {
            val intent = Intent(this, Intro::class.java) // Intro 액티비티로 이동
            startActivity(intent)
        }

        specialtyButton.setOnClickListener { loadFragment(R.layout.tab_specialty) }
        burgerButton.setOnClickListener { loadFragment(R.layout.tab_burger) }
        sideButton.setOnClickListener { loadFragment(R.layout.tab_side) }
        coffeeButton.setOnClickListener { loadFragment(R.layout.tab_coffee) }
        dessertButton.setOnClickListener { loadFragment(R.layout.tab_dessert) }
        beverageButton.setOnClickListener { loadFragment(R.layout.tab_beverage) }
    }




    private fun setupCardMenuListeners() {
        val maclunchCard: CardView = findViewById(R.id.Card_maclunch)
        val happysnackCard: CardView = findViewById(R.id.Card_happysnack)
        val dessertcoffeeCard: CardView = findViewById(R.id.Card_dessertcoffee)
        val specialtyCard: CardView = findViewById(R.id.Card_specialty)

        maclunchCard.setOnClickListener { loadFragment(R.layout.tab_burger) }
        happysnackCard.setOnClickListener { loadFragment(R.layout.tab_happysnack) }
        dessertcoffeeCard.setOnClickListener { loadFragment(R.layout.tab_dessertcoffee) }
        specialtyCard.setOnClickListener { loadFragment(R.layout.tab_specialty) }
    }

    private fun loadFragment(layoutId: Int) {
        rightLayout.removeAllViews()
        val inflater = LayoutInflater.from(this)
        val newView = inflater.inflate(layoutId, rightLayout, false)
        rightLayout.addView(newView)
    }
}
