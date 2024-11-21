package com.example.plantago.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "planta")
data class Planta(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val especie: String,
    val fotoUrl: String?,
    val categoriaId: Int // Chave estrangeira
)