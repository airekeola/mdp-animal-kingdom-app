package com.airekeola.animalkingdomexplorer.adapter

import android.view.LayoutInflater
import android.view.Menu
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.airekeola.animalkingdomexplorer.R
import com.airekeola.animalkingdomexplorer.data.Animal
import com.airekeola.animalkingdomexplorer.viewholder.AnimalViewHolder

class AnimalsAdapter (
    private val animalsLiveData: MutableLiveData<List<Animal>>, private val menuClickListener:
        (Int, Int) -> Boolean
) : RecyclerView.Adapter<AnimalViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimalViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_animal, parent, false)

        return AnimalViewHolder(view)
    }

    override fun getItemCount(): Int = animalsLiveData.value!!.size

    override fun onBindViewHolder(holder: AnimalViewHolder, position: Int) {
        holder.nameTextView.text = animalsLiveData.value!![position].name
        holder.habitatTextView.text = animalsLiveData.value!![position].habitat
        holder.dietTextView.text = animalsLiveData.value!![position].diet

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
