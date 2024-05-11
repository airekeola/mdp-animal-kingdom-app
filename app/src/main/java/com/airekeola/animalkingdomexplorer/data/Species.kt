package com.airekeola.animalkingdomexplorer.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "species")
data class Species(
    var name: String,
    var description: String
){
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null

    constructor(
        id: Int?,
        name: String,
        description: String
    ) : this(name, description) {
        this.id = id;
    }
}
