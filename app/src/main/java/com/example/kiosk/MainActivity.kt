//MainActivity
package com.example.kiosk

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = Intent(this, Intro::class.java) //intros.kt 실행
        startActivity(intent)
        finish() // MainActivity 종료
    }
}
