package com.example.plantago.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.plantago.model.Anotacao

@Dao
interface AnotacaoDao {
    @Insert
    suspend fun inserirAnotacao(anotacao: Anotacao)

    @Query("SELECT * FROM anotacao WHERE plantaId = :plantaId")
    suspend fun obterAnotacoesPorPlanta(plantaId: Int): List<Anotacao>

    @Query("SELECT * FROM anotacao")
    suspend fun buscarTodas(): List<Anotacao>
}
