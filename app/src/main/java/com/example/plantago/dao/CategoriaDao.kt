package com.example.plantago.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.plantago.model.Categoria

@Dao
interface CategoriaDao {
    @Insert
    suspend fun inserirCategoria(categoria: Categoria)

    @Query("SELECT * FROM categoria WHERE id = :id")
    suspend fun obterCategoriaPorId(id: Int): Categoria?

    @Query("SELECT * FROM categoria")
    suspend fun buscarTodas(): List<Categoria>
}
