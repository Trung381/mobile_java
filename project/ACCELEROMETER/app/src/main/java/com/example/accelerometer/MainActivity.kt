package com.example.accelerometer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.max
import kotlin.math.min

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private lateinit var xText: TextView
    private lateinit var yText: TextView
    private lateinit var zText: TextView
    private lateinit var ball: ImageView
    private var ballX: Float = 0f
    private var ballY: Float = 0f
    private val SPEED_FACTOR = 10.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo views
        xText = findViewById(R.id.xValue)
        yText = findViewById(R.id.yValue)
        zText = findViewById(R.id.zValue)
        ball = findViewById(R.id.ball)

        // Khởi tạo SensorManager và accelerometer
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        // Lấy vị trí ban đầu của quả bóng
        ballX = ball.x
        ballY = ball.y
    }

    override fun onResume() {
        super.onResume()
        // Đăng ký listener cho cảm biến
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_GAME)
        }
    }

    override fun onPause() {
        super.onPause()
        // Hủy đăng ký listener khi activity tạm dừng
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                // Lấy giá trị gia tốc theo 3 trục
                val x = it.values[0]
                val y = it.values[1]
                val z = it.values[2]

                // Cập nhật TextViews
                xText.text = "X: %.2f".format(x)
                yText.text = "Y: %.2f".format(y)
                zText.text = "Z: %.2f".format(z)

                // Tính toán vị trí mới cho quả bóng
                ballX -= x * SPEED_FACTOR
                ballY += y * SPEED_FACTOR

                // Giới hạn quả bóng trong màn hình
                val maxX = windowManager.defaultDisplay.width - ball.width
                val maxY = windowManager.defaultDisplay.height - ball.height

                ballX = max(0f, min(ballX, maxX.toFloat()))
                ballY = max(0f, min(ballY, maxY.toFloat()))

                // Cập nhật vị trí quả bóng
                ball.x = ballX
                ball.y = ballY
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Xử lý khi độ chính xác thay đổi (nếu cần)
    }
}