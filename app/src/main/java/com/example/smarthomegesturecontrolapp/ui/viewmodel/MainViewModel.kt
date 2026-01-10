package com.example.smarthomegesturecontrolapp.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.smarthomegesturecontrolapp.data.model.Gesture
import com.example.smarthomegesturecontrolapp.data.repository.GestureRepository

class MainViewModel : ViewModel() {

    // 1. Initialize the Repository
    private val repository = GestureRepository()

    // 2. Create a "Live" list that the UI can watch
    // _gestures is private so only this ViewModel can change it
    private val _gestures = MutableLiveData<List<Gesture>>()

    // gestures is public (but read-only) so the UI can read it
    val gestures: LiveData<List<Gesture>> = _gestures

    // 3. Function to load data
    fun loadGestures() {
        // We get the list from the repo and post it to the LiveData
        val data = repository.getAvailableGestures()
        _gestures.value = data
    }
}