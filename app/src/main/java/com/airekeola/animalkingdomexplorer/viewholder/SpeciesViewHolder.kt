package com.airekeola.animalkingdomexplorer.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.airekeola.animalkingdomexplorer.R

class SpeciesViewHolder (view: View) : RecyclerView.ViewHolder(view) {
    val nameTextView: TextView
    val descriptionTextView: TextView

    init {
        nameTextView = view.findViewById(R.id.nameTextView)
        descriptionTextView = view.findViewById(R.id.descriptionTextView)
    }
}
