import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Rect
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import org.tensorflow.lite.Interpreter
import java.nio.ByteBuffer
import java.nio.ByteOrder
import android.widget.Toast
import com.example.test.R
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.face.FaceDetection
import com.google.mlkit.vision.face.FaceDetectorOptions
import android.graphics.ImageFormat
import android.graphics.YuvImage
import android.media.Image
import androidx.camera.core.*
import androidx.camera.core.ImageProxy
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import java.io.ByteArrayOutputStream

class Model : AppCompatActivity() {
    private lateinit var yoloInterpreter: Interpreter
    private lateinit var ageInterpreter: Interpreter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.startorder)

        // 모델 로드
        yoloInterpreter = loadTFLiteModel("yolo11n.tflite")
        ageInterpreter = loadTFLiteModel("cnn_model.tflite")

        // 카메라 실행
        startCamera()
    }

    // TFLite 모델 로드
    private fun loadTFLiteModel(modelName: String): Interpreter {
        val assetFileDescriptor = assets.openFd(modelName)
        val inputStream = assetFileDescriptor.createInputStream()
        val modelBuffer = inputStream.readBytes()
        val buffer = ByteBuffer.allocateDirect(modelBuffer.size).order(ByteOrder.nativeOrder())
        buffer.put(modelBuffer)
        return Interpreter(buffer)
    }

    // 얼굴 검출 및 연령대 분류
    private fun detectFaceAndPredictAge(bitmap: Bitmap) {
        val faceDetector = FaceDetection.getClient(
            FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                .build()
        )

        val image = InputImage.fromBitmap(bitmap, 0)
        faceDetector.process(image)
            .addOnSuccessListener { faces ->
                for (face in faces) {
                    val boundingBox = face.boundingBox
                    val faceBitmap = cropBitmap(bitmap, boundingBox)
                    val agePrediction = predictAge(faceBitmap)

                    runOnUiThread {
                        Toast.makeText(this, "예측된 연령대: $agePrediction", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .addOnFailureListener { e ->
                Log.e("FaceDetection", "Error: ${e.message}")
            }
    }

    // 얼굴 크롭
    private fun cropBitmap(bitmap: Bitmap, rect: Rect): Bitmap {
        return Bitmap.createBitmap(
            bitmap,
            rect.left,
            rect.top,
            rect.width(),
            rect.height()
        )
    }

    // 연령대 예측 (TFLite 실행)
    private fun predictAge(bitmap: Bitmap): String {
        val input = preprocessBitmap(bitmap)
        val output = Array(1) { FloatArray(3) } // 3개 클래스 (kid, middle, senior)
        ageInterpreter.run(input, output)

        val ageLabels = arrayOf("kid", "middle", "senior")
        return ageLabels[output[0].indices.maxByOrNull { output[0][it] } ?: 0]
    }

    // 이미지 전처리
    private fun preprocessBitmap(bitmap: Bitmap): ByteBuffer {
        val resized = Bitmap.createScaledBitmap(bitmap, 128, 128, true)
        val buffer = ByteBuffer.allocateDirect(128 * 128 * 3 * 4).order(ByteOrder.nativeOrder())
        val pixels = IntArray(128 * 128)
        resized.getPixels(pixels, 0, 128, 0, 0, 128, 128)
        for (pixel in pixels) {
            val r = (pixel shr 16 and 0xFF) / 255.0f
            val g = (pixel shr 8 and 0xFF) / 255.0f
            val b = (pixel and 0xFF) / 255.0f
            buffer.putFloat(r)
            buffer.putFloat(g)
            buffer.putFloat(b)
        }
        return buffer
    }

    // 카메라 실행
    private fun startCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder().build().also {
                it.setSurfaceProvider(viewFinder.surfaceProvider)
            }

            val imageAnalyzer = ImageAnalysis.Builder()
                .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                .build()
                .also {
                    it.setAnalyzer(ContextCompat.getMainExecutor(this)) { image ->
                        val bitmap = imageToBitmap(image)
                        detectFaceAndPredictAge(bitmap) // 실시간 분석 실행
                        image.close()
                    }
                }

            val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(this, cameraSelector, preview, imageAnalyzer)
            } catch (exc: Exception) {
                Log.e("CameraX", "카메라 바인딩 실패: ${exc.message}")
            }
        }, ContextCompat.getMainExecutor(this))
    }

    private fun imageToBitmap(image: ImageProxy): Bitmap {
        val yBuffer = image.planes[0].buffer
        val vuBuffer = image.planes[2].buffer

        val ySize = yBuffer.remaining()
        val vuSize = vuBuffer.remaining()

        val nv21 = ByteArray(ySize + vuSize)

        yBuffer.get(nv21, 0, ySize)
        vuBuffer.get(nv21, ySize, vuSize)

        val yuvImage = YuvImage(nv21, ImageFormat.NV21, image.width, image.height, null)
        val out = ByteArrayOutputStream()
        yuvImage.compressToJpeg(Rect(0, 0, image.width, image.height), 100, out)

        val imageBytes = out.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}