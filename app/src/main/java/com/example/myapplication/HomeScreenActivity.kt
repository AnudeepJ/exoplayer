package com.example.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.media3.common.util.UnstableApi
import com.example.myapplication.databinding.ActivityHomeScreenBinding
@UnstableApi
class HomeScreenActivity : AppCompatActivity() {
    lateinit var binding:ActivityHomeScreenBinding
    var isOfflineVideo = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeScreenBinding.inflate(LayoutInflater.from(this))
        val intent = Intent(this,MainActivity::class.java)
        binding.offlineVideos.setOnClickListener {

            isOfflineVideo = true
            intent.putExtra("IS_OFFLINE",isOfflineVideo)
            startActivity(intent)
        }
        binding.onlineVideos.setOnClickListener {
            isOfflineVideo = false
            intent.putExtra("IS_OFFLINE",isOfflineVideo)
            startActivity(intent)
        }
        setContentView(binding.root)
    }
}