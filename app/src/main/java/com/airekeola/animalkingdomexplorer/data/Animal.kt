package com.airekeola.animalkingdomexplorer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "animals")
data class Animal(
    val name: String,
    val habitat: String,
    val diet: String
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    constructor(
        id: Int?,
        name: String,
        habitat: String,
        diet: String
    ) : this(name, habitat, diet) {
        this.id = id;
    }
}
