package com.example.kiosk

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.TextView
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Intro : AppCompatActivity() {
    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.intro)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.introLayout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        videoView = findViewById(R.id.intro_video)

        // Intent로 전달된 age_group 값 읽기
        val ageGroup = intent.getStringExtra("age_group")
        // age_group에 따라 재생할 비디오 파일 결정
        val videoResId = when (ageGroup) {
            "kid" -> R.raw.intro_kid
            "middle" -> R.raw.intro_middle
            "senior" -> R.raw.intro_senior
            else -> R.raw.wait  // 예외 상황에 대한 기본 비디오
        }

        // 비디오 파일 경로 설정
        val uri: Uri = Uri.parse("android.resource://$packageName/$videoResId")
        videoView.setVideoURI(uri)
        videoView.start() // 비디오 시작

        findViewById<CardView>(R.id.card_here).setOnClickListener {
            val intent = Intent(this, MenuMain::class.java)
            intent.putExtra("age_group", ageGroup)
            startActivity(intent)
        }

        findViewById<CardView>(R.id.card_takeout).setOnClickListener {
            val intent = Intent(this, MenuMain::class.java)
            intent.putExtra("age_group", ageGroup)
            startActivity(intent)
        }

        // 언어 변경 버튼 설정
        findViewById<ImageButton>(R.id.imgBtn_Kr).setOnClickListener {
            updateLanguage("Kr")
        }

        findViewById<ImageButton>(R.id.imgBtn_Eng).setOnClickListener {
            updateLanguage("Eng")
        }

        findViewById<ImageButton>(R.id.imgBtn_Jp).setOnClickListener {
            updateLanguage("Jp")
        }

        findViewById<ImageButton>(R.id.imgBtn_Ch).setOnClickListener {
            updateLanguage("Ch")
        }
    }

    override fun onResume() {
        super.onResume()
        // 비디오 다시 시작
        videoView.seekTo(1) // 비디오를 처음부터 재생하기 위해 seekTo(1) 사용
        videoView.start()
    }

    private fun updateLanguage(language: String) {
        findViewById<CardView>(R.id.card_here).setTag(language)
        findViewById<CardView>(R.id.card_takeout).setTag(language)
        updateText(language)
        updateSitePickText(language)
    }

    private fun updateText(language: String) {
        val textViewHere = findViewById<TextView>(R.id.txt_happysnack)
        val textViewTakeaway = findViewById<TextView>(R.id.txt_takeaway)

        when (language) {
            "Kr" -> {
                textViewHere.text = getString(R.string.매장)
                textViewTakeaway.text = getString(R.string.포장)
            }
            "Eng" -> {
                textViewHere.text = getString(R.string.for_here)
                textViewTakeaway.text = getString(R.string.take_out)
            }
            "Jp" -> {
                textViewHere.text = getString(R.string.ここで食べる)
                textViewTakeaway.text = getString(R.string.持ち帰り)
            }
            "Ch" -> {
                textViewHere.text = getString(R.string.在这里吃)
                textViewTakeaway.text = getString(R.string.外带)
            }
        }
    }

    private fun updateSitePickText(language: String) {
        val sitePickTextView = findViewById<TextView>(R.id.sitePick)

        when (language) {
            "Kr" -> sitePickTextView.text = getString(R.string.sitePick_kr)
            "Eng" -> sitePickTextView.text = getString(R.string.sitePick_eng)
            "Jp" -> sitePickTextView.text = getString(R.string.sitePick_jp)
            "Ch" -> sitePickTextView.text = getString(R.string.sitePick_ch)
        }
    }
}
