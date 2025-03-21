// BurgerChoice2.kt
package com.example.test

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BurgerChoice2 : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.burgerchoice2) // burgerchoice2.xml 레이아웃을 설정

        // 메뉴 이름 설정
        val nameTextView = findViewById<TextView>(R.id.bh2_nameTextView)
        val intent = intent
        val menuName = intent.getStringExtra("menuName") // 메뉴 이름을 Intent에서 가져옴
        nameTextView.text = menuName // TextView에 설정

        // 버튼 초기화
        val btnBurgerChoice = findViewById<Button>(R.id.btn_bc2_back)
        val btnBurgerChoice2 = findViewById<Button>(R.id.btn_bc2_cancle)

        // 이전 화면으로 가기
        btnBurgerChoice.setOnClickListener {
            finish()
        }

        // 이전의 전 화면으로 가기 (예: MainActivity.class)
        btnBurgerChoice2.setOnClickListener {
            val intent = Intent(this, MenuMain::class.java)
            startActivity(intent)
            finish() // 현재 액티비티 종료
        }
    }
}
