package com.example.test

import Model
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageButton
import android.widget.VideoView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class Start : AppCompatActivity() {
    private lateinit var videoView: VideoView
    private val MODEL_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.startorder)

        // 앱 실행 시 바로 Model.kt 실행
        val intent = Intent(this, Model::class.java)
        startActivityForResult(intent, MODEL_REQUEST_CODE)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        videoView = findViewById(R.id.videoView)

        // 비디오 파일 경로 설정
        val uri: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.intro)
        videoView.setVideoURI(uri)
        videoView.start() // 비디오 시작

        // 매장, 포장 버튼 설정
        findViewById<Button>(R.id.button_rounded1).setOnClickListener {
            val intent = Intent(this, DivMenuActivity::class.java) // DivMenuActivity로 이동
            startActivity(intent)
        }

        findViewById<Button>(R.id.button_rounded2).setOnClickListener {
            val intent = Intent(this, DivMenuActivity::class.java) // DivMenuActivity로 이동
            startActivity(intent)
        }



        // 언어 변경 버튼 설정
        findViewById<ImageButton>(R.id.imgBtn_Kr).setOnClickListener {
            findViewById<Button>(R.id.button_rounded1).text = "먹고가기"
            findViewById<Button>(R.id.button_rounded2).text = "포장하기"
        }

        findViewById<ImageButton>(R.id.imgBtn_Eng).setOnClickListener {
            findViewById<Button>(R.id.button_rounded1).text = "For Here"
            findViewById<Button>(R.id.button_rounded2).text = "To Go"
        }

        findViewById<ImageButton>(R.id.imgBtn_Jp).setOnClickListener {
            findViewById<Button>(R.id.button_rounded1).text = "店内で食べる"
            findViewById<Button>(R.id.button_rounded2).text = "持ち帰り"
        }

        findViewById<ImageButton>(R.id.imgBtn_Ch).setOnClickListener {
            findViewById<Button>(R.id.button_rounded1).text = "在这里用餐"
            findViewById<Button>(R.id.button_rounded2).text = "打包"
        }
    }

    // Model.kt에서 연령대 결과를 받아서 UI 업데이트
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (requestCode == MODEL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
//            val ageGroup = data?.getStringExtra("age_group") ?: "unknown"
//            Toast.makeText(this, "분석 결과: $ageGroup", Toast.LENGTH_LONG).show()
//        }
//    }
}
