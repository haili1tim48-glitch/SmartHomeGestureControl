package com.example.smarthomegesturecontrolapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.smarthomegesturecontrolapp.ui.GestureListActivity
import com.example.smarthomegesturecontrolapp.ui.VideoGalleryActivity
import com.example.smarthomegesturecontrolapp.ui.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    // 1. Get a reference to the ViewModel (this requires the activity-ktx dependency)
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // 2. Find the UI elements
        val btnLoad = findViewById<Button>(R.id.btnLoadGestures)
        val tvStatus = findViewById<TextView>(R.id.tvStatus)

        // 3. Observe the Data (The Listener)
        // Whenever the ViewModel updates the list, this code runs automatically.
        viewModel.gestures.observe(this) { gestureList ->
            val resultText = StringBuilder()
            resultText.append("Found ${gestureList.size} gestures:\n\n")

            for (gesture in gestureList) {
                resultText.append("• ${gesture.name}\n")
            }

            tvStatus.text = resultText.toString()
        }

        // 4. Set up the Button Click - Navigate to GestureListActivity
        btnLoad.setOnClickListener {
            val intent = Intent(this, GestureListActivity::class.java)
            startActivity(intent)
        }

        // 5. Set up Browse Videos button
        val btnBrowseVideos = findViewById<Button>(R.id.btnBrowseVideos)
        btnBrowseVideos.setOnClickListener {
            val intent = Intent(this, VideoGalleryActivity::class.java)
            startActivity(intent)
        }
    }
}