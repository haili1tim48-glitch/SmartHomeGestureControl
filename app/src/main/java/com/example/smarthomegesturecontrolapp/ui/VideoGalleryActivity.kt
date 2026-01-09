package com.example.smarthomegesturecontrolapp.ui

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomegesturecontrolapp.R
import com.example.smarthomegesturecontrolapp.data.model.Video
import com.example.smarthomegesturecontrolapp.ui.adapter.VideoAdapter

class VideoGalleryActivity : AppCompatActivity() {

    private lateinit var rvVideos: RecyclerView
    private lateinit var videoAdapter: VideoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_gallery)

        setupRecyclerView()
        loadSampleVideos()
    }

    private fun setupRecyclerView() {
        rvVideos = findViewById(R.id.rvVideos)

        // Set up GridLayoutManager with 2 columns
        val gridLayoutManager = GridLayoutManager(this, 2)
        rvVideos.layoutManager = gridLayoutManager

        // Initialize adapter with empty list
        videoAdapter = VideoAdapter(emptyList()) { video ->
            onVideoClicked(video)
        }
        rvVideos.adapter = videoAdapter
    }

    private fun loadSampleVideos() {
        // Sample video data for demonstration
        val sampleVideos = listOf(
            Video(id = "1", name = "Living Room Light On"),
            Video(id = "2", name = "Living Room Light Off"),
            Video(id = "3", name = "Bedroom Fan On"),
            Video(id = "4", name = "Bedroom Fan Off"),
            Video(id = "5", name = "Kitchen Appliances"),
            Video(id = "6", name = "Security System Arm"),
            Video(id = "7", name = "Thermostat Adjust"),
            Video(id = "8", name = "Garage Door Open")
        )

        videoAdapter.updateVideos(sampleVideos)
    }

    private fun onVideoClicked(video: Video) {
        Toast.makeText(this, "Selected: ${video.name}", Toast.LENGTH_SHORT).show()
    }
}
