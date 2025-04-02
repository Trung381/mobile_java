package com.example.laban


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null
    private lateinit var compassImage: ImageView
    private lateinit var degreeText: TextView

    private var gravity = FloatArray(3)
    private var geomagnetic = FloatArray(3)
    private var lastUpdateTime = 0L
    private val UPDATE_INTERVAL = 100 // milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Khởi tạo views
        compassImage = findViewById(R.id.compass)
        degreeText = findViewById(R.id.degreeText)

        // Khởi tạo SensorManager và các sensor
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        // Kiểm tra xem thiết bị có hỗ trợ cảm biến không
        if (accelerometer == null || magnetometer == null) {
            degreeText.text = "Thiết bị không hỗ trợ la bàn"
        }
    }

    override fun onResume() {
        super.onResume()
        // Đăng ký listeners cho các cảm biến
        accelerometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
        magnetometer?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        // Hủy đăng ký listeners để tiết kiệm pin
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            // Lấy dữ liệu từ cảm biến
            when (it.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> gravity = it.values.clone()
                Sensor.TYPE_MAGNETIC_FIELD -> geomagnetic = it.values.clone()
            }

            // Chỉ cập nhật nếu có dữ liệu từ cả hai cảm biến
            val currentTime = System.currentTimeMillis()
            if (currentTime - lastUpdateTime < UPDATE_INTERVAL) return
            lastUpdateTime = currentTime

            // Tính toán hướng
            val rotationMatrix = FloatArray(9)
            val inclinationMatrix = FloatArray(9)
            val success = SensorManager.getRotationMatrix(
                rotationMatrix,
                inclinationMatrix,
                gravity,
                geomagnetic
            )

            if (success) {
                val orientation = FloatArray(3)
                SensorManager.getOrientation(rotationMatrix, orientation)

                // Chuyển đổi từ radian sang độ
                val azimuth = Math.toDegrees(orientation[0].toDouble()).toFloat()
                val degree = (if (azimuth < 0) azimuth + 360 else azimuth)

                // Xoay kim la bàn (đảo dấu vì kim chỉ hướng bắc)
                compassImage.rotation = -degree

                // Hiển thị góc lệch
                degreeText.text = "${degree.toInt()}°"
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Xử lý khi độ chính xác thay đổi (nếu cần)
    }
}