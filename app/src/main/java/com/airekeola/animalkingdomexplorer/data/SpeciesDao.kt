package com.airekeola.animalkingdomexplorer.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface SpeciesDao {
    @Query("SELECT * FROM species")
    suspend fun getAll(): List<Species>

    @Insert
    suspend fun insert(species: Species):Long

    @Query("SELECT * FROM species WHERE id = :speciesId")
    suspend fun getById(speciesId: Int): Species?

    @Query("UPDATE species SET name = :name, description = :description WHERE id = :speciesId")
    suspend fun update(speciesId: Int, name: String, description: String)

    @Query("DELETE FROM species WHERE id = :speciesId")
    suspend fun delete(speciesId: Int)
}
