package com.example.plantago.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.plantago.dao.*
import com.example.plantago.model.*

@Database(
    entities = [Planta::class, Categoria::class, Historico::class, Anotacao::class],
    version = 2
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun plantaDao(): PlantaDao
    abstract fun categoriaDao(): CategoriaDao
    abstract fun historicoDao(): HistoricoDao
    abstract fun anotacoesDao(): AnotacaoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null


        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {

                database.execSQL("""
                    CREATE TABLE planta_temp (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        nome TEXT NOT NULL,
                        especie TEXT NOT NULL,
                        fotoUrl TEXT,
                        categoria TEXT
                    )
                """)


                database.execSQL("INSERT INTO planta_temp (id, nome, especie, fotoUrl, categoria) " +
                        "SELECT id, nome, especie, fotoUrl, categoriaId FROM planta")


                database.execSQL("DROP TABLE planta")


                database.execSQL("ALTER TABLE planta_temp RENAME TO planta")
            }
        }

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
