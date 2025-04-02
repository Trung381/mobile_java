package com.example.playvideo

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.MediaController
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.playvideo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val PICK_VIDEO_REQUEST = 1
    private val STORAGE_PERMISSION_CODE = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up MediaController for video playback controls
        val mediaController = MediaController(this)
        mediaController.setAnchorView(binding.videoView)
        binding.videoView.setMediaController(mediaController)

        // Button to pick video from device
        binding.btnPickVideo.setOnClickListener {
            if (checkStoragePermission()) {
                pickVideoFromDevice()
            } else {
                requestStoragePermission()
            }
        }

        // Button to play video from URL
        binding.btnPlayUrl.setOnClickListener {
            val url = binding.urlEditText.text.toString()
            if (url.isNotEmpty()) {
                playVideoFromUrl(url)
            } else {
                Toast.makeText(this, "Please enter a valid URL", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Check if storage permission is granted
    private fun checkStoragePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE
        ) == PackageManager.PERMISSION_GRANTED
    }

    // Request storage permission
    private fun requestStoragePermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            STORAGE_PERMISSION_CODE
        )
    }

    // Handle permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                pickVideoFromDevice()
            } else {
                Toast.makeText(this, "Storage permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Launch intent to pick video from MediaStore
    private fun pickVideoFromDevice() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, PICK_VIDEO_REQUEST)
    }

    // Play video from a URL
    private fun playVideoFromUrl(url: String) {
        try {
            val uri = Uri.parse(url)
            binding.videoView.setVideoURI(uri)
            binding.videoView.setOnPreparedListener {
                binding.videoView.start()
            }
            binding.videoView.setOnErrorListener { _, what, extra ->
                Toast.makeText(this, "Error playing video: $what, $extra", Toast.LENGTH_SHORT).show()
                true
            }
        } catch (e: Exception) {
            Toast.makeText(this, "Invalid URL or error: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }

    // Handle result from picking video
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_VIDEO_REQUEST && resultCode == RESULT_OK && data != null) {
            val videoUri = data.data
            if (videoUri != null) {
                binding.videoView.setVideoURI(videoUri)
                binding.videoView.setOnPreparedListener {
                    binding.videoView.start()
                }
                binding.videoView.setOnErrorListener { _, what, extra ->
                    Toast.makeText(this, "Error playing video: $what, $extra", Toast.LENGTH_SHORT).show()
                    true
                }
            } else {
                Toast.makeText(this, "Failed to load video", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Manage VideoView lifecycle
    override fun onPause() {
        super.onPause()
        binding.videoView.pause()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding.videoView.stopPlayback()
    }
}
