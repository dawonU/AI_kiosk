package com.example.kiosk;

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kiosk.leftsideTab.TabBeverageAdapter
import com.example.kiosk.leftsideTab.TabCoffeeAdapter
import com.example.kiosk.leftsideTab.TabDessertAdapter
import com.example.kiosk.leftsideTab.TabHappySnackAdapter
import com.example.kiosk.leftsideTab.TabSideAdapter
import com.example.kiosk.leftsideTab.TabSpecialtyAdapter
import com.example.kiosk.network.RetrofitClient
import com.example.kiosk.leftsideTab.TabBurgerAdapter
import com.example.kiosk.leftsideTab.TabDessertBeverageAdapter
import com.example.kiosk.network.ApiService
import com.example.kiosk.network.MenuResponse
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
    private lateinit var tabSpecialtyAdapter: TabSpecialtyAdapter
    private lateinit var tabDessertBeverageAdapter: TabDessertBeverageAdapter
    private lateinit var tabHappySnackAdapter: TabHappySnackAdapter

    // 메뉴 데이터 리스트
    private lateinit var menuList: List<MenuResponse>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_main)

        rightLayout = findViewById(R.id.rightLayout)
        val btnLogo: ImageButton = findViewById(R.id.btn_logo)

        setupSideMenuListeners()
        setupCardMenuListeners()
        loadMenuData()

        btnLogo.setOnClickListener {
            val intent = Intent(this, MenuMain::class.java)
            finish()
            startActivity(intent)
        }
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
        val dessertbeverageCard: CardView = findViewById(R.id.Card_dessertbeverage)
        val specialtyCard: CardView = findViewById(R.id.Card_specialty)

        maclunchCard.setOnClickListener { loadFragment("burger") }
        happysnackCard.setOnClickListener { loadFragment("happysnack") }
        dessertbeverageCard.setOnClickListener { loadFragment("dessertbeverage") }
        specialtyCard.setOnClickListener { loadFragment("specialty") }
    }

    private fun loadFragment(category: String) {
        rightLayout.removeAllViews()
        val inflater = LayoutInflater.from(this)
        val newView = inflater.inflate(getLayoutIdByCategory(category), rightLayout, false)
        rightLayout.addView(newView)

        if (category in listOf("tab_specialty", "burger", "side", "coffee", "dessert", "beverage",
                "dessertbeverage", "happysnack")) {
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
            "dessertbeverage" -> R.layout.tab_dessertbeverage
            "happysnack" -> R.layout.tab_happysnack
            else -> R.layout.tab_specialty // 기본값
        }
    }

    private fun setupRecyclerView(view: View, category: String) {
        recyclerView = view.findViewById(R.id.recyclerView)
        val gridLayoutManager = GridLayoutManager(this, 3)
        recyclerView.layoutManager = gridLayoutManager

        // RecyclerView에 Adapter 설정
        when (category) {
            /*"specialty" -> {
                tabSpecialtyAdapter = TabSpecialtyAdapter(getFilteredMenuList(13))
                recyclerView.adapter = tabSpecialtyAdapter
            }*/
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
            "dessertbeverage" -> {
                tabDessertBeverageAdapter = TabDessertBeverageAdapter(getFilteredMenuList(14,15,17))
                recyclerView.adapter = tabDessertBeverageAdapter
            }
            "happysnack" -> {
                tabHappySnackAdapter = TabHappySnackAdapter(getFilteredMenuList(18))
                recyclerView.adapter = tabHappySnackAdapter
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