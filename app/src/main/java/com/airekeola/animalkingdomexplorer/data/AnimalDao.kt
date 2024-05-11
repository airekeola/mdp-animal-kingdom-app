package com.airekeola.animalkingdomexplorer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface AnimalDao {
    @Query("SELECT * FROM animals")
    suspend fun getAll(): List<Animal>

    @Insert
    suspend fun insert(animal: Animal):Long

    @Query("SELECT * FROM animals WHERE id = :animalId")
    suspend fun getById(animalId: Int): Animal?

    @Query("UPDATE animals SET name = :name, habitat = :habitat, diet = :diet WHERE id = :animalId")
    suspend fun update(animalId: Int, name: String, habitat: String, diet: String)

    @Query("DELETE FROM animals WHERE id = :animalId")
    suspend fun delete(animalId: Int)
}
