// BurgerChoice2.java
package com.example.kiosk

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.example.kiosk.network.ApiService


class BurgerChoice2 : AppCompatActivity() {
    private lateinit var apiService: ApiService  // apiService 사용 코드가 없으면 제거 고려

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.burgerchoice2)

        // 메뉴 이름 초기화 및 설정
        val menuName = intent.getStringExtra("menuName")
        findViewById<TextView>(R.id.bh2_nameTextView).text = menuName
        val ageGroup = intent.getStringExtra("age_group")

        // 이미지 초기화
        val img_url = intent.getStringExtra("img_url")
        val imgUrlBurgerSet = intent.getStringExtra("img_url_burgerSet")
        findViewById<ImageView>(R.id.img_burgerset).also { imageView ->
            Glide.with(this).load(imgUrlBurgerSet).into(imageView)
        }
        findViewById<ImageView>(R.id.img_largeburgerset).also { imageView ->
            Glide.with(this).load(imgUrlBurgerSet).into(imageView)
        }

        // 가격 데이터 intent로부터 받아오기
        val subSetPrice = intent.getIntExtra("sub_Set_price", 0)
        val subLSetPrice = intent.getIntExtra("sub_LSet_price", 0)
        findViewById<TextView>(R.id.bc2_burgerset2).text =
            if (subSetPrice != 0) "$subSetPrice 원" else "가격 없음"
        findViewById<TextView>(R.id.bc2_largeburgerset2).text =
            if (subLSetPrice != 0) "$subLSetPrice 원" else "가격 없음"

        // 세트 카테고리명
        val sub_Set_cat_name = intent.getStringExtra("sub_Set_cat_name")
        val sub_LSet_cat_name = intent.getStringExtra("sub_LSet_cat_name")

        // 버튼, 카드 초기화 및 이벤트 처리
        findViewById<CardView>(R.id.card_burgerset).setOnClickListener {
            Intent(this, BurgerChoice3::class.java).apply {
                putExtra("menuName", menuName)
                putExtra("img_url", img_url)
                putExtra("sub_Set_cat_name", sub_Set_cat_name)
                putExtra("subSetPrice", subSetPrice)
                putExtra(
                    "fries_img_url",
                    "https://www.mcdelivery.co.kr/kr//static/1738737640738/assets/82/products/1402.png"
                )
                putExtra("fries_name", "후렌치 후라이 (미디엄)")
                    putExtra(
                    "coke_img_url",
                    "https://www.mcdelivery.co.kr/kr//static/1738737640738/assets/82/products/1506.png?"
                )
                putExtra("cokeM_name", "코카콜라 (미디엄)")
                putExtra("age_group", ageGroup)
                startActivity(this)
            }
        }

        findViewById<CardView>(R.id.card_burgerlargeset).setOnClickListener {
            Intent(this, BurgerChoice3::class.java).apply {
                putExtra("menuName", menuName)
                putExtra("img_url", img_url)
                putExtra("sub_LSet_cat_name", sub_LSet_cat_name)
                putExtra("subLSetPrice", subLSetPrice)
                putExtra(
                    "fries_img_url",
                    "https://www.mcdelivery.co.kr/kr//static/1738737640738/assets/82/products/1403.png"
                )
                putExtra("fries_name", "후렌치 후라이 (라지)")
                putExtra(
                    "coke_img_url",
                    "https://www.mcdelivery.co.kr/kr//static/1738737640738/assets/82/products/1506.png?"
                )
                putExtra("cokeL_name", "코카콜라 (라지)")
                putExtra("age_group", ageGroup)
                startActivity(this)
            }
        }

        findViewById<Button>(R.id.btn_bc2_back).setOnClickListener { finish() }
        findViewById<Button>(R.id.btn_bc2_cancle).setOnClickListener {
            startActivity(Intent(this, MenuMain::class.java).apply {
                putExtra("age_group", ageGroup)
            })
            finish()
        }
    }
}


