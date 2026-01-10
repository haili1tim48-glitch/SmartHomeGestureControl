package com.example.smarthomegesturecontrolapp.ui.gesturelist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.smarthomegesturecontrolapp.data.model.Gesture
import com.example.smarthomegesturecontrolapp.data.repository.GestureRepository
import kotlinx.coroutines.launch

/**
 * Represents the UI state for the gesture list screen
 */
sealed class GestureListUiState {
    data object Loading : GestureListUiState()
    data class Success(val gestures: List<Gesture>) : GestureListUiState()
    data class Error(val message: String) : GestureListUiState()
}

/**
 * ViewModel for the GestureListActivity.
 * Handles the download process and exposes UI state via LiveData.
 */
class GestureListViewModel(
    private val repository: GestureRepository
) : ViewModel() {

    private val _uiState = MutableLiveData<GestureListUiState>()
    val uiState: LiveData<GestureListUiState> = _uiState

    init {
        // Automatically trigger download when ViewModel is created
        downloadGestures()
    }

    /**
     * Downloads gesture videos from assets to internal storage.
     * This always performs a force refresh (deletes and re-copies files).
     */
    fun downloadGestures() {
        _uiState.value = GestureListUiState.Loading

        viewModelScope.launch {
            val result = repository.downloadGestures()

            result.fold(
                onSuccess = { gestures ->
                    _uiState.value = GestureListUiState.Success(gestures)
                },
                onFailure = { exception ->
                    _uiState.value = GestureListUiState.Error(
                        exception.message ?: "Unknown error occurred while downloading gestures"
                    )
                }
            )
        }
    }

    /**
     * Factory for creating GestureListViewModel with repository dependency
     */
    class Factory(private val repository: GestureRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(GestureListViewModel::class.java)) {
                return GestureListViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
