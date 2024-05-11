package com.airekeola.animalkingdomexplorer

import android.os.Bundle
import android.text.Layout.Directions
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.airekeola.animalkingdomexplorer.databinding.FragmentDashboardBinding

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment() {
    private lateinit var binding: FragmentDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)

        initialize()

        return binding.root
    }

    private fun initialize(){
        binding.animalDetailsBtn.setOnClickListener(this::onAnimalDetailsBtnClick)
        binding.speciesDetailsBtn.setOnClickListener(this::onSpeciesDetailsBtnClick)
    }

    private fun onAnimalDetailsBtnClick(view:View){
        val directions =DashboardFragmentDirections.actionDashboardFragmentToAnimalDetailsFragment()
        findNavController().navigate(directions)
    }

    private fun onSpeciesDetailsBtnClick(view:View){
        val directions =DashboardFragmentDirections.actionDashboardFragmentToSpeciesDetailsFragment()
        findNavController().navigate(directions)
    }
}
