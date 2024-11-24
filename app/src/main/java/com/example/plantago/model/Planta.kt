package com.example.plantago.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "planta")
data class Planta(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val descricao: String,
    val fotoUrl: String? = null,
    val categoria: String
)