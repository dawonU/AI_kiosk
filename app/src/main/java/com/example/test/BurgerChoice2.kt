package com.example.test

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.test.network.ApiService
import com.example.test.network.SubMenuResponse

class BurgerChoice2 : AppCompatActivity() {
    private lateinit var apiService: ApiService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.burgerchoice2)

        // 메뉴 이름 설정
        val nameTextView = findViewById<TextView>(R.id.bh2_nameTextView)
        val intent = intent
        val menuName = intent.getStringExtra("menuName") // 메뉴 이름을 Intent에서 가져옴
        nameTextView.text = menuName // TextView에 설정


        // 버튼 초기화
        val btnBurgerChoice = findViewById<Button>(R.id.btn_bc2_back)
        val btnBurgerChoice2 = findViewById<Button>(R.id.btn_bc2_cancle)

        // 카드 초기화
        val cardBurgerSet = findViewById<CardView>(R.id.card_burgerset)
        val cardBurgerLargeSet = findViewById<CardView>(R.id.card_burgerlargeset)

        // 가격 표시할 TextView 초기화
        val priceTextViewSet = findViewById<TextView>(R.id.bc2_burgerset2)
        val priceTextViewLargeSet = findViewById<TextView>(R.id.bc2_largeburgerset2)

        // card_burgerset 클릭 시
        cardBurgerSet.setOnClickListener {
            val intent = Intent(this, BurgerChoice3::class.java)
            intent.putExtra("friesSize", "후렌치후라이-미디엄") // 데이터 전송
            intent.putExtra("menuName", menuName)
            intent.putExtra("coleslawValue", "+500원")
            startActivity(intent)
        }

        // card_burgerlargeset 클릭 시
        cardBurgerLargeSet.setOnClickListener {
            val intent = Intent(this, BurgerChoice3::class.java)
            intent.putExtra("friesSize", "후렌치후라이-라지") // 데이터 전송
            intent.putExtra("menuName", menuName)
            intent.putExtra("coleslawValue", "")
            startActivity(intent)
        }

        // 이전 화면으로 가기
        btnBurgerChoice.setOnClickListener {
            finish()
        }

        // 이전의 전 화면으로 가기 (예: MainActivity.class)
        btnBurgerChoice2.setOnClickListener {
            val intent = Intent(this, MenuMain::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun fetchMenuPrices(menuName: String?, priceTextViewSet: TextView, priceTextViewLargeSet: TextView) {
        apiService.fetchMenus { prices ->
            if (prices != null) {
                // prices를 SubMenuResponse 리스트로 처리
                val setPrice = prices.filterIsInstance<SubMenuResponse>().firstOrNull { it.variationName == "세트" }
                val largeSetPrice = prices.filterIsInstance<SubMenuResponse>().firstOrNull { it.variationName == "라지세트" }

                // 가격 설정
                priceTextViewSet.text = (setPrice?.price?.toString() + " 원") ?: "가격 없음"
                priceTextViewLargeSet.text = (largeSetPrice?.price?.toString() + " 원") ?: "가격 없음"
            } else {
                priceTextViewSet.text = "가격 없음"
                priceTextViewLargeSet.text = "가격 없음"
            }
        }
    }
}

