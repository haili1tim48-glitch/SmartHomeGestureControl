package com.example.smarthomegesturecontrolapp.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.smarthomegesturecontrolapp.R
import com.example.smarthomegesturecontrolapp.data.model.Gesture

class GestureAdapter(
    private var gestures: List<Gesture>,
    private val onGestureClick: (Gesture) -> Unit
) : RecyclerView.Adapter<GestureAdapter.GestureViewHolder>() {

    class GestureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivIcon: ImageView = itemView.findViewById(R.id.ivGestureIcon)
        val tvName: TextView = itemView.findViewById(R.id.tvGestureName)
        val tvPath: TextView = itemView.findViewById(R.id.tvGesturePath)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GestureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_gesture, parent, false)
        return GestureViewHolder(view)
    }

    override fun onBindViewHolder(holder: GestureViewHolder, position: Int) {
        val gesture = gestures[position]

        holder.tvName.text = gesture.name
        holder.tvPath.text = gesture.localPath ?: "Not downloaded"

        // Set placeholder icon
        holder.ivIcon.setImageResource(android.R.drawable.ic_menu_gallery)

        holder.itemView.setOnClickListener {
            onGestureClick(gesture)
        }
    }

    override fun getItemCount(): Int = gestures.size

    fun updateGestures(newGestures: List<Gesture>) {
        gestures = newGestures
        notifyDataSetChanged()
    }
}
