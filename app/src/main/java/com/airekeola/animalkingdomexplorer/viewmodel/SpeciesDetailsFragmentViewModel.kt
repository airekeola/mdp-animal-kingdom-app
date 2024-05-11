package com.airekeola.animalkingdomexplorer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airekeola.animalkingdomexplorer.data.Species
import com.airekeola.animalkingdomexplorer.data.SpeciesDao
import kotlinx.coroutines.launch

class SpeciesDetailsFragmentViewModel () : ViewModel() {
    private lateinit var speciesDao: SpeciesDao
    var speciesesLiveData = MutableLiveData<List<Species>>()
    val specieses: List<Species>
        get() = speciesesLiveData.value!!

    suspend fun initialize(dao: SpeciesDao) {
        speciesDao = dao
        val job = viewModelScope.launch {
            val dbSpecies = speciesDao.getAll()
            speciesesLiveData.value = dbSpecies.toMutableList()
        }
        job.join()
    }

    fun getSpecies(index: Int): Species {
        return this.speciesesLiveData.value!![index]
    }

    suspend fun getSpeciesById(id: Int): Species {
        return speciesDao.getById(id)!!
    }

    suspend fun updateSpecies(index: Int, species: Species) {
        val mutableList = this.specieses.toMutableList()
        mutableList[index] = species
        viewModelScope.launch {
            updateSpeciesById(species.id!!, species)
        }

        this.speciesesLiveData.value = mutableList
    }

    private suspend fun updateSpeciesById(id: Int, species: Species) {
        speciesDao.update(id, species.name, species.description)
    }

    suspend fun deleteSpecies(index: Int) {
        val mutableList = this.specieses.toMutableList()
        val species = mutableList[index]
        mutableList.removeAt(index)

        viewModelScope.launch {
            deleteSpeciesById(species.id!!)
        }

        this.speciesesLiveData.value = mutableList
    }

    private suspend fun deleteSpeciesById(id: Int) {
        speciesDao.delete(id)
    }

    suspend fun addSpeciesInMemory(species: Species) {
        val mutableList = this.specieses.toMutableList()
        mutableList.add(species)
        viewModelScope.launch {
            val speciesId = addSpecies(species)
            species.id = speciesId
        }
        this.speciesesLiveData.value = mutableList
    }

    private suspend fun addSpecies(species: Species):Int {
        return speciesDao.insert(species).toInt()
    }
}
