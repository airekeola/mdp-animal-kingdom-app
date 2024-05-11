package com.airekeola.animalkingdomexplorer.adapter

import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.airekeola.animalkingdomexplorer.R
import com.airekeola.animalkingdomexplorer.data.Species
import com.airekeola.animalkingdomexplorer.viewholder.SpeciesViewHolder

class SpeciesAdapter (
    private val speciesesLiveData: MutableLiveData<List<Species>>, private val menuClickListener:
        (Int, Int) -> Boolean
) : RecyclerView.Adapter<SpeciesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SpeciesViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_species, parent, false)

        return SpeciesViewHolder(view)
    }

    override fun getItemCount(): Int = speciesesLiveData.value!!.size

    override fun onBindViewHolder(holder: SpeciesViewHolder, position: Int) {
        holder.nameTextView.text = speciesesLiveData.value!![position].name
        holder.descriptionTextView.text = speciesesLiveData.value!![position].description

        holder.itemView.setOnCreateContextMenuListener { menu, _, _ ->
            // Inflate menu and set item click listener
            menu?.add(Menu.NONE, 0, Menu.NONE, "Edit")?.setOnMenuItemClickListener {
                menuClickListener(it.itemId, position)
            }
            menu?.add(Menu.NONE, 1, Menu.NONE, "Delete")?.setOnMenuItemClickListener {
                menuClickListener(it.itemId, position)
            }
        }
    }
}
