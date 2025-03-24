package com.example.test

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.test.leftsideTab.TabBeverageAdapter
import com.example.test.leftsideTab.TabBurgerAdapter
import com.example.test.leftsideTab.TabCoffeeAdapter
import com.example.test.leftsideTab.TabDessertAdapter
import com.example.test.leftsideTab.TabSideAdapter
import com.example.test.leftsideTab.TabSpecialtyAdapter
import com.example.test.network.ApiService
import com.example.test.network.MenuResponse
import com.example.test.network.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuMain : AppCompatActivity() {

    private lateinit var rightLayout: LinearLayout
    private lateinit var recyclerView: RecyclerView

    private lateinit var tabBeverageAdapter: TabBeverageAdapter
    private lateinit var tabBurgerAdapter: TabBurgerAdapter
    private lateinit var tabCoffeeAdapter: TabCoffeeAdapter
    private lateinit var tabDessertAdapter: TabDessertAdapter
    private lateinit var tabSideAdapter: TabSideAdapter
    //private lateinit var tabSpecialtyAdapter: TabSpecialtyAdapter

    // 메뉴 데이터 리스트
    private lateinit var menuList: List<MenuResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_main)

        rightLayout = findViewById(R.id.rightLayout)

        setupSideMenuListeners()
        setupCardMenuListeners()

        // 메뉴 데이터 로드
        loadMenuData()
    }

    private fun setupSideMenuListeners() {
        val homeButton: Button = findViewById(R.id.sideMenu_홈)
        val specialtyButton: Button = findViewById(R.id.sideMenu_추천메뉴)
        val burgerButton: Button = findViewById(R.id.sideMenu_버거)
        val sideButton: Button = findViewById(R.id.sideMenu_사이드)
        val coffeeButton: Button = findViewById(R.id.sideMenu_커피)
        val dessertButton: Button = findViewById(R.id.sideMenu_디저트)
        val beverageButton: Button = findViewById(R.id.sideMenu_음료)

        homeButton.setOnClickListener {
            val intent = Intent(this, Intro::class.java)
            startActivity(intent)
        }

        specialtyButton.setOnClickListener { loadFragment("specialty") }
        burgerButton.setOnClickListener { loadFragment("burger") }
        sideButton.setOnClickListener { loadFragment("side") }
        coffeeButton.setOnClickListener { loadFragment("coffee") }
        dessertButton.setOnClickListener { loadFragment("dessert") }
        beverageButton.setOnClickListener { loadFragment("beverage") }
    }

    private fun setupCardMenuListeners() {
        val maclunchCard: CardView = findViewById(R.id.Card_maclunch)
        val happysnackCard: CardView = findViewById(R.id.Card_happysnack)
        val dessertcoffeeCard: CardView = findViewById(R.id.Card_dessertcoffee)
        val specialtyCard: CardView = findViewById(R.id.Card_specialty)

        maclunchCard.setOnClickListener { loadFragment("burger") }
        happysnackCard.setOnClickListener { loadFragment("happysnack") }
        dessertcoffeeCard.setOnClickListener { loadFragment("dessertcoffee") }
        specialtyCard.setOnClickListener { loadFragment("specialty") }
    }

    private fun loadFragment(category: String) {
        rightLayout.removeAllViews()
        val inflater = LayoutInflater.from(this)
        val newView = inflater.inflate(getLayoutIdByCategory(category), rightLayout, false)
        rightLayout.addView(newView)

        if (category in listOf("burger", "beverage", "coffee", "dessert", "side")) {
            setupRecyclerView(newView, category)
        }
    }

    private fun getLayoutIdByCategory(category: String): Int {
        return when (category) {
            "burger" -> R.layout.tab_burger
            "side" -> R.layout.tab_side
            "dessert" -> R.layout.tab_dessert
            "coffee" -> R.layout.tab_coffee
            "beverage" -> R.layout.tab_beverage
            "specialty" -> R.layout.tab_specialty
            else -> R.layout.tab_specialty // 기본값
        }
    }

    private fun setupRecyclerView(view: View, category: String) {
        recyclerView = view.findViewById(R.id.recyclerView)
        val gridLayoutManager = GridLayoutManager(this, 2)
        recyclerView.layoutManager = gridLayoutManager

        // RecyclerView에 Adapter 설정
        when (category) {
            "burger" -> {
                tabBurgerAdapter = TabBurgerAdapter(getFilteredMenuList(11))
                recyclerView.adapter = tabBurgerAdapter
            }
            "beverage" -> {
                tabBeverageAdapter = TabBeverageAdapter(getFilteredMenuList(14, 17))
                recyclerView.adapter = tabBeverageAdapter
            }
            "coffee" -> {
                tabCoffeeAdapter = TabCoffeeAdapter(getFilteredMenuList(17))
                recyclerView.adapter = tabCoffeeAdapter
            }
            "side" -> {
                tabSideAdapter = TabSideAdapter(getFilteredMenuList(13))
                recyclerView.adapter = tabSideAdapter
            }
            "dessert" -> {
                tabDessertAdapter = TabDessertAdapter(getFilteredMenuList(15))
                recyclerView.adapter = tabDessertAdapter
            }
        }
    }

    private fun getFilteredMenuList(vararg categoryIds: Int): List<MenuResponse> {
        return menuList.filter { menu -> categoryIds.contains(menu.category_id) }
    }

    private fun loadMenuData() {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)

        apiService.getMenus().enqueue(object : Callback<List<MenuResponse>> {
            override fun onResponse(call: Call<List<MenuResponse>>, response: Response<List<MenuResponse>>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        menuList = it
                        Log.d("MenuMain", "Menu list loaded: $menuList")
                    } ?: run {
                        Log.e("MenuMain", "Menu list is empty")
                    }
                } else {
                    Log.e("MenuMain", "Response error: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<List<MenuResponse>>, t: Throwable) {
                Log.e("MenuMain", "Network error: ${t.message}")
            }
        })
    }
}
