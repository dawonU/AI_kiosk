//MainActivity
package com.example.kiosk

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.kiosk.network.ApiService
import com.example.kiosk.network.PredictionResponse
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream

class MainActivity : AppCompatActivity() {

    private val REQUEST_IMAGE_CAPTURE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 필요시 임시 레이아웃을 설정할 수 있지만 여기서는 카메라 기능만 사용합니다.
        // 카메라 촬영 Intent 실행
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        if (takePictureIntent.resolveActivity(packageManager) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
        } else {
            Toast.makeText(this, "카메라 앱을 찾을 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    // 카메라 촬영 후 결과 수신
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // 축소된 Bitmap 이미지 획득
            val bitmap = data?.extras?.get("data") as Bitmap
            // 서버로 이미지 전송
            sendImageToServer(bitmap)
            // 사진 촬영 후 Intro Activity 실행
            startActivity(Intent(this, Intro::class.java))
            finish()
        }
    }

    // Bitmap 이미지를 파일로 변환 후 서버에 전송하는 메서드
    fun sendImageToServer(bitmap: Bitmap) {
        val file = bitmapToFile(bitmap, "temp_image.jpg")
        val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
        val body = MultipartBody.Part.createFormData("image", file.name, requestFile)

        // Retrofit 인스턴스 생성 (ngrok 주소 사용 예시)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://5cd3-220-76-202-228.ngrok-free.app/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(ApiService::class.java)
        api.uploadImage(body).enqueue(object : Callback<PredictionResponse> {
            override fun onResponse(
                call: Call<PredictionResponse>,
                response: Response<PredictionResponse>
            ) {
                val ageGroup = response.body()?.age_group ?: ""
                Log.d("Intro", "전달받은 age_group: $ageGroup")
                Toast.makeText(applicationContext, "예상 연령대: $ageGroup", Toast.LENGTH_LONG).show()
                // age_group 값을 Intro 액티비티로 전달
                val intent = Intent(this@MainActivity, Intro::class.java)
                intent.putExtra("age_group", ageGroup)
                startActivity(intent)
            }

            override fun onFailure(call: Call<PredictionResponse>, t: Throwable) {
                TODO("Not yet implemented")
            }
        })
    }

    // Bitmap을 File로 변환하는 헬퍼 메서드
    fun bitmapToFile(bitmap: Bitmap, fileName: String): File {
        val file = File(cacheDir, fileName)
        file.createNewFile()
        val fos = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos)
        fos.flush()
        fos.close()
        return file
    }
}
