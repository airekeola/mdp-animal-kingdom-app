package com.airekeola.animalkingdomexplorer.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airekeola.animalkingdomexplorer.R

class AnimalViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val nameTextView: TextView
    val habitatTextView: TextView
    val dietTextView: TextView

    init {
        nameTextView = view.findViewById(R.id.nameTextView)
        habitatTextView = view.findViewById(R.id.habitatTextView)
        dietTextView = view.findViewById(R.id.dietTextView)
    }
}
