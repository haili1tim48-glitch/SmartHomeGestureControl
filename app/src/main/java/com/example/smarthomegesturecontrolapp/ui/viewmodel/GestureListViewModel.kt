package com.example.smarthomegesturecontrolapp.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.smarthomegesturecontrolapp.data.model.Gesture
import com.example.smarthomegesturecontrolapp.data.repository.GestureRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Sealed class representing the different states of the download process.
 */
sealed class DownloadState {
    object Idle : DownloadState()
    object Downloading : DownloadState()
    data class Success(val gestures: List<Gesture>) : DownloadState()
    data class Error(val message: String) : DownloadState()
}

/**
 * ViewModel for GestureListActivity.
 * Handles the "download" simulation from assets to internal storage.
 */
class GestureListViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = GestureRepository(application.applicationContext)

    private val _downloadState = MutableLiveData<DownloadState>(DownloadState.Idle)
    val downloadState: LiveData<DownloadState> = _downloadState

    private val _gestures = MutableLiveData<List<Gesture>>(emptyList())
    val gestures: LiveData<List<Gesture>> = _gestures

    /**
     * Initiates the download process.
     * This copies video files from assets to internal storage.
     * Always performs a fresh copy (force refresh).
     */
    fun downloadGestures() {
        _downloadState.value = DownloadState.Downloading

        viewModelScope.launch {
            // Perform I/O operation on background thread
            val result = withContext(Dispatchers.IO) {
                // Add a small delay to simulate network download
                delay(1500)
                repository.downloadGesturesToInternalStorage()
            }

            result.fold(
                onSuccess = { gestureList ->
                    _gestures.value = gestureList
                    _downloadState.value = DownloadState.Success(gestureList)
                },
                onFailure = { error ->
                    _downloadState.value = DownloadState.Error(
                        error.message ?: "Unknown error occurred"
                    )
                }
            )
        }
    }

    /**
     * Resets the state to idle.
     */
    fun resetState() {
        _downloadState.value = DownloadState.Idle
    }
}
