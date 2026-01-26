package com.example.smarthomegesturecontrolapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthomegesturecontrolapp.ui.GestureListActivity
import com.example.smarthomegesturecontrolapp.ui.VideoGalleryActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Set up Load Gestures button - Navigate to GestureListActivity
        val btnLoad = findViewById<Button>(R.id.btnLoadGestures)
        btnLoad.setOnClickListener {
            val intent = Intent(this, GestureListActivity::class.java)
            startActivity(intent)
        }

        // Set up Browse Videos button
        val btnBrowseVideos = findViewById<Button>(R.id.btnBrowseVideos)
        btnBrowseVideos.setOnClickListener {
            val intent = Intent(this, VideoGalleryActivity::class.java)
            startActivity(intent)
        }
    }
}
