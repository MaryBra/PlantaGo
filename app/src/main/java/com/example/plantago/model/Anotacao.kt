package com.example.plantago.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "anotacao")
data class Anotacao(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nome: String,
    val descricao: String,
    val data: String,
    val plantaId: Int // Chave estrangeira
)