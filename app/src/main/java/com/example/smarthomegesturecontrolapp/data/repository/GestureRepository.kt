package com.example.smarthomegesturecontrolapp.data.repository

import com.example.smarthomegesturecontrolapp.data.model.Gesture

class GestureRepository {

    // This simulates getting data from a server
    fun getAvailableGestures(): List<Gesture> {
        return listOf(
            Gesture(
                id = "1",
                name = "Swipe Left (Light On)",
                downloadUrl = "http://example.com/swipe_left.mp4",
                localPath = null
            ),
            Gesture(
                id = "2",
                name = "Swipe Right (Fan On)",
                downloadUrl = "http://example.com/swipe_right.mp4",
                localPath = null
            ),
            Gesture(
                id = "3",
                name = "Circle Clockwise (Volume Up)",
                downloadUrl = "http://example.com/circle_cw.mp4",
                localPath = null
            )
        )
    }
}