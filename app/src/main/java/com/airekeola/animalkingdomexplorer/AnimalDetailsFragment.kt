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
import com.airekeola.animalkingdomexplorer.adapter.AnimalsAdapter
import com.airekeola.animalkingdomexplorer.data.Animal
import com.airekeola.animalkingdomexplorer.data.AppDatabase
import com.airekeola.animalkingdomexplorer.databinding.FragmentAnimalDetailsBinding
import com.airekeola.animalkingdomexplorer.dialog.AppDialogBuilder
import com.airekeola.animalkingdomexplorer.viewmodel.AnimalDetailsFragmentViewModel
import kotlinx.coroutines.launch

/**
 * A simple [Fragment] subclass.
 */
class AnimalDetailsFragment : BaseFragment() {
    private lateinit var binding: FragmentAnimalDetailsBinding
    private lateinit var animalsAdapter: AnimalsAdapter
    private lateinit var viewModel: AnimalDetailsFragmentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_animal_details, container, false)
        binding = FragmentAnimalDetailsBinding.bind(view)

        viewModel = ViewModelProvider(this)[AnimalDetailsFragmentViewModel::class.java]
        launch {
            context?.let {
                val dao =  AppDatabase(it).animalDao()
                viewModel.initialize(dao)
                initialize()
            }
        }

        return binding.root;
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun initialize() {
        binding.floatActionBtn.setOnClickListener(this::onAddBtnClick)

        this.animalsAdapter = AnimalsAdapter(this.viewModel.animalsLiveData, this::onItemContextMenuClick)
        binding.animalRecyclerView.adapter = this.animalsAdapter
        binding.animalRecyclerView.layoutManager = LinearLayoutManager(this.context)

        viewModel.animalsLiveData.observe(viewLifecycleOwner) {
            this.animalsAdapter.notifyDataSetChanged()
        }
    }

    private fun onAddBtnClick(view: View) {
        val animal = Animal("", "", "")
        editAnimal(animal) { animal1 ->
            launch {
                context?.let {
                    viewModel.addAnimalInMemory(animal1)
                    //this.animalsAdapter.notifyItemInserted(viewModel.animals.size - 1)
                    Toast.makeText(it, "Added successfully!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun editAnimal(
        animal: Animal,
        actionType: String = "add",
        onComplete: (Animal) -> Unit
    ) {
        val addDialog = AppDialogBuilder.createDialog(
            this.requireContext(),
            R.layout.dialog_animal,
            if (actionType == "add") "Add animal" else "Edit animal",
            if (actionType == "add") "Add" else "Update"
        ) { dialogView ->
            if (actionType != "add") {
                dialogView.findViewById<EditText>(R.id.nameText).setText(animal.name)
                dialogView.findViewById<EditText>(R.id.habitatText).setText(animal.habitat)
                dialogView.findViewById<EditText>(R.id.dietText).setText(animal.diet)
            }
        }
        addDialog.show()
        addDialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
            val name = addDialog.findViewById<EditText>(R.id.nameText)!!.text.toString()
            val habitat = addDialog.findViewById<EditText>(R.id.habitatText)!!.text.toString()
            val diet = addDialog.findViewById<EditText>(R.id.dietText)!!.text.toString()

            if (name.isEmpty() || habitat.isEmpty() || diet.isEmpty()) {
                Snackbar.make(it, "All fields are required.", Snackbar.LENGTH_SHORT).show()
            } else {
                onComplete(Animal(animal.id, name, habitat, diet))
                addDialog.dismiss()
            }
        }
    }

    private fun onItemContextMenuClick(menuId: Int, position: Int): Boolean {
        return when (menuId) {
            0 -> {
                val animal = this.viewModel.animals[position]
                editAnimal(animal, "edit") { newAnimal ->
                    launch {
                        context?.let {
                            viewModel.updateAnimal(position, newAnimal)
                            Toast.makeText(it, "Updated successfully!", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                true
            }

            1 -> {
                launch {
                    context?.let {
                        viewModel.deleteAnimal(position)
                        //this.animalsAdapter.notifyItemRemoved(position)
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
