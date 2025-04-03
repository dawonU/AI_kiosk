package com.example.kiosk

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class BurgerChoice3 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.burgerchoice3)

        // BurgerChoice 에서 전달된 기본 데이터
        val menuName = intent.getStringExtra("menuName")
        val imgUrl = intent.getStringExtra("img_url")
        val ageGroup = intent.getStringExtra("age_group")

        // 기본 UI 요소 초기화
        findViewById<ImageView>(R.id.bc3_img_burgerSingle).also { imageView ->
            Glide.with(this).load(imgUrl).into(imageView)
        }
        findViewById<TextView>(R.id.txt_burger).text = menuName ?: ""
        findViewById<TextView>(R.id.bh3_nameTextView).text = menuName ?: ""

        // 전달 받은 추가 데이터
        val subSetPrice = intent.getIntExtra("subSetPrice", 0)
        val subLSetPrice = intent.getIntExtra("subLSetPrice", 0)
        val friesImgUrl = intent.getStringExtra("fries_img_url")
        val friesName = intent.getStringExtra("fries_name")
        val cokeImgUrl = intent.getStringExtra("coke_img_url")
        val cokeM_name = intent.getStringExtra("cokeM_name")
        val cokeL_name = intent.getStringExtra("cokeL_name")

        // UI 요소 초기화
        val priceText = if (subSetPrice != 0) "총 가격: $subSetPrice 원"
        else if (subLSetPrice != 0) "총 가격: $subLSetPrice 원"
        else "가격 없음"

        findViewById<TextView>(R.id.bh3_priceTextView).text = priceText

        findViewById<TextView>(R.id.txt_fries).text = friesName ?: ""
        findViewById<TextView>(R.id.txt_coke).text = cokeM_name ?: cokeL_name ?: ""

        findViewById<ImageView>(R.id.img_fries).also { imageView ->
            Glide.with(this).load(friesImgUrl).into(imageView)
        }
        findViewById<ImageView>(R.id.img_coke).also { imageView ->
            Glide.with(this).load(cokeImgUrl).into(imageView)
        }

        findViewById<Button>(R.id.addCart).setOnClickListener {
            Intent(this, MenuMain::class.java).apply {
                putExtra("menuName", menuName)
                putExtra("priceText", priceText)
                putExtra("subSetPrice", subSetPrice)
                putExtra("subLSetPrice", subLSetPrice)
                putExtra("age_group", ageGroup)  // age_group 전달
                addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
                startActivity(this)
            }
        }

        // 뒤로가기, 취소 버튼 클릭 이벤트
        findViewById<Button>(R.id.btn_bc3_back).setOnClickListener { finish() }
        findViewById<Button>(R.id.btn_bc3_cancle).setOnClickListener {
            Intent(this, MenuMain::class.java).apply {
                putExtra("age_group", ageGroup)  // age_group 전달
                startActivity(this)
            }
            finish()
        }
    }
}

