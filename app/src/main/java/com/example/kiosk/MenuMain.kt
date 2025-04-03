package com.example.kiosk

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kiosk.leftsideTab.TabBeverageAdapter
import com.example.kiosk.leftsideTab.TabBurgerAdapter
import com.example.kiosk.leftsideTab.TabCoffeeAdapter
import com.example.kiosk.leftsideTab.TabDessertAdapter
import com.example.kiosk.leftsideTab.TabHappySnackAdapter
import com.example.kiosk.leftsideTab.TabSideAdapter
import com.example.kiosk.leftsideTab.TabSpecialtyAdapter
import com.example.kiosk.leftsideTab.TabDessertBeverageAdapter
import com.example.kiosk.network.MenuResponse
import com.example.kiosk.network.RetrofitClient
import com.example.kiosk.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuMain : AppCompatActivity() {
    private var ageGroup: String = ""
    private lateinit var timerHelper: TimerHelper

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

    // 주문 내역(장바구니) 관련 변수
    private lateinit var orderRecyclerView: RecyclerView
    private lateinit var addCartAdapter: AddCart
    private val cartItemList = ArrayList<AddCart.CartItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_main)

        ageGroup = intent.getStringExtra("age_group") ?: ""

        val orderTimerTextView = findViewById<TextView>(R.id.order_timer)
        timerHelper = TimerHelper(this, orderTimerTextView, ageGroup)
        timerHelper.setOnTimerFinishListener(object : TimerHelper.OnTimerFinishListener {
            override fun onTimerFinish() {// 타이머=0 일때
                cartItemList.clear() // -> 장바구니 초기화
                addCartAdapter.notifyDataSetChanged()
                updateTotalPrice()  // 총 금액 초기화
            }
        })
        timerHelper.startTimer()

        val btnOrderPay = findViewById<Button>(R.id.btn_order_pay)
        btnOrderPay.setOnClickListener {
            // pay.xml 팝업을 새로 inflate
            val dialogView = layoutInflater.inflate(R.layout.pay, null)
            val dialog = AlertDialog.Builder(this)
                .setView(dialogView)
                .create()

            // pay.xml 내의 recycler_orderList 설정
            val recyclerOrderList = dialogView.findViewById<RecyclerView>(R.id.recycler_orderList)
            recyclerOrderList.layoutManager = LinearLayoutManager(this)
            val payAdapter = PayAdapter(cartItemList) // cartItemList 기반 데이터 바인딩
            recyclerOrderList.adapter = payAdapter

            // 총 금액 계산 및 표시
            val txtPayTotalPrice2 = dialogView.findViewById<TextView>(R.id.txt_pay_totalPrice2)
            val totalPrice = cartItemList.sumOf { item ->
                val price = if (item.getSubSetPrice() != 0) item.getSubSetPrice() else item.getSubLSetPrice()
                price * item.getQuantity()
            }
            txtPayTotalPrice2.text = "$totalPrice 원"

            // pay.xml 내의 btn_pay_back : 클릭 시 팝업 닫기
            val btnPayBack = dialogView.findViewById<Button>(R.id.btn_pay_back)
            btnPayBack.setOnClickListener {
                dialog.dismiss()
            }

            // pay.xml 내의 btn_pay_next : 클릭 시 팝업 닫고 pay_end.xml 팝업 띄우기
            val btnPayNext = dialogView.findViewById<Button>(R.id.btn_pay_next)
            btnPayNext.setOnClickListener {
                dialog.dismiss() // 현재 팝업 닫기

                // pay_end.xml 팝업 인플레이트
                val payEndDialogView = layoutInflater.inflate(R.layout.pay_end, null)
                val payEndDialog = AlertDialog.Builder(this)
                    .setView(payEndDialogView)
                    .create()

                // pay_end.xml 내의 btn_payend 클릭 시 Intro 액티비티로 이동
                val btnPayEnd = payEndDialogView.findViewById<Button>(R.id.btn_payend)
                btnPayEnd.setOnClickListener {
                    payEndDialog.dismiss()
                    val intent = Intent(this, Intro::class.java)
                    startActivity(intent)
                }
                payEndDialog.show()
            }
            dialog.show()
        }


        rightLayout = findViewById(R.id.rightLayout)
        setupSideMenuListeners()
        setupCardMenuListeners()
        loadMenuData()

        // 주문 내역(장바구니) RecyclerView 초기화
        orderRecyclerView = findViewById(R.id.orderlist_recyclerView)
        orderRecyclerView.layoutManager = LinearLayoutManager(this)
        addCartAdapter = AddCart(this, cartItemList)
        addCartAdapter.setOnCartItemChangeListener(object : AddCart.OnCartItemChangeListener {
            override fun onCartItemChanged() {
                updateTotalPrice()
            }
        })
        orderRecyclerView.adapter = addCartAdapter

        // "전체 삭제" 버튼 처리 (order_pay.xml에 포함된 버튼)
        val clearCartButton = findViewById<Button>(R.id.button2)
        clearCartButton.setOnClickListener {
            val intent = Intent(this, MenuMain::class.java).apply {
                putExtra("clearCart", true)
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP)
            }
            startActivity(intent)
        }

        processNewIntent(intent)
        updateTotalPrice()
    }


    // 새로운 intent를 받으면 기존 주문 내역에 추가
    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        processNewIntent(intent)
        updateTotalPrice()
    }

    // 예를 들어 onActivityResult로 결과를 받아 처리하는 경우 (사용 시)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 결과 처리 후 updateTotalPrice() 호출
        updateTotalPrice()
    }

    private fun processNewIntent(intent: Intent) {
        if (intent.getBooleanExtra("clearCart", false)) {
            cartItemList.clear()
            addCartAdapter.notifyDataSetChanged()
            updateTotalPrice()
            return
        }
        // 기존 주문 데이터 처리
        val menuNameExtra = intent.getStringExtra("menuName")
        val priceExtra = intent.getIntExtra("price", 0)
        val subSetPrice = intent.getIntExtra("subSetPrice", 0)
        val subLSetPrice = intent.getIntExtra("subLSetPrice", 0)

        if (menuNameExtra != null) {
            val newItem = if (priceExtra != 0) {
                AddCart.CartItem(menuNameExtra, priceExtra, 0)
            } else {
                AddCart.CartItem(menuNameExtra, subSetPrice, subLSetPrice)
            }
            cartItemList.add(newItem)
            addCartAdapter.notifyItemInserted(cartItemList.size - 1)
        }
    }

    // 총 가격을 계산하여 txt_order_totalPrice2에 표시
    private fun updateTotalPrice() {
        val totalPrice = cartItemList.sumOf { item ->
            val price = if (item.getSubSetPrice() != 0) item.getSubSetPrice() else item.getSubLSetPrice()
            price * item.getQuantity()
        }
        val totalPriceTextView = findViewById<TextView>(R.id.txt_order_totalPrice2)
        totalPriceTextView.text = "$totalPrice 원"
    }

    override fun onResume() {
        super.onResume()
        timerHelper.startTimer()
    }

    override fun onPause() {
        super.onPause()
        timerHelper.stopTimer()
    }

    override fun onDestroy() {
        super.onDestroy()
        timerHelper.stopTimer()
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

        if (category in listOf(
                "tab_specialty", "burger", "side", "coffee", "dessert", "beverage",
                "dessertbeverage", "happysnack"
            )
        ) {
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
            else -> R.layout.tab_specialty
        }
    }

    private fun setupRecyclerView(view: View, category: String) {
        recyclerView = view.findViewById(R.id.recyclerView)
        val spanCount = if (ageGroup == "senior") 2 else 3
        val gridLayoutManager = GridLayoutManager(this, spanCount)
        recyclerView.layoutManager = gridLayoutManager

        when (category) {
            "burger" -> {
                tabBurgerAdapter = TabBurgerAdapter(getFilteredMenuList(11), ageGroup)
                recyclerView.adapter = tabBurgerAdapter
            }
            "beverage" -> {
                tabBeverageAdapter = TabBeverageAdapter(getFilteredMenuList(14, 17), ageGroup)
                recyclerView.adapter = tabBeverageAdapter
            }
            "coffee" -> {
                tabCoffeeAdapter = TabCoffeeAdapter(getFilteredMenuList(17), ageGroup)
                recyclerView.adapter = tabCoffeeAdapter
            }
            "side" -> {
                tabSideAdapter = TabSideAdapter(getFilteredMenuList(13), ageGroup)
                recyclerView.adapter = tabSideAdapter
            }
            "dessert" -> {
                tabDessertAdapter = TabDessertAdapter(getFilteredMenuList(15), ageGroup)
                recyclerView.adapter = tabDessertAdapter
            }
            "dessertbeverage" -> {
                tabDessertBeverageAdapter = TabDessertBeverageAdapter(getFilteredMenuList(14, 15, 17), ageGroup)
                recyclerView.adapter = tabDessertBeverageAdapter
            }
            "happysnack" -> {
                tabHappySnackAdapter = TabHappySnackAdapter(getFilteredMenuList(18), ageGroup)
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

// PayAdapter
class PayAdapter(private val cartList: ArrayList<AddCart.CartItem>) :
    RecyclerView.Adapter<PayAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val menuName: TextView = itemView.findViewById(R.id.pay_item_menuName)
        val menuNum: TextView = itemView.findViewById(R.id.pay_item_memuNum)
        val menuPrice: TextView = itemView.findViewById(R.id.pay_item_menuPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.pay_detail_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = cartList[position]
        holder.menuName.text = item.getMenuName()
        holder.menuNum.text = item.getQuantity().toString()
        val price = if (item.getSubSetPrice() != 0) item.getSubSetPrice() else item.getSubLSetPrice()
        holder.menuPrice.text = "${price}원"
    }

    override fun getItemCount(): Int = cartList.size
}
