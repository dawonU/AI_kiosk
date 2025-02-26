// 버튼 기능 연결
// 비디오 출력 확인

package com.example.test

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.ImageButton
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity

class OrderScreen : AppCompatActivity() {
    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.module_payment)

        videoView = findViewById(R.id.videoViewPayment)
        // 비디오 파일 경로
        val uri: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.intro)
        videoView.setVideoURI(uri)
        videoView.start() // 비디오 시작
    }
}