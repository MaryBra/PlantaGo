package com.example.plantago.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categoria")
data class Categoria(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val codigo: String
)