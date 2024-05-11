package com.airekeola.animalkingdomexplorer

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.airekeola.animalkingdomexplorer.adapter.SpeciesAdapter
import com.airekeola.animalkingdomexplorer.data.AppDatabase
import com.airekeola.animalkingdomexplorer.data.Species
import com.airekeola.animalkingdomexplorer.databinding.FragmentSpeciesDetailsBinding
import com.airekeola.animalkingdomexplorer.dialog.AppDialogBuilder
import com.airekeola.animalkingdomexplorer.viewmodel.SpeciesDetailsFragmentViewModel
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class SpeciesDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentSpeciesDetailsBinding
    private lateinit var speciesAdapter: SpeciesAdapter
    private lateinit var viewModel: SpeciesDetailsFragmentViewModel
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_species_details, container, false)
        binding = FragmentSpeciesDetailsBinding.bind(view)

        viewModel = ViewModelProvider(this)[SpeciesDetailsFragmentViewModel::class.java]
        launch {
            context?.let {
                val dao =  AppDatabase(it).speciesDao()
                viewModel.initialize(dao)
                initialize()
            }
        }

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initialize() {
        binding.floatActionBtn.setOnClickListener(this::onAddBtnClick)

        this.speciesAdapter = SpeciesAdapter(this.viewModel.speciesesLiveData, this::onItemContextMenuClick)
        binding.speciesRecyclerView.adapter = this.speciesAdapter
        binding.speciesRecyclerView.layoutManager = LinearLayoutManager(this.context)

        viewModel.speciesesLiveData.observe(viewLifecycleOwner) {
            this.speciesAdapter.notifyDataSetChanged()
        }
    }

    private fun onAddBtnClick(view: View) {
        val species = Species("", "")
        editSpecies(species) { species1 ->
            launch {
                context?.let {
                    viewModel.addSpeciesInMemory(species1)
                    Toast.makeText(it, "Added successfully!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun editSpecies(
        species: Species,
        actionType: String = "add",
        onComplete: (Species) -> Unit
    ) {
        val addDialog = AppDialogBuilder.createDialog(
            this.requireContext(),
            R.layout.dialog_species,
            if (actionType == "add") "Add Species" else "Edit Species",
            if (actionType == "add") "Add" else "Update"
        ) { dialogView ->
            if (actionType != "add") {
                dialogView.findViewById<EditText>(R.id.nameText).setText(species.name)
                dialogView.findViewById<EditText>(R.id.descriptionText).setText(species.description)
            }
        }
        addDialog.show()
        addDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val name = addDialog.findViewById<EditText>(R.id.nameText)!!.text.toString()
            val description = addDialog.findViewById<EditText>(R.id.descriptionText)!!.text.toString()

            if (name.isEmpty() || description.isEmpty()) {
                Snackbar.make(it, "All fields are required.", Snackbar.LENGTH_SHORT).show()
            } else {
                onComplete(Species(species.id, name, description))
                addDialog.dismiss()
            }
        }
    }

    private fun onItemContextMenuClick(menuId: Int, position: Int): Boolean {
        return when (menuId) {
            0 -> {
                val species = this.viewModel.specieses[position]
                editSpecies(species, "edit") { newSpecies ->
                    launch {
                        context?.let {
                            viewModel.updateSpecies(position, newSpecies)
                            Toast.makeText(it, "Updated successfully!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                true
            }

            1 -> {
                launch {
                    context?.let {
                        viewModel.deleteSpecies(position)
                        Toast.makeText(it, "Deleted successfully!", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }

            else -> {
                false
            }
        }
    }
}
