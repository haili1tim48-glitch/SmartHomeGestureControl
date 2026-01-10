package com.example.smarthomegesturecontrolapp.ui.gesturelist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomegesturecontrolapp.R
import com.example.smarthomegesturecontrolapp.data.model.Gesture

/**
 * RecyclerView Adapter for displaying gesture items.
 * Uses ListAdapter with DiffUtil for efficient list updates.
 */
class GestureAdapter(
    private val onItemClick: ((Gesture) -> Unit)? = null
) : ListAdapter<Gesture, GestureAdapter.GestureViewHolder>(GestureDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GestureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gesture, parent, false)
        return GestureViewHolder(view, onItemClick)
    }

    override fun onBindViewHolder(holder: GestureViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class GestureViewHolder(
        itemView: View,
        private val onItemClick: ((Gesture) -> Unit)?
    ) : RecyclerView.ViewHolder(itemView) {

        private val iconImageView: ImageView = itemView.findViewById(R.id.gestureIcon)
        private val nameTextView: TextView = itemView.findViewById(R.id.gestureName)
        private val pathTextView: TextView = itemView.findViewById(R.id.gesturePath)

        fun bind(gesture: Gesture) {
            nameTextView.text = gesture.name
            pathTextView.text = gesture.localPath ?: "Path not available"

            // Set placeholder icon (generic video icon)
            iconImageView.setImageResource(R.drawable.ic_video_placeholder)

            itemView.setOnClickListener {
                onItemClick?.invoke(gesture)
            }
        }
    }

    /**
     * DiffUtil callback for efficient list updates
     */
    class GestureDiffCallback : DiffUtil.ItemCallback<Gesture>() {
        override fun areItemsTheSame(oldItem: Gesture, newItem: Gesture): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Gesture, newItem: Gesture): Boolean {
            return oldItem == newItem
        }
    }
}
