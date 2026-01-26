package com.example.smarthomegesturecontrolapp.ui

import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomegesturecontrolapp.R
import com.example.smarthomegesturecontrolapp.data.model.Gesture
import com.example.smarthomegesturecontrolapp.data.repository.GestureRepository
import com.example.smarthomegesturecontrolapp.ui.adapter.GestureAdapter

class GestureListActivity : AppCompatActivity() {

    private lateinit var rvGestures: RecyclerView
    private lateinit var gestureAdapter: GestureAdapter
    private val repository = GestureRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gesture_list)

        setupCloseButton()
        setupRecyclerView()
        loadGestures()
    }

    private fun setupCloseButton() {
        val btnClose: ImageButton = findViewById(R.id.btnClose)
        btnClose.setOnClickListener {
            finish()
        }
    }

    private fun setupRecyclerView() {
        rvGestures = findViewById(R.id.rvGestures)

        rvGestures.layoutManager = LinearLayoutManager(this)

        gestureAdapter = GestureAdapter(emptyList()) { gesture ->
            onGestureClicked(gesture)
        }
        rvGestures.adapter = gestureAdapter
    }

    private fun loadGestures() {
        val gestures = repository.getAvailableGestures()
        gestureAdapter.updateGestures(gestures)
    }

    private fun onGestureClicked(gesture: Gesture) {
        Toast.makeText(this, "Selected: ${gesture.name}", Toast.LENGTH_SHORT).show()
    }
}
