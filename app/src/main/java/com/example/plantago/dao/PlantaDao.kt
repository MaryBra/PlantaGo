package com.example.plantago.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.plantago.model.Planta

@Dao
interface PlantaDao {
    @Insert
    suspend fun inserirPlanta(planta: Planta)

    @Update
    suspend fun atualizarPlanta(planta: Planta)

    @Query("SELECT * FROM planta WHERE id = :id")
    suspend fun obterPlantaPorId(id: kotlin.Long): Planta?

    @Query("DELETE FROM planta WHERE id = :id")
    suspend fun deletarPlantaPorId(id: Int)

    @Query("SELECT * FROM planta")
    suspend fun buscarTodas(): List<Planta>
}
