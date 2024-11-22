package com.example.plantago.view
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.plantago.dao.PlantaDao
import com.example.plantago.database.AppDatabase
import com.example.plantago.model.Planta
import kotlinx.coroutines.launch

class TelaInicial : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "planta_database").build()
        val plantaDao = db.plantaDao()

        setContent {
            MainScreen(plantaDao)
        }
    }
}


@Composable
fun MainScreen(plantaDao: PlantaDao) {
    var nome by remember { mutableStateOf("") }
    var especie by remember { mutableStateOf("") }
    var categoria by remember { mutableStateOf("") }
    var plantasList by remember { mutableStateOf<List<Planta>>(emptyList()) }
    val coroutineScope = rememberCoroutineScope()

    // Carrega a lista de plantas ao iniciar
    LaunchedEffect(Unit) {
        plantasList = plantaDao.buscarTodas()
    }

    Column(modifier = Modifier.padding(16.dp)) {
        // Campo Nome
        TextField(
            value = nome,
            onValueChange = { nome = it },
            label = { Text("Nome da Planta") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo Espécie
        TextField(
            value = especie,
            onValueChange = { especie = it },
            label = { Text("Espécie da Planta") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        // Campo Categoria
        TextField(
            value = categoria,
            onValueChange = { categoria = it },
            label = { Text("Categoria da Planta") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Botão para adicionar planta
        Button(onClick = {
            if (nome.isNotEmpty() && especie.isNotEmpty() && categoria.isNotEmpty()) {
                val novaPlanta = Planta(
                    nome = nome,
                    especie = especie,
                    categoria = categoria
                )
                coroutineScope.launch {
                    plantaDao.inserirPlanta(novaPlanta)
                    plantasList = plantaDao.buscarTodas()
                }
                nome = ""
                especie = ""
                categoria = ""
            }
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Adicionar Planta")
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Lista de plantas cadastradas
        Text("Plantas Cadastradas:", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(8.dp))

        plantasList.forEach { planta ->
            Text(
                text = "Nome: ${planta.nome}, Espécie: ${planta.especie}, Categoria: ${planta.categoria}",
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }
}