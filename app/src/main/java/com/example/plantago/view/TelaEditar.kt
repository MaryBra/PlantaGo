package com.example.plantago.view

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.plantago.dao.PlantaDao
import com.example.plantago.database.AppDatabase
import com.example.plantago.model.Planta
import com.example.plantago.ui.theme.PlantaGoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch



class TelaEditar : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val plantaId = intent.getLongExtra("plantaId", -1L)
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "planta_database").build()
        val plantaDao = db.plantaDao()

        setContent {
            PlantaGoTheme {
                TelaEditarContent(plantaId = plantaId, plantaDao = plantaDao) { finish() }
            }
        }
    }
}

@Composable
fun TelaEditarContent(plantaId: Long, plantaDao: PlantaDao, onSalvar: () -> Unit) {
    var planta by remember { mutableStateOf<Planta?>(null) }
    var nome by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(true) }

    // Carregar planta
    LaunchedEffect(plantaId) {
        planta = plantaDao.obterPlantaPorId(plantaId)
        planta?.let {
            nome = it.nome
            especie = it.especie
            categoria = it.categoria
        }
        isLoading = false // Carregamento concluído
    }

    if (isLoading) {
        // Exibir um indicador de carregamento ou mensagem
        CircularProgressIndicator(modifier = Modifier.fillMaxSize())
    } else if (planta == null) {
        Text(
            text = "Erro ao carregar planta para edição.",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp)
        )
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = nome,
                onValueChange = { nome = it },
                label = { Text("Nome") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = especie,
                onValueChange = { especie = it },
                label = { Text("Espécie") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = categoria,
                onValueChange = { categoria = it },
                label = { Text("Categoria") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    planta?.let {
                        CoroutineScope(Dispatchers.IO).launch {
                            plantaDao.atualizar(
                                it.copy(nome = nome, especie = especie, categoria = categoria)
                            )
                            onSalvar() // Fecha a tela após salvar
                        }
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Salvar")
            }
        }
    }
}
