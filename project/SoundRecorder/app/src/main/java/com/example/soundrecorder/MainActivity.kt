//package com.example.soundrecorder
//
//import android.Manifest
//import android.R
//import android.content.ContentValues
//import android.content.pm.PackageManager
//import android.media.MediaPlayer
//import android.media.MediaRecorder
//import android.net.Uri
//import android.os.Bundle
//import android.os.Environment
//import android.provider.MediaStore
//import android.widget.ArrayAdapter
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.core.app.ActivityCompat
//import androidx.core.content.ContextCompat
//import com.example.soundrecorder.databinding.ActivityMainBinding
//import java.io.File
//import java.io.IOException
//import java.text.SimpleDateFormat
//import java.util.*
//
//class MainActivity : AppCompatActivity() {
//    private lateinit var binding: ActivityMainBinding
//    private var mediaRecorder: MediaRecorder? = null
//    private var mediaPlayer: MediaPlayer? = null
//    private var fileName: String = ""
//    private val recordingsList = mutableListOf<String>()
//    private lateinit var adapter: ArrayAdapter<String>
//    private val RECORD_REQUEST_CODE = 101
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)
//
//        setupPermissions()
//        setupUI()
//        loadRecordings()
//    }
//
//    private fun setupPermissions() {
//        val permissions = arrayOf(
//            Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
//            Manifest.permission.READ_EXTERNAL_STORAGE
//        )
//
//        if (!hasPermissions(permissions)) {
//            ActivityCompat.requestPermissions(this, permissions, RECORD_REQUEST_CODE)
//        }
//    }
//
//    private fun hasPermissions(permissions: Array<String>): Boolean {
//        return permissions.all {
//            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
//        }
//    }
//
//    private fun setupUI() {
//        adapter = ArrayAdapter(this, R.layout.simple_list_item_1, recordingsList)
//        binding.recordingsList.adapter = adapter
//
//        binding.btnRecord.setOnClickListener {
//            startRecording()
//        }
//
//        binding.btnStop.setOnClickListener {
//            stopRecording()
//        }
//
//        binding.recordingsList.setOnItemClickListener { _, _, position, _ ->
//            playRecording(recordingsList[position])
//        }
//    }
//
//    private fun startRecording() {
//        try {
//            // Tạo tên file dựa trên thời gian
//            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
//            fileName = "AUDIO_${timeStamp}.mp3"
//
//            mediaRecorder = MediaRecorder().apply {
//                setAudioSource(MediaRecorder.AudioSource.MIC)
//                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
//
//                // Lưu file tạm thời
//                val file = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), fileName)
//                setOutputFile(file.absolutePath)
//
//                prepare()
//                start()
//            }
//
//            binding.btnRecord.isEnabled = false
//            binding.btnStop.isEnabled = true
//            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show()
//
//        } catch (e: IOException) {
//            e.printStackTrace()
//            Toast.makeText(this, "Recording failed", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun stopRecording() {
//        try {
//            mediaRecorder?.apply {
//                stop()
//                release()
//            }
//            mediaRecorder = null
//
//            binding.btnRecord.isEnabled = true
//            binding.btnStop.isEnabled = false
//
//            // Lưu file vào MediaStore
//            saveToMediaStore()
//            loadRecordings()
//
//            Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show()
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Toast.makeText(this, "Error stopping recording", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun saveToMediaStore() {
//        val file = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), fileName)
//
//        val values = ContentValues().apply {
//            put(MediaStore.Audio.Media.DISPLAY_NAME, fileName)
//            put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3")
//            put(MediaStore.Audio.Media.RELATIVE_PATH, Environment.DIRECTORY_MUSIC)
//            put(MediaStore.Audio.Media.IS_PENDING, 1)
//        }
//
//        val resolver = contentResolver
//        val uri = resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)
//
//        uri?.let {
//            resolver.openOutputStream(it)?.use { outputStream ->
//                file.inputStream().use { inputStream ->
//                    inputStream.copyTo(outputStream)
//                }
//            }
//
//            values.clear()
//            values.put(MediaStore.Audio.Media.IS_PENDING, 0)
//            resolver.update(uri, values, null, null)
//        }
//    }
//
//    private fun loadRecordings() {
//        recordingsList.clear()
//
//        val projection = arrayOf(
//            MediaStore.Audio.Media.DISPLAY_NAME,
//            MediaStore.Audio.Media._ID
//        )
//
//        val cursor = contentResolver.query(
//            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//            projection,
//            null,
//            null,
//            MediaStore.Audio.Media.DATE_ADDED + " DESC"
//        )
//
//        cursor?.use {
//            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
//            while (it.moveToNext()) {
//                val name = it.getString(nameColumn)
//                if (name.startsWith("AUDIO_")) {
//                    recordingsList.add(name)
//                }
//            }
//        }
//
//        adapter.notifyDataSetChanged()
//    }
//
//    private fun playRecording(fileName: String) {
//        try {
//            mediaPlayer?.release()
//            mediaPlayer = MediaPlayer()
//
//            val cursor = contentResolver.query(
//                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                arrayOf(MediaStore.Audio.Media._ID),
//                "${MediaStore.Audio.Media.DISPLAY_NAME}=?",
//                arrayOf(fileName),
//                null
//            )
//
//            cursor?.use {
//                if (it.moveToFirst()) {
//                    val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
//                    val uri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
//                    val contentUri = Uri.withAppendedPath(uri, id.toString())
//
//                    mediaPlayer?.apply {
//                        setDataSource(this@MainActivity, contentUri)
//                        prepare()
//                        start()
//                    }
//                }
//            }
//
//            mediaPlayer?.setOnCompletionListener {
//                it.release()
//                mediaPlayer = null
//            }
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//            Toast.makeText(this, "Error playing recording", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        mediaRecorder?.release()
//        mediaPlayer?.release()
//    }
//}


package com.example.soundrecorder

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.soundrecorder.databinding.ActivityMainBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var mediaRecorder: MediaRecorder? = null
    private var mediaPlayer: MediaPlayer? = null
    private var fileName: String = ""
    private val recordingsList = mutableListOf<String>()
    private lateinit var adapter: ArrayAdapter<String>
    private val RECORD_REQUEST_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupPermissions()
        setupUI()
        loadRecordings()
    }

    private fun setupPermissions() {
        val permissions = arrayOf(
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE
        )

        if (!hasPermissions(permissions)) {
            ActivityCompat.requestPermissions(this, permissions, RECORD_REQUEST_CODE)
        }
    }

    private fun hasPermissions(permissions: Array<String>): Boolean {
        return permissions.all {
            ContextCompat.checkSelfPermission(this, it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun setupUI() {
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, recordingsList)
        binding.recordingsList.adapter = adapter

        binding.btnRecord.setOnClickListener {
            startRecording()
        }

        binding.btnStop.setOnClickListener {
            stopRecording()
        }

        binding.recordingsList.setOnItemClickListener { _, _, position, _ ->
            playRecording(recordingsList[position])
        }
    }

    private fun startRecording() {
        try {
            // Tạo tên file dựa trên thời gian
            val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
            fileName = "AUDIO_${timeStamp}.mp3"

            mediaRecorder = MediaRecorder().apply {
                setAudioSource(MediaRecorder.AudioSource.MIC)
                setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                setAudioEncoder(MediaRecorder.AudioEncoder.AAC)

                // Lưu file tạm thời
                val file = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), fileName)
                setOutputFile(file.absolutePath)

                prepare()
                start()
            }

            binding.btnRecord.isEnabled = false
            binding.btnStop.isEnabled = true
            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show()

        } catch (e: IOException) {
            e.printStackTrace()
            Toast.makeText(this, "Recording failed", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopRecording() {
        try {
            mediaRecorder?.apply {
                stop()
                release()
            }
            mediaRecorder = null

            binding.btnRecord.isEnabled = true
            binding.btnStop.isEnabled = false

            // Lưu file vào MediaStore
            saveToMediaStore()
            loadRecordings()

            Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error stopping recording", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveToMediaStore() {
        val file = File(getExternalFilesDir(Environment.DIRECTORY_MUSIC), fileName)

        val values = ContentValues().apply {
            put(MediaStore.Audio.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Audio.Media.MIME_TYPE, "audio/mp3")
            put(MediaStore.Audio.Media.RELATIVE_PATH, Environment.DIRECTORY_MUSIC)
            put(MediaStore.Audio.Media.IS_PENDING, 1)
        }

        val resolver = contentResolver
        val uri = resolver.insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, values)

        uri?.let {
            resolver.openOutputStream(it)?.use { outputStream ->
                file.inputStream().use { inputStream ->
                    inputStream.copyTo(outputStream)
                }
            }

            values.clear()
            values.put(MediaStore.Audio.Media.IS_PENDING, 0)
            resolver.update(uri, values, null, null)
        }
    }

    private fun loadRecordings() {
        recordingsList.clear()

        val projection = arrayOf(
            MediaStore.Audio.Media.DISPLAY_NAME,
            MediaStore.Audio.Media._ID
        )

        val cursor = contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            null,
            null,
            MediaStore.Audio.Media.DATE_ADDED + " DESC"
        )

        cursor?.use {
            val nameColumn = it.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)
            while (it.moveToNext()) {
                val name = it.getString(nameColumn)
                if (name.startsWith("AUDIO_")) {
                    recordingsList.add(name)
                }
            }
        }

        adapter.notifyDataSetChanged()
    }

    private fun playRecording(fileName: String) {
        try {
            mediaPlayer?.release()
            mediaPlayer = MediaPlayer()

            val cursor = contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                arrayOf(MediaStore.Audio.Media._ID),
                "${MediaStore.Audio.Media.DISPLAY_NAME}=?",
                arrayOf(fileName),
                null
            )

            cursor?.use {
                if (it.moveToFirst()) {
                    val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.Audio.Media._ID))
                    val uri = MediaStore.Audio.Media.getContentUri(MediaStore.VOLUME_EXTERNAL)
                    val contentUri = android.net.Uri.withAppendedPath(uri, id.toString())

                    mediaPlayer?.apply {
                        setDataSource(this@MainActivity, contentUri)
                        prepare()
                        start()
                    }
                }
            }

            mediaPlayer?.setOnCompletionListener {
                it.release()
                mediaPlayer = null
            }

        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "Error playing recording", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaRecorder?.release()
        mediaPlayer?.release()
    }
}