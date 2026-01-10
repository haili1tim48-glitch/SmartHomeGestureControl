package com.example.smarthomegesturecontrolapp.ui

import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomegesturecontrolapp.R
import com.example.smarthomegesturecontrolapp.data.model.Gesture
import com.example.smarthomegesturecontrolapp.ui.adapter.GestureAdapter
import com.example.smarthomegesturecontrolapp.ui.viewmodel.DownloadState
import com.example.smarthomegesturecontrolapp.ui.viewmodel.GestureListViewModel

class GestureListActivity : AppCompatActivity() {

    private val viewModel: GestureListViewModel by viewModels()

    private lateinit var rvGestures: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var tvDownloadStatus: TextView
    private lateinit var tvError: TextView
    private lateinit var tvEmptyState: TextView
    private lateinit var gestureAdapter: GestureAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture_list)

        initViews()
        setupCloseButton()
        setupRecyclerView()
        observeDownloadState()

        // Automatically start download when activity is created
        viewModel.downloadGestures()
    }

    private fun initViews() {
        rvGestures = findViewById(R.id.rvGestures)
        progressBar = findViewById(R.id.progressBar)
        tvDownloadStatus = findViewById(R.id.tvDownloadStatus)
        tvError = findViewById(R.id.tvError)
        tvEmptyState = findViewById(R.id.tvEmptyState)
    }

    private fun setupCloseButton() {
        val btnClose: ImageButton = findViewById(R.id.btnClose)
        btnClose.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        gestureAdapter = GestureAdapter(emptyList()) { gesture ->
            onGestureClicked(gesture)
        }

        rvGestures.layoutManager = LinearLayoutManager(this)
        rvGestures.adapter = gestureAdapter
    }

    private fun observeDownloadState() {
        viewModel.downloadState.observe(this) { state ->
            when (state) {
                is DownloadState.Idle -> {
                    showIdleState()
                }
                is DownloadState.Downloading -> {
                    showDownloadingState()
                }
                is DownloadState.Success -> {
                    showSuccessState(state.gestures)
                }
                is DownloadState.Error -> {
                    showErrorState(state.message)
                }
            }
        }
    }

    private fun showIdleState() {
        progressBar.visibility = View.GONE
        tvDownloadStatus.visibility = View.GONE
        tvError.visibility = View.GONE
        rvGestures.visibility = View.GONE
        tvEmptyState.visibility = View.GONE
    }

    private fun showDownloadingState() {
        progressBar.visibility = View.VISIBLE
        tvDownloadStatus.visibility = View.VISIBLE
        tvDownloadStatus.text = "Downloading gestures..."
        tvError.visibility = View.GONE
        rvGestures.visibility = View.GONE
        tvEmptyState.visibility = View.GONE
    }

    private fun showSuccessState(gestures: List<Gesture>) {
        progressBar.visibility = View.GONE
        tvDownloadStatus.visibility = View.GONE
        tvError.visibility = View.GONE

        if (gestures.isEmpty()) {
            rvGestures.visibility = View.GONE
            tvEmptyState.visibility = View.VISIBLE
        } else {
            rvGestures.visibility = View.VISIBLE
            tvEmptyState.visibility = View.GONE
            gestureAdapter.updateGestures(gestures)
        }
    }

    private fun showErrorState(message: String) {
        progressBar.visibility = View.GONE
        tvDownloadStatus.visibility = View.GONE
        tvError.visibility = View.VISIBLE
        tvError.text = "Error: $message"
        rvGestures.visibility = View.GONE
        tvEmptyState.visibility = View.GONE
    }

    private fun onGestureClicked(gesture: Gesture) {
        Toast.makeText(
            this,
            "Selected: ${gesture.name}\nPath: ${gesture.localPath}",
            Toast.LENGTH_LONG
        ).show()
    }
}
