package com.example.plantago.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historico")
data class Historico(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val plantaId: Int, // Chave estrangeira
    val dataHora: String,
    val rega: Boolean
)