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


        // 메뉴 이름 및 가격 설정
        val intent = intent
        val nameTextView = findViewById<TextView>(R.id.bh3_nameTextView)
        val nameFries = findViewById<TextView>(R.id.txt_fries)
        val nameColeslaws = findViewById<TextView>(R.id.txt_coleslaw)

        val menuName = intent.getStringExtra("menuName")
        //
        val cardFries = findViewById<View>(R.id.card_fries)
        val imgFries = findViewById<ImageView>(R.id.img_fries)
        val txtFries = intent.getStringExtra("menuName")
        val img_url_Fries = intent.getStringExtra("img_url")
        //
        val cardColeslaws = findViewById<View>(R.id.card_coleslaw)
        val imgColeslaws = findViewById<ImageView>(R.id.img_coleslaw)
        val txtColeslaw = intent.getStringExtra("txt_coleslaw")
        val img_url_Coleslaws = intent.getStringExtra("img_url")


        nameTextView.text = menuName
        nameFries.text = "$txtFries"
        nameColeslaws.text = "$txtColeslaw"


        Glide.with(this)
            .load(img_url_Fries)
            .into(imgFries)

        Glide.with(this)
            .load(img_url_Coleslaws)
            .into(imgColeslaws)


        // cardFries 선택
//        cardFries.setOnClickListener {
//            val Intent = Intent(this, OptionalBurgerset4::class.java)
//            Intent.putExtra("menuName", menuName) // 메뉴 이름 전달
//            startActivity(Intent)
//        }
//
//        // cardColeslaws 선택
//        cardColeslaws.setOnClickListener {
//            val Intent = Intent(this, OptionalBurgerset4::class.java)
//            Intent.putExtra("menuName", menuName) // 메뉴 이름 전달
//            startActivity(Intent)
//        }

        // 이전 화면으로 가기
        val btnBurgerChoice = findViewById<Button>(R.id.btn_bc3_back)
        btnBurgerChoice.setOnClickListener {
            finish()
        }

        // 이전의 전 화면으로 가기 (예: MainActivity.class)
        val btnBurgerChoice2 = findViewById<Button>(R.id.btn_bc3_cancle)
        btnBurgerChoice2.setOnClickListener {
            val newIntent = Intent(this, MenuMain::class.java)
            startActivity(newIntent)
            finish()
        }
    }
}