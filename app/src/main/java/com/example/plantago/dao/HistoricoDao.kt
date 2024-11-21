package com.example.plantago.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.plantago.model.Historico

@Dao
interface HistoricoDao {
    @Insert
    suspend fun inserirHistorico(historico: Historico)

    @Query("SELECT * FROM historico WHERE plantaId = :plantaId")
    suspend fun obterHistoricosPorPlanta(plantaId: Int): List<Historico>

    @Query("SELECT * FROM historico")
    suspend fun buscarTodos(): List<Historico>
}
