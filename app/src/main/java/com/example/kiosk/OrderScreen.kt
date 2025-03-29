// 버튼 기능 연결
// 비디오 출력 확인

package com.example.kiosk

import android.net.Uri
import android.os.Bundle
import android.widget.VideoView
import androidx.appcompat.app.AppCompatActivity
import android.app.AlertDialog
import android.widget.Button
import android.widget.Toast

class OrderScreen : AppCompatActivity() {
    private lateinit var videoView: VideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.menu_payment)

        videoView = findViewById(R.id.videoViewPayment)
        // 비디오 파일 경로
        val uri: Uri = Uri.parse("android.resource://" + packageName + "/" + R.raw.intro)
        videoView.setVideoURI(uri)
        videoView.start() // 비디오 시작

        // 결제하기 버튼 선택시
        class MainActivity : AppCompatActivity() {
            override fun onCreate(savedInstanceState: Bundle?) {
                super.onCreate(savedInstanceState)
                setContentView(R.layout.menu_payment)
                // 버튼 찾기
                val btnPayment = findViewById<Button>(R.id.btn_payment)

                // 버튼 클릭 시 AlertDialog
                btnPayment.setOnClickListener {
                    showPaymentDialog()
                }
            }

            // 결제창 내용
            private fun showPaymentDialog() {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("잠깐만요")
                builder.setMessage("세트로 구매시 ~ 원 더 저렴합니다")  // 가격 추천 로직 추가 필요

                // "예" 버튼 클릭 시
                builder.setPositiveButton("예") { _, _ ->
                    Toast.makeText(this, "결제가 진행됩니다!", Toast.LENGTH_SHORT).show()
                    // 여기에 결제 로직 추가 가능 (예: 서버 요청)
                }

                // "아니오" 버튼 클릭 시
                builder.setNegativeButton("아니오") { dialog, _ ->
                    dialog.dismiss() // 팝업 닫기
                }

                // 다이얼로그 표시
                builder.show()
            }
        }
    }
}