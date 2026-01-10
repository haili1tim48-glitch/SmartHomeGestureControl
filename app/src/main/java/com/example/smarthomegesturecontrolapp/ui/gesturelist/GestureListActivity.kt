package com.example.smarthomegesturecontrolapp.ui.gesturelist

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomegesturecontrolapp.R
import com.example.smarthomegesturecontrolapp.data.repository.GestureRepository
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton

/**
 * Activity that displays the list of available gestures.
 * Handles downloading gesture videos from assets and displaying them in a RecyclerView.
 */
class GestureListActivity : AppCompatActivity() {

    private lateinit var viewModel: GestureListViewModel
    private lateinit var adapter: GestureAdapter

    // Views
    private lateinit var toolbar: MaterialToolbar
    private lateinit var recyclerView: RecyclerView
    private lateinit var loadingContainer: LinearLayout
    private lateinit var loadingText: TextView
    private lateinit var errorContainer: LinearLayout
    private lateinit var errorText: TextView
    private lateinit var retryButton: MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_gesture_list)

        setupWindowInsets()
        initViews()
        setupToolbar()
        setupRecyclerView()
        setupViewModel()
        observeUiState()
    }

    private fun setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.gestureListRoot)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun initViews() {
        toolbar = findViewById(R.id.toolbar)
        recyclerView = findViewById(R.id.gestureRecyclerView)
        loadingContainer = findViewById(R.id.loadingContainer)
        loadingText = findViewById(R.id.loadingText)
        errorContainer = findViewById(R.id.errorContainer)
        errorText = findViewById(R.id.errorText)
        retryButton = findViewById(R.id.retryButton)
    }

    private fun setupToolbar() {
        toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun setupRecyclerView() {
        adapter = GestureAdapter { gesture ->
            // Optional: Handle gesture item click (for future video playback)
        }
        recyclerView.adapter = adapter
    }

    private fun setupViewModel() {
        val repository = GestureRepository(applicationContext)
        val factory = GestureListViewModel.Factory(repository)
        viewModel = ViewModelProvider(this, factory)[GestureListViewModel::class.java]

        retryButton.setOnClickListener {
            viewModel.downloadGestures()
        }
    }

    private fun observeUiState() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is GestureListUiState.Loading -> showLoadingState()
                is GestureListUiState.Success -> showSuccessState(state)
                is GestureListUiState.Error -> showErrorState(state)
            }
        }
    }

    private fun showLoadingState() {
        loadingContainer.visibility = View.VISIBLE
        recyclerView.visibility = View.GONE
        errorContainer.visibility = View.GONE
    }

    private fun showSuccessState(state: GestureListUiState.Success) {
        loadingContainer.visibility = View.GONE
        errorContainer.visibility = View.GONE
        recyclerView.visibility = View.VISIBLE
        adapter.submitList(state.gestures)
    }

    private fun showErrorState(state: GestureListUiState.Error) {
        loadingContainer.visibility = View.GONE
        recyclerView.visibility = View.GONE
        errorContainer.visibility = View.VISIBLE
        errorText.text = state.message
    }
}
