package com.airekeola.animalkingdomexplorer.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.airekeola.animalkingdomexplorer.data.Animal
import com.airekeola.animalkingdomexplorer.data.AnimalDao
import kotlinx.coroutines.launch

class AnimalDetailsFragmentViewModel : ViewModel() {
    private lateinit var animalDao: AnimalDao
    var animalsLiveData = MutableLiveData<List<Animal>>()
    val animals: List<Animal>
        get() = animalsLiveData.value!!

    suspend fun initialize(dao: AnimalDao) {
        animalDao = dao
        val job = viewModelScope.launch {
            val dbAnimals = animalDao.getAll()
            animalsLiveData.value = dbAnimals.toMutableList()
        }
        job.join()
    }

    fun getAnimal(index: Int): Animal {
        return this.animalsLiveData.value!![index]
    }

    suspend fun getAnimalById(id: Int): Animal {
        return animalDao.getById(id)!!
    }

    suspend fun updateAnimal(index: Int, animal: Animal) {
        val mutableList = this.animals.toMutableList()
        mutableList[index] = animal
        viewModelScope.launch {
            updateAnimalById(animal.id!!, animal)
        }

        this.animalsLiveData.value = mutableList
    }

    private suspend fun updateAnimalById(id: Int, animal: Animal) {
        animalDao.update(id, animal.name, animal.habitat, animal.diet)
    }

    suspend fun deleteAnimal(index: Int) {
        val mutableList = this.animals.toMutableList()
        val animal = mutableList[index]
        mutableList.removeAt(index)

        viewModelScope.launch {
            deleteAnimalById(animal.id!!)
        }

        this.animalsLiveData.value = mutableList
    }

    private suspend fun deleteAnimalById(id: Int) {
        animalDao.delete(id)
    }

    suspend fun addAnimalInMemory(animal: Animal) {
        val mutableList = this.animals.toMutableList()
        mutableList.add(animal)
        viewModelScope.launch {
            val animalId = addAnimal(animal)
            animal.id = animalId
        }
        this.animalsLiveData.value = mutableList
    }

    private suspend fun addAnimal(animal: Animal):Int {
        return animalDao.insert(animal).toInt()
    }
}
