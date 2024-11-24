package com.example.plantago.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.room.Room
import com.example.plantago.dao.PlantaDao
import com.example.plantago.database.AppDatabase
import com.example.plantago.model.Planta
import com.example.plantago.ui.theme.PlantaGoTheme

class TelaInicial : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "planta_database").build()
        val plantaDao = db.plantaDao()

        setContent {
            PlantaGoTheme {
                MainScreen(plantaDao = plantaDao) { plantaId ->
                    // Navegar para TelaDetalhes passando o ID
                    val intent = Intent(this, TelaDetalhes::class.java)
                    intent.putExtra("plantaId", plantaId)
                    startActivity(intent)
                }
            }
        }
    }
}

@Composable
fun MainScreen(plantaDao: PlantaDao, onPlantaClick: (Long) -> Unit) {
    var plantasList by remember { mutableStateOf<List<Planta>>(emptyList()) }

    // Carrega a lista de plantas ao iniciar
    LaunchedEffect(Unit) {
        plantasList = plantaDao.buscarTodas()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Column {
            Text(
                text = "Minhas Plantas",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )

            if (plantasList.isEmpty()) {
                Text(
                    text = "Nenhuma planta cadastrada!",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                ListagemPlantas(plantasList) { planta ->
                    onPlantaClick(planta.id.toLong())
                }
            }
        }

        FloatingActionButton(
            onClick = { /* Abrir tela de cadastro */ },
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Text("+", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun ListagemPlantas(plantasList: List<Planta>, onPlantaClick: (Planta) -> Unit) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        items(plantasList) { planta ->
            PlantaCard(planta = planta, onClick = { onPlantaClick(planta) })
        }
    }
}

@Composable
fun PlantaCard(planta: Planta, onClick: () -> Unit) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = MaterialTheme.shapes.medium,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = planta.nome,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Espécie: ${planta.especie}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Categoria: ${planta.categoria}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}
