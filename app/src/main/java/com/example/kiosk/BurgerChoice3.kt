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

        // 기본 UI 요소 초기화 (BurgerChoice의 기본 이미지와 메뉴명)
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

        // UI 요소 초기화 (감자튀김, 콜라 관련)
        findViewById<TextView>(R.id.bh3_priceTextView).text =
            if (subSetPrice != 0) "총 가격: $subSetPrice 원"
            else if (subLSetPrice != 0) "총 가격: $subLSetPrice 원"
            else "가격 없음"

        findViewById<TextView>(R.id.txt_fries).text = friesName ?: ""
        findViewById<TextView>(R.id.txt_coke).text = cokeM_name ?: cokeL_name ?: ""

        findViewById<ImageView>(R.id.img_fries).also { imageView ->
            Glide.with(this).load(friesImgUrl).into(imageView)
        }
        findViewById<ImageView>(R.id.img_coke).also { imageView ->
            Glide.with(this).load(cokeImgUrl).into(imageView)
        }

        findViewById<Button>(R.id.addCart).setOnClickListener {
            startActivity(Intent(this, MenuMain::class.java))
        }

        // 뒤로가기, 취소 버튼 클릭 이벤트
        findViewById<Button>(R.id.btn_bc3_back).setOnClickListener { finish() }
        findViewById<Button>(R.id.btn_bc3_cancle).setOnClickListener {
            startActivity(Intent(this, MenuMain::class.java))
            finish()
        }
    }
}

