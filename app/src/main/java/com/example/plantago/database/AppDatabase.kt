package com.example.plantago.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.plantago.dao.*
import com.example.plantago.model.*

@Database(
    entities = [Planta::class, Categoria::class, Historico::class, Anotacao::class],
    version = 1
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun plantaDao(): PlantaDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun historicoDao(): HistoricoDao
    abstract fun anotacoesDao(): AnotacaoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}